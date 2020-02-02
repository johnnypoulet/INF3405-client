package client;

import java.util.Scanner;

public class Login {
	static Scanner keyboard = new Scanner(System.in);
	
	public static void close() throws Exception {
		keyboard.close();
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
		System.out.println("Veuillez entrer votre nom d'utilisateur, qu'il soit nouveau ou deja existant. Entre 4 et 20 caracteres.");
		String usernameIn = keyboard.next();
		while (!Validators.validateUsername(usernameIn)) {
			System.out.println("Veuillez entrer votre nom d'utilisateur, qu'il soit nouveau ou deja existant. Entre 4 et 20 caracteres.");
			usernameIn = keyboard.next();
		}
		// Le nom d'utilisateur entre est valide
		return usernameIn;
	}
	
	public static String passwordConnection() throws Exception {
		// Entrez le mot de passe
		System.out.format("Veuillez entrer votre mot de passe (entre 4 et 20 caracteres):");
		String passwordIn = keyboard.next();
		while (!Validators.validatePassword(passwordIn)) {
			System.out.format("Veuillez entrer votre mot de passe (entre 4 et 20 caracteres):");
			passwordIn = keyboard.next();
		}
		// Le mot de passe est valide
		return passwordIn;
	}
}
