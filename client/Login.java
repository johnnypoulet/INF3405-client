package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Login {
	static Scanner keyboard = new Scanner(System.in);
	static String serverAddress = "";
	static int serverPort = 0;
	static String username = "";
	static String password = "";
	private static Socket socket;
	static DataInputStream in;
	static DataOutputStream out;
	
	public static void close() throws Exception {
		socket.close();
		keyboard.close();
	}
	
	public static boolean startConnectionRoutine() throws Exception {
		try {
			// Obtention des informations serveur
			serverAddress = serverConnection();
			serverPort = portConnection();
			socket = new Socket(Login.serverAddress, Login.serverPort);
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			
			// Obtention des credentials
			username = usernameConnection();
			password = passwordConnection();
			
			// Envoi username au serveur pour verifier si le nom d'utilisateur existe
			out.writeUTF(Login.username);
			
			// On attend la reponse du serveur pour savoir si l'utilisateur existe
			// Utilisateur existant
			if (Login.in.readBoolean()) {
				System.out.format("Rebienvenue, utilisateur existant %s! ", Login.username);
				out.writeUTF(Login.password);
				
				// On attend la reponse
				if (in.readBoolean()) {
					System.out.println("Utilisateur identifie. Merci!");
					return true;
				} else {
					System.out.println("Mot de passe refuse (ou erreur au serveur).");
					return false;
				}
			// Nouvel utilisateur
			} else {
				System.out.format("Nouvel utilisateur %s. \n", Login.username);
				out.writeUTF(Login.password);
				
				// On attend la reponse
				if (in.readBoolean()) {
					return true;
				} else {
					System.out.println("Erreur lors de la connexion. Le serveur a refuse le mot de passe.");
					return false;
				}
			}
		} catch (Exception e) {
			System.out.println("La connexion n'a pas pu etre etablie.");
			return false;
		}
	}
	
	public static String serverConnection() throws Exception {
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
		return serverAddressIn;
	}

	public static int portConnection() throws Exception {
		// Entrez le numero de port
		System.out.println("Entrez le numero du port du serveur (entre 5000 et 5050):");
		int serverPortIn = keyboard.nextInt();
		
		while (!Validators.validatePortNumber(serverPortIn)) {
			System.out.println("Entrez le numero du port du serveur (entre 5000 et 5050):");
			serverPortIn = keyboard.nextInt();
		}
		// Le numero de port est valide
		return serverPortIn;
	}
	
	public static String usernameConnection() throws Exception {
		// Entrez le nom d'utilisateur
		System.out.println("Veuillez entrer votre nom d'utilisateur (nouveau ou deja existant), entre 4 et 20 caracteres.");
		String usernameIn = keyboard.next();
		while (!Validators.validateUsername(usernameIn)) {
			System.out.println("Veuillez entrer votre nom d'utilisateur (nouveau ou deja existant), entre 4 et 20 caracteres.");
			usernameIn = keyboard.next();
		}
		// Le nom d'utilisateur entre est valide
		return usernameIn;
	}
	
	public static String passwordConnection() throws Exception {
		// Entrez le mot de passe
		System.out.format("Veuillez entrer votre mot de passe (entre 4 et 20 caracteres): ");
		String passwordIn = keyboard.next();
		while (!Validators.validatePassword(passwordIn)) {
			System.out.format("Veuillez entrer votre mot de passe (entre 4 et 20 caracteres): ");
			passwordIn = keyboard.next();
		}
		// Le mot de passe est valide
		return passwordIn;
	}
}
