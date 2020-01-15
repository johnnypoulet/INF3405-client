package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	private static Socket socket;
	
	public static void main(String[] args) throws Exception
	{
		String serverAddress = "";
		int serverPort = 0;
		String username = "";
		String password = "";
		
		Scanner keyboard = new Scanner(System.in);
		
		// Entrez l'adresse IP
		System.out.println("Entrez l'adresse IP du serveur:");
		String serverAddressIn = keyboard.next();
		String[] tempAddr = serverAddressIn.split("\\.");
		
		while (!Validators.validateIPAddress(tempAddr)) {
			System.out.println("Entrez l'adresse IP du serveur:");
			serverAddressIn = keyboard.next();
			tempAddr = serverAddressIn.split("\\.");	
		}
		// L'adresse est valide
		serverAddress = serverAddressIn;
		
		// Entrez le numero de port
		System.out.println("Entrez le numero du port du serveur (entre 5000 et 5050):");
		int serverPortIn = keyboard.nextInt();
		
		while (!Validators.validatePortNumber(serverPortIn)) {
			System.out.println("Entrez le numero du port du serveur (entre 5000 et 5050):");
			serverPortIn = keyboard.nextInt();
		}
		// Le numero de port est valide
		serverPort = serverPortIn;
		
		// Entrez le nom d'utilisateur
		System.out.println("Veuillez entrer votre nom d'utilisateur, qu'il soit nouveau ou deja existant. Entre 4 et 20 caracteres.");
		String usernameIn = keyboard.next();
		while (!Validators.validateUsername(usernameIn)) {
			System.out.println("Veuillez entrer votre nom d'utilisateur, qu'il soit nouveau ou deja existant. Entre 4 et 20 caracteres.");
			usernameIn = keyboard.next();
		}
		// Le nom d'utilisateur entre est valide
		username = usernameIn;

		// On doit verifier sur le serveur si le nom d'utilisateur existe
		DataInputStream in = new DataInputStream(socket.getInputStream());
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		out.writeUTF(username);

		// On attend la reponse
		String responseUN = in.readUTF();
		if (responseUN == "true") {
			System.out.format("Rebienvenue %s!", usernameIn);
		} else {
			System.out.format("Bienvenue dans PolySobel, %s!", usernameIn);
		}

		// Entrez le mot de passe
		System.out.format("Veuillez entrer votre mot de passe (entre 4 et 20 caracteres):");
		String passwordIn = keyboard.next();
		while (!Validators.validatePassword(passwordIn)) {
			System.out.format("Veuillez entrer votre mot de passe (entre 4 et 20 caracteres):");
			passwordIn = keyboard.next();
		}
		// Le mot de passe est valide
		password = passwordIn;

		out.writeUTF(password);
		// On attend la réponse
		String responsePW = in.readUTF();
		if (responsePW == "true") {
			System.out.format("Utilisateur identifie. Merci!");
		} else {
			System.out.format("Mot de passe refuse (ou erreur au serveur).");
			socket.close();
			keyboard.close();
			System.exit(0);
		}
		
		// Entrez le nom du fichier
		System.out.println("Veuillez entrer le nom de l'image: ");
		String fileName = keyboard.next();
		
		while(!Validators.validateFileName(fileName)) {
			
		}
		
		socket = new Socket(serverAddress, serverPort);
		
		// Envoi de la photo
		
		// Attente de reception
				
		socket.close();
		keyboard.close();
	}
}
