package client;

public class Validators {
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

    // Valider nom d'utilisateur (entre 4 et 20 caractères)
    public static boolean validateUsername(String input) throws Exception {
		if (input.length() < 4 || input.length() > 20) {
            System.out.println("Erreur dans le nom d'utilisateur. Veuillez reessayer.");
			return false;
		} else {
			return true;
		}
    }

    // Valider le mot de passe (entre 4 et 20 caractères)
    public static boolean validatePassword(String input) throws Exception {
        if (input.length() < 4 || input.length() > 20) {
            System.out.println("Erreur dans le mot de passe. Veuillez reessayer.");
            return false;
        } else {
            return true;
        }
    }
    
    // Valider le format du nom de fichier
    // TODO: ajouter validations pour extensions accepttees
    public static boolean validateFileName(String input) throws Exception {
    	String[] temp = input.split("\\.");
    	if (temp.length != 3) {
    		return false;
    	} else {
    		return true;
    	}
    }
} 