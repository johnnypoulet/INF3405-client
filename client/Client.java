package client;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.net.Socket;
import java.util.Scanner;

import javax.imageio.ImageIO;

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
		
		// Ouverture du socket de communication avec serveur
		socket = new Socket(serverAddress, serverPort);
		DataInputStream in = new DataInputStream(socket.getInputStream());
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		
		// Entrez le nom d'utilisateur
		System.out.println("Veuillez entrer votre nom d'utilisateur, qu'il soit nouveau ou deja existant. Entre 4 et 20 caracteres.");
		String usernameIn = keyboard.next();
		while (!Validators.validateUsername(usernameIn)) {
			System.out.println("Veuillez entrer votre nom d'utilisateur, qu'il soit nouveau ou deja existant. Entre 4 et 20 caracteres.");
			usernameIn = keyboard.next();
		}
		// Le nom d'utilisateur entre est valide
		username = usernameIn;
		
		// Envoi username au server pour verifier sur le serveur si le nom d'utilisateur existe
		out.writeUTF(username);
		// On attend la reponse
		boolean responseONE = in.readBoolean();
		if (responseONE == true) {
			System.out.format("Rebienvenue %s! ", usernameIn);
		} else {
			System.out.format("Bienvenue dans PolySobel, %s! ", usernameIn);
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
		
		
		/* SCENARIO 1 UTILISATEUR DANS BASE DE DONN�E*/
		if (responseONE)
		{
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
		}
		/* SCENARIO 2 NOUVEL UTILISATEUR */
		else
		{
			out.writeUTF(password);
			System.out.format("Nouvel utilisateur enregistre. Merci!");
		}
		
		
		// Entrez le nom du fichier
		System.out.println("Veuillez entrer le nom de l'image: ");
		String fileName = keyboard.next();
		
		while(!Validators.validateFileName(fileName)) {
			
		}
		
		// Envoi de la photo
		Image image = ImageIO.read(new File(fileName));
		BufferedImage buffered = (BufferedImage) image;
		ByteArrayOutputStream baOut= new ByteArrayOutputStream();
		ImageIO.write(buffered,"png",baOut);
		out.write(baOut.toByteArray());
		out.flush();
		System.out.println("Image envoye au serveur");
		// Attente de reception
				
		socket.close();
		keyboard.close();
	}
}
