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
	public static boolean validatePortNumber(String input) throws Exception {
		try {
			int temp = Integer.parseInt(input);
			if (!(temp < 5000 || temp > 5050)) {
				return true;
			} else {
				System.out.println("Erreur dans le numero de port. Veuillez reessayer.");
				return false;
			}
		} catch (NumberFormatException e) {
			System.out.println("Erreur dans le numero de port. Veuillez reessayer.");
			return false;
		}
    }

    // Valider nom d'utilisateur (entre 4 et 20 caract??res)
    public static boolean validateUsername(String username) throws Exception {
		if (username.length() < 4 || username.length() > 20) {
            System.out.println("Erreur dans le nom d'utilisateur. Veuillez reessayer.");
			return false;
		} else {
			return true;
		}
    }

    // Valider le mot de passe (entre 4 et 20 caract??res)
    public static boolean validatePassword(String password) throws Exception {
        if (password.length() < 4 || password.length() > 20) {
            System.out.println("Erreur dans le mot de passe. Veuillez reessayer.");
            return false;
        } else {
            return true;
        }
    }
    
    // Valider le format du nom de fichier
    public static boolean validateFileName(String input) throws Exception {
    	// On doit d'abord verifier s'il y a un path relatif ou des caracteres non permis
    	if (input.contains("/") || input.contains("\\") || input.contains(" ") || input.contains("\\.") || input.contains("<") ||
    			input.contains(">") || input.contains(":") || input.contains("|") || input.contains("?") || input.contains("*")) {
    		return false;
    	}
    	// On verifie si le nom correspond aux formats supportes
    	String[] temp = input.split("\\.");
    	if (temp.length == 2 && (temp[1].equalsIgnoreCase("png") || temp[1].equalsIgnoreCase("jpg") || temp[1].equalsIgnoreCase("bmp"))) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public static boolean validateFileNameToSave(String input) throws Exception {
    	// On doit d'abord verifier s'il y a un path relatif ou des caracteres non permis
    	if (input.contains("/") || input.contains("\\") || input.contains(" ") || input.contains("\\.") || input.contains("<") ||
    			input.contains(">") || input.contains(":") || input.contains("|") || input.contains("?") || input.contains("*")) {
    		return false;
    	}
    	return true;
    }
    
    // Renvoie 0 si la reponse est invalide, 1 si Oui, 2 si Non
    public static int validateResponse(String input, String message) throws Exception {
		if (input.equalsIgnoreCase("oui")) {
			return 1;
		} else if (input.equalsIgnoreCase("non")) {
			return 2;
		} else {
			System.out.println(message);
			return 0;
		}
    }
} 