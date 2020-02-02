package client;

import java.util.Scanner;

public class Transfer {
	static Scanner keyboard = new Scanner(System.in);
	
	public static void close() throws Exception {
		keyboard.close();
	}
	
	public static String fileNameIn() throws Exception {
		// Entrez le nom du fichier
		System.out.println("L'image doit se trouver dans le repertoire suivant (ou un sous-repertoire si vous l'incluez dans le nom du fichier): " + System.getProperty("user.dir"));
		System.out.println("Cette application supporte les formats JPG, PNG et BMP. Veuillez entrer le nom de l'image que vous voulez traiter: ");
		String fileName = keyboard.next();
		
		while(!Validators.validateFileName(fileName)) {
		    System.out.println("Erreur dans le nom du fichier.");
			System.out.println("Cette application supporte les formats JPG, PNG et BMP. Veuillez entrer le nom de l'image que vous voulez traiter: ");
			fileName = keyboard.next();
		}
		return fileName;
	}
	
	public static String fileNameOut() throws Exception {
		// Entrez le nom du fichier
		System.out.println("L'image sera sauvegardee dans le repertoire suivant (ou un sous-repertoire si vous l'incluez dans le nom du fichier): " + System.getProperty("user.dir"));
		System.out.println("Cette application supporte les formats JPG, PNG et BMP. Veuillez entrer le nom du fichier que vous voulez sauvegarder: ");
		String fileName = keyboard.next();
		
		while(!Validators.validateFileName(fileName)) {
		    System.out.println("Erreur dans le nom du fichier.");
			System.out.println("Cette application supporte les formats JPG, PNG et BMP. Veuillez entrer le nom du fichier que vous voulez sauvegarder: ");
			fileName = keyboard.next();
		}
		return fileName;
	}
	
	public static boolean fileNameOverwrite() throws Exception {
		String message = "Fichier deja existant. Ecraser? Entrez Oui ou Non.";
		String response = keyboard.next();
		int result = Validators.validateResponse(response, message);
		while (result == 0) {
			System.out.println("Reponse inintelligible. Veuillez reessayer. Entrez Oui ou Non.");
			response = keyboard.next();
			result = Validators.validateResponse(response, message);
		}
		// L'utilisateur repond Oui pour ecraser
		if (result == 1) {
			return true;
		}
		return false;		
	}
}
