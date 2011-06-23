package org.group02.test;

import org.group02.IKeyring;
import org.group02.Keyring;
import org.junit.Assert;
import org.junit.Test;


public class KeyringEncryptionTest {
	@Test
	public void testClass(){
		IKeyring k = new Keyring();
		
		String user = "willi", password="willis passwort",
			myMessage = "Lorem ipsum.....pp.Alles super geheim heute hier...",
			file = "myFile.txt";
		
		k.create(user, password);
		System.out.println("Encrypted message: ");
		System.out.println(k.encrypt(user, file, myMessage));
		
		System.out.println();System.out.println("Decrypted message:");
		String result = k.decrypt(user, file, password);
		System.out.println(result);
		Assert.assertEquals(myMessage, result);
		
	}
}
