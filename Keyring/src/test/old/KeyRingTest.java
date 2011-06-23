package test.old;

import static org.junit.Assert.assertArrayEquals;

import java.security.KeyPair;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.group02.Keyring;
import org.junit.Test;


public class KeyRingTest {
	@Test
	public void testKeyCreation() {
		Security.addProvider(new BouncyCastleProvider());
		try {
			Keyring k = new Keyring();
			KeyPair originalPair = k.create("martin", "hallo");
			KeyPair readPair = k.create("martin", "hallo");
			
			System.out.println(originalPair.getPrivate().toString());
			System.out.println(readPair.getPrivate().toString());
			
			assertArrayEquals(originalPair.getPrivate().getEncoded(), readPair.getPrivate().getEncoded());
			assertArrayEquals(originalPair.getPublic().getEncoded(), readPair.getPublic().getEncoded());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
