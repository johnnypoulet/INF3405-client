package client;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class Transfer {
	static Scanner keyboard = new Scanner(System.in);
	static String fileName = "";
	static BufferedImage imageConverted;
	static boolean startSuccessful = false;
	static boolean startPostSuccesful = false;
	
	public static void close() throws Exception {
		keyboard.close();
	}
	
	public static void startRoutine() throws Exception {
		fileName = Transfer.fileNameIn();
		// Transformation du fichier en BufferedImage
		try {
			Image image = ImageIO.read(new File(Transfer.fileName));
			BufferedImage buffered = (BufferedImage) image;
			ByteArrayOutputStream baOut= new ByteArrayOutputStream();
			ImageIO.write(buffered, "png", baOut);

			// Envoi de la taille de l'image
			byte[] len = ByteBuffer.allocate(4).putInt(baOut.size()).array();
			Login.out.write(len);
			
			// Envoi du nom de l'image
			Login.out.writeUTF(fileName);
			
			// Envoi de l'image
			Login.out.write(baOut.toByteArray());
			Login.out.flush();
			System.out.println("Image envoyee au serveur. Attente de la reponse...");
			
			// Attente de reception
			int lenMod = Login.in.readInt();
			System.out.format("Taille de l'image en reception: %s octets. Reception de l'image en cours...\n", lenMod);
			byte[] inputBytes = Login.in.readNBytes(lenMod);
			System.out.println("Image recue.");
			InputStream inp = new ByteArrayInputStream(inputBytes);
			imageConverted = ImageIO.read(inp);
			
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
		try {
			String pathOut = Transfer.fileNameOut();
			String[] temp = pathOut.split("\\.");
			File file = new File(pathOut);
			if (file.exists()) {
				boolean tempResponse = Transfer.fileNameOverwrite();
				// On ecrase
				if (tempResponse) {
					System.out.println("Ecrasement du fichier: " + file.toString());
					ImageIO.write(imageConverted, temp[1], file);
				} else {
					pathOut = Transfer.fileNameOut();
					temp = pathOut.split("\\.");
				}
			}
			ImageIO.write(imageConverted, temp[1], file);
			startPostSuccesful = true;
			return;
		} catch (Exception e) {
			System.out.println("Erreur dans l'ecriture du fichier.");
			Login.close();
			startPostSuccesful = false;
			return;
		}
	}
	
	public static String fileNameIn() {
		// Entrez le nom du fichier
		System.out.println("L'image doit se trouver dans le repertoire suivant (ou un sous-repertoire si vous l'incluez dans le nom du fichier):");
		System.out.println(System.getProperty("user.dir"));
		System.out.println("Cette application supporte les formats JPG, PNG et BMP. Veuillez entrer le nom de l'image que vous voulez traiter:");
		String fileName = keyboard.next();
		
		try {
			while(!Validators.validateFileName(fileName)) {
			    System.out.println("Erreur dans le nom du fichier.");
				System.out.println("Cette application supporte les formats JPG, PNG et BMP. Veuillez entrer le nom de l'image que vous voulez traiter:");
				fileName = keyboard.next();
			}
		} catch (Exception e) {
			System.out.println("apres exception filenameIn");
			e.printStackTrace();
		}
		return fileName;
	}
	
	public static String fileNameOut() {
		// Entrez le nom du fichier
		System.out.println("L'image sera sauvegardee dans le repertoire suivant (ou un sous-repertoire si vous l'incluez dans le nom du fichier): ");
		System.out.println(System.getProperty("user.dir"));
		System.out.println("Cette application supporte les formats JPG, PNG et BMP. Veuillez entrer le nom du fichier que vous voulez sauvegarder: ");
		String fileName = keyboard.next();
		
		try {
			while(!Validators.validateFileName(fileName)) {
			    System.out.println("Erreur dans le nom du fichier.");
				System.out.println("Cette application supporte les formats JPG, PNG et BMP. Veuillez entrer le nom du fichier que vous voulez sauvegarder: ");
				fileName = keyboard.next();
			}
		} catch (Exception e) {
			System.out.println("apres exception filenameOut");
			e.printStackTrace();
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
