package client;

public class Client {
	public static void main(String[] args) throws Exception
	{
		System.out.println("Bienvenue dans l'application PolySobel - Client! (Copyright Derek Bernard & Jean-Olivier Dalphond 2020)");
		
		while (!Login.startConnectionRoutine()) {
			Login.startConnectionRoutine();
		}
		
		while (!Login.startUserRoutine()) {
			Login.startUserRoutine();
		}

		while (!Transfer.startRoutine()) {
			Transfer.startRoutine();
		}
		while (!Transfer.startPostRoutine()) {
			Transfer.startPostRoutine();
		}
		
		Login.close();
		Transfer.close();
		System.out.println("Merci d'avoir utilise l'application PolySobel - Client. A la prochaine!");
		System.exit(0);
	}
}

