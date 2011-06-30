package org.group02;

import java.security.KeyPair;

public class Keyring {
	public static void main(String[] args) {
		IKeyring keyRing = new KeyringManager();

		if (args.length == 0) {
			System.out.println("No arguments supplied.");
			printUsage();
		} else {
			if (args[0].equals("create") && args.length == 3) {
				String user = args[1];
				String password = args[2];
				KeyPair keyPair = keyRing.create(user, password);
				if (keyPair != null) {
					System.out.println("Key pair created for user " + user + "!");
				}
			} else if (args[0].equals("encrypt") && args.length == 4) {
				String user = args[1];
				String filename = args[2];
				String message = args[3];
				System.out.println("Original message: " + message);
				System.out.println("Encrypted message: " + keyRing.encrypt(user, filename, message));
			} else if (args[0].equals("decrypt") && args.length == 4) {
				String user = args[1];
				String filename = args[2];
				String password = args[3];
				System.out.println("Decrypted Message: " + keyRing.decrypt(user, filename, password));
			} else {
				System.out.println("Invalid parameter sequence.");
				printUsage();
			}
		}
	}

	private static void printUsage() {
		System.out.println("Usage:");
		System.out.println("keyring create <user> <password> - Creates a new key pair for <user> using <password> to encrypt private key");
		System.out.println("keyring encrypt <user> <filename> <message> - Encrypts <message> for <user> into file <filename>");
		System.out.println("keyring decrypt <user> <filename> <password> - Decrypts the encrypted message in <filename> using <user>'s private key that is protected by <password>");
	}

}
