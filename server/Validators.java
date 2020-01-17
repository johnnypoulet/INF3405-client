package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Validators {
	static String path = "credentials.csv";
	static String currentUsername = "";
	static String currentPassword = "";

	// Valider l'adresse (chiffres et nombre de points)
	public static boolean validateIPAddress(String[] input) throws Exception {
		if (input.length != 4) {
			System.out.println("Erreur dans l'addresse. Veuillez reessayer.");
			return false;
		}
		for (int i = 0; i < input.length; i++) {
			try {
				int tempInt = Integer.parseInt(input[i]);
				if (tempInt < 0 || tempInt > 255) {
					return false;
				}
			} catch (Exception e) {
				System.out.println("Erreur dans l'addresse. Veuillez reessayer.");
				return false;
			}
		}
		return true;
	}
	
	// Valider le numero de port
	public static boolean validatePortNumber(int input) throws Exception {
		if (input < 5000 || input > 5050) {
			System.out.println("Erreur dans le numero de port. Veuillez reessayer.");
			return false;
		}
		return true;
	}
	
	// Remplir la map et créer le fichier si requis
	public static void initMap() throws Exception {
		File file = new File(path);
		// Le fichier existe
		if (file.exists()) {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line = "";
			String[] data;
			while ((line = reader.readLine()) != null) {
				data = line.split(",");
				currentUsername = data[0];
				currentPassword = data[1];
			}
			reader.close();
		} else {
			System.out.println("Erreur dans la lecture du fichier.");
		}
	}
	
	public static boolean validateUsername(String input) throws Exception {
		File file = new File(path);
		// Le fichier existe
		if (file.exists()) {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line = "";
			String[] data;
			while ((line = reader.readLine()) != null) {
				data = line.split(",");
				currentUsername = data[0];
				if (input == currentUsername) {
					return true;
				}
				currentPassword = data[1];
			}
			reader.close();
		} else {
			System.out.println("Erreur dans la lecture du fichier.");
		}
		return false;
	}
	
	public static boolean validatePassword(String username, String input) throws Exception {
		if (currentPassword == input) {
			return true;
		} else {
			return false;
		}
	}
}
