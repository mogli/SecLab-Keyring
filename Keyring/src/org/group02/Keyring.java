package org.group02;

public class Keyring {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		IKeyring keyRing = new KeyringManager();

		if (args.length == 0) {
			System.out.println("No arguments supplied. Please use \"keyring help\" for usage information.");
		} else {

			if (args[0].equals("create") && args.length == 3) {
				String user = args[1];
				String password = args[2];
				keyRing.create(user, password);
			} else if (args[0] == "encrypt" && args.length == 4) {
				String user = args[1];
				String filename = args[2];
				String message = args[3];
				System.out.println("Encrypted Message: " + keyRing.encrypt(user, filename, message));
			} else if (args[0] == "decrypt" && args.length == 4) {
				String user = args[1];
				String filename = args[2];
				String password = args[3];
				System.out.println("Decrypted Message: " + keyRing.decrypt(user, filename, password));
			} else {
				System.out.println("Invalid parameter sequence.");
				System.out.println("Usage:");
				System.out.println("keyring create <user> <password> - Creates a new key pair for <user> using <password> to encrypt private key");
				System.out.println("keyring encrypt <user> <filename> <message> - Encrypts <message> for <user> into file <filename>");
				System.out.println("keyring decrypt <user> <filename> <password> - Decrypts the encrypted message in <filename> using <user>'s private key that is protected by <password>");
			}
		}
	}

}
