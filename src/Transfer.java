package client;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class Transfer {
	static Scanner keyboard = new Scanner(System.in);
	static String filePathIn = "";
	static String filePathOut = "";
	static String formatIn = "";
	static BufferedImage imageConverted;
	static boolean startSuccessful = false;
	static boolean startPostSuccesful = false;
	
	public static void close() throws Exception {
		keyboard.close();
	}
	
	public static void startRoutine() throws Exception {
		try {
			// Obtention du nom du fichier
			Transfer.fileNameIn();
			
			// Transformation du fichier en BufferedImage
			System.out.println("Lecture de l'image...");
			Image image = ImageIO.read(new File(Transfer.filePathIn));
			BufferedImage buffered = (BufferedImage) image;
			ByteArrayOutputStream baOut= new ByteArrayOutputStream();
			ImageIO.write(buffered, "png", baOut);
			System.out.println("Lecture terminee. Transmission vers le serveur...");

			// Envoi de la taille de l'image
			byte[] len = ByteBuffer.allocate(4).putInt(baOut.size()).array();
			Login.out.write(len);
			
			// Envoi du nom de l'image
			Login.out.writeUTF(filePathIn);
			
			// Envoi de l'image
			Login.out.write(baOut.toByteArray());
			Login.out.flush();
			System.out.println("Image envoyee au serveur. Attente de la reponse...");
			
			// Attente de reception
			int lenMod = Login.in.readInt();
			System.out.format("Taille de l'image en reception: %s octets. Reception de l'image en cours...\n", lenMod);
			byte[] inputBytes = Login.in.readNBytes(lenMod);
			System.out.println("Image recue au format: " + formatIn + "Deconnexion du serveur...");
			InputStream inp = new ByteArrayInputStream(inputBytes);
			imageConverted = ImageIO.read(inp);
			
			// Routine terminee
			startSuccessful = true;
			return;
		} catch (Exception e) {
			System.out.println("Erreur dans la lecture du fichier. Assurez-vous de placer le fichier dans le repertoire courant.");
			startSuccessful = false;
			return;
		}
	}
	
	public static void startPostRoutine() throws Exception {
		// On ecrit l'image recue dans un fichier
		Transfer.fileNameOut();
		File file = new File(filePathOut);
		while (file.exists()) {
			boolean tempResponse = Transfer.fileNameOverwrite();
			// On ecrase
			if (tempResponse) {
				System.out.println("Ecrasement du fichier: " + file.toString());
				file.delete();
				file = new File(filePathOut);
				break;
			} else {
				Transfer.fileNameOut();
				file = new File(filePathOut);
			}
		}
		try {
			// On ecrit la nouvelle image dans le fichier
			ImageIO.write(imageConverted, formatIn, file);
			startPostSuccesful = true;
		} catch (IOException e) {
			System.out.println("Erreur dans l'ecriture du fichier.");
			Login.close();
			startPostSuccesful = false;
		}
	}
	
	public static void fileNameIn() throws Exception {
		// Entrez le nom du fichier
		System.out.println("L'image doit se trouver dans le repertoire suivant:");
		System.out.println(System.getProperty("user.dir"));
		System.out.println("Cette application supporte les formats JPG, PNG et BMP. Veuillez entrer le nom de l'image que vous voulez traiter:");
		String fileNameInput = keyboard.next();
		
		try {
			while(!Validators.validateFileName(fileNameInput)) {
			    System.out.println("Erreur dans le nom du fichier.");
				System.out.println("Cette application supporte les formats JPG, PNG et BMP. Veuillez entrer le nom de l'image que vous voulez traiter:");
				fileNameInput = keyboard.next();
			}
		} catch (Exception e) {
			System.out.println("Erreur durant l'obtention de l'entree clavier.");
			e.printStackTrace();
		}
		String temp[] = fileNameInput.split("\\.");
		filePathIn = fileNameInput;
		formatIn = temp[1];
	}
	
	public static void fileNameOut() throws Exception {
		// Entrez le nom du fichier
		System.out.format("L'image sera sauvegardee au format %s dans le repertoire suivant:\n", formatIn);
		System.out.println(System.getProperty("user.dir"));
		System.out.println("Veuillez entrer le nom du fichier que vous voulez sauvegarder, sans l'extension:");
		String filePathInput = keyboard.next();
		
		try {
			while(!Validators.validateFileNameToSave(filePathInput)) {
			    System.out.println("Erreur dans le nom du fichier.");
				System.out.println("Veuillez entrer le nom du fichier que vous voulez sauvegarder, sans l'extension:");
				filePathInput = keyboard.next();
			}
		} catch (Exception e) {
			System.out.println("Erreur durant l'obtention de l'entree clavier.");
			e.printStackTrace();
		}
		filePathOut = filePathInput + "." + formatIn;
	}
	
	public static boolean fileNameOverwrite() throws Exception {
		String message = "Fichier deja existant. Ecraser? Entrez Oui ou Non.";
		System.out.println(message);
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
