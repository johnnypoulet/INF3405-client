package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class Validators {
	static HashMap<String, String> userDataMap = new HashMap<String, String>();
	static String path = "credentials.csv";
	static String currentUsername = "";

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
			userDataMap.clear();
			String line = "";
			String[] data;
			while ((line = reader.readLine()) != null) {
				data = line.split(",");
				userDataMap.put(data[0], data[1]);
			}
			reader.close();
		} else {
			System.out.println("Erreur dans la lecture du fichier.");
		}
	}
	
	public static void manageFile() throws Exception {
		File file = new File(path);
		FileWriter writer = new FileWriter(path);
		// Si le fichier n'existe pas
		if (!file.exists()) {
			userDataMap.forEach((username, password)->{
				try {
					writer.append(username);
					writer.append(",");
					writer.append(password);
					writer.append("\n");
				} catch (IOException e) {
					System.out.println("Erreur dans l'ecriture sur le fichier.");
					e.printStackTrace();
				}
			});
			writer.flush();
			writer.close();
		}
	}
	
	public static boolean validateUsername(String input) throws Exception {
		return userDataMap.containsKey(input);
	}
	
	public static boolean validatePassword(String username, String input) throws Exception {
		if (userDataMap.get(username) == input) {
			return true;
		} else {
			return false;
		}
	}
}
