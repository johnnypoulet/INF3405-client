package client;

public class Client {
	public static void main(String[] args) throws Exception
	{
		System.out.println("Bienvenue dans l'application PolySobel - Client! (Copyright Derek Bernard & Jean-Olivier Dalphond 2020)");

		while (!Login.startSuccessful) {
			Login.startConnectionRoutine();
		}

		while (!Transfer.startSuccessful) {
			Transfer.startRoutine();
		}
		while (!Transfer.startPostSuccesful) {
			Transfer.startPostRoutine();
		}
		
		Login.close();
		Transfer.close();
		System.out.println("Merci d'avoir utilise l'application PolySobel - Client. A la prochaine!");
		System.exit(0);
	}
}

