package client;

public class Client {
	public static void main(String[] args) throws Exception
	{
		System.out.println("Bienvenue dans l'application PolySobel - Client! (Copyright Derek Bernard & Jean-Olivier Dalphond 2020)");

		try {
			while (!Login.startSuccessful) {
				Login.startConnectionRoutine();
			}

			while (!Transfer.startSuccessful) {
				Transfer.startRoutine();
			}
			while (!Transfer.startPostSuccesful) {
				Transfer.startPostRoutine();
			}
		} catch (Exception e) {
			System.out.println("Exception levee lors de la connexion. Le serveur est-il encore en ligne? Sortie de l'application...");
			Login.close();
			Transfer.close();
			System.exit(0);
		} finally {
			Login.close();
			Transfer.close();
			System.out.println("Merci d'avoir utilise l'application PolySobel - Client. A la prochaine!");
		}
	}
}

