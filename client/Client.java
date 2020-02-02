package client;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

public class Client {
	private static Socket socket;
	
	public static void main(String[] args) throws Exception
	{
		System.out.println("Bienvenue dans l'application PolySobel - Client! (Copyright Derek Bernard & Jean-Olivier Dalphond 2020)");
		
		String serverAddress = "";
		int serverPort = 0;
		String username = "";
		String password = "";
		String fileName = "";
		
		serverAddress = Login.serverConnection();
		serverPort = Login.portConnection();
		
		// Ouverture du socket de communication avec serveur
		socket = new Socket(serverAddress, serverPort);
		DataInputStream in = new DataInputStream(socket.getInputStream());
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());

		// Envoi username au serveur pour verifier si le nom d'utilisateur existe
		username = Login.usernameConnection();
		out.writeUTF(username);
		// On attend la reponse
		boolean responseUN = in.readBoolean();
		if (responseUN) {
			System.out.format("Rebienvenue, utilisateur existant %s! ", username);
			password = Login.passwordConnection();
			out.writeUTF(password);
			// On attend la reponse
			boolean responsePW = in.readBoolean();
			if (responsePW) {
				System.out.format("Utilisateur identifie. Merci! \n");
			} else {
				System.out.format("Mot de passe refuse (ou erreur au serveur). \n");
				socket.close();
				serverAddress = Login.serverConnection();
				serverPort = Login.portConnection();
				socket = new Socket(serverAddress, serverPort);
			}		
		} else {
			System.out.format("Bienvenue dans PolySobel, nouvel utilisateur %s! \n", username);
			password = Login.passwordConnection();
			out.writeUTF(password);
		}
		fileName = Transfer.fileNameIn();
		
		// Transformation du fichier en BufferedImage
		try {
			Image image = ImageIO.read(new File(fileName));
			BufferedImage buffered = (BufferedImage) image;
			ByteArrayOutputStream baOut= new ByteArrayOutputStream();
			ImageIO.write(buffered, "png", baOut);

			// Envoi de la taille de l'image
			byte[] len = ByteBuffer.allocate(4).putInt(baOut.size()).array();
			out.write(len);
			
			// Envoi de l'image
			out.write(baOut.toByteArray());
			out.flush();
			System.out.println("Image envoyee au serveur. Attente de la reponse...");
		} catch (Exception e) {
			System.out.println("Erreur dans la lecture du fichier.");
			fileName = Transfer.fileNameIn();
		}

		// Attente de reception
		int lenMod = in.readInt();
		System.out.format("Taille de l'image en reception: %s octets. Reception de l'image en cours...\n", lenMod);
		byte[] inputBytes = in.readNBytes(lenMod);
		System.out.println("Image recue.");
		InputStream inp = new ByteArrayInputStream(inputBytes);
		BufferedImage imageConverted = ImageIO.read(inp);
		
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
		} catch (Exception e) {
			System.out.println("Erreur dans l'ecriture du fichier. Sortie...");
			Login.close();
			socket.close();
			System.exit(1);
		}
		
		Login.close();
		socket.close();
	}
}

