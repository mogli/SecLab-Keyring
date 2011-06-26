package org.group02.test;

import org.group02.IKeyring;
import org.group02.KeyringManager;
import org.junit.Assert;
import org.junit.Test;


public class KeyringEncryptionTest {
	private void d(String s){
		System.out.println(s);
	}
	@Test
	public void testClass(){
		IKeyring k = new KeyringManager();
		
		String user = "willi", password="willis passwort",
			myMessage = "Lorem ipsum.....pp.Alles super geheim heute hier...",
			file = "myFile.txt";
		System.out.printf("Starting test with user '%s', password '%s' and message '%s'. The output is written into file '%s'.\n\n", user, password, myMessage, file);
		k.create(user, password);
		d("Encrypted message: ");
		d(k.encrypt(user, file, myMessage));
		
		d("");
		d("Decrypted message:");
		String result = k.decrypt(user, file, password);
		d(result);
		Assert.assertEquals(myMessage, result);
		
	}
}
