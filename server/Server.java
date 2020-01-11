package server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
	private static ServerSocket listener;

	public static void main(String[] args) throws Exception {
		int clientNumber = 0;
		int serverPort = 0;
		String serverAddress = "";
		
		Scanner keyboard = new Scanner(System.in);
		
		// Entrez l'adresse IP
		System.out.println("Entrez l'adresse IP du poste:");
		String serverAddressIn = keyboard.next();
		String[] tempAddr = serverAddressIn.split("\\.");
		
		while (!Validators.validateIPAddress(tempAddr)) {
			System.out.println("Entrez l'adresse IP du poste:");
			serverAddressIn = keyboard.next();
			tempAddr = serverAddressIn.split("\\.");	
		}
		// L'adresse est valide
		serverAddress = serverAddressIn;
		
		// Entrez le numero de port
		System.out.println("Entrez le numero du port (entre 5000 et 5050):");
		int serverPortIn = keyboard.nextInt();
		
		while (!Validators.validatePortNumber(serverPortIn)) {
			System.out.println("Entrez le numero du port (entre 5000 et 5050):");
			serverPortIn = keyboard.nextInt();
		}
		// Le numero de port est valide
		serverPort = serverPortIn;
		
		System.out.println("Entrez votre nom d'utilisateur:");
		String usernameIn = keyboard.next();
		if (Validators.validateUsername(usernameIn)) {
			System.out.println("Rebienvenue %s! Entrez votre mot de passe:" + usernameIn);
			if (Validators.validatePassword(usernameIn, keyboard.next())) {
				System.out.println("Bienvenue dans PolySobel! Veuillez entrer le nom de l'image que vous voulez convertir:");
				// Sobel.process(keyboard.next());
			}
		}
		
		keyboard.close();

		listener = new ServerSocket();
		listener.setReuseAddress(true);
		InetAddress serverIP = InetAddress.getByName(serverAddress);
		
		listener.bind(new InetSocketAddress(serverIP, serverPort));
		
		System.out.format("Le serveur fonctionne sur %s:%d%n", serverAddress, serverPort);
		
		try
		{
			while (true)
			{
				new ClientHandler(listener.accept(), clientNumber++).start();
			}
		}
		finally
		{
			listener.close();
		}
	}
	
	private static class ClientHandler extends Thread
	{
		private Socket socket;
		private int clientNumber;
		
		public ClientHandler(Socket socket, int clientNumber)
		{
			this.socket = socket;
			this.clientNumber = clientNumber;
			System.out.println("New connection with client#" + clientNumber + " at " + socket);
		}
		
		public void run()
		{
			try
			{
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				
				out.writeUTF("Hello from server - you are client#" + clientNumber);
			} catch (IOException e)
			{
				System.out.println("Error handling client# " + clientNumber + ": " + e);
			}
			finally
			{
				try
				{
					socket.close();
				}
				catch (IOException e)
				{
					System.out.println("Couldn't close a socket, what's going on?");
				}
				System.out.println("Connection with client# " + clientNumber + " closed");
			}
		}
	}
}
