package test;

import static org.junit.Assert.*;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.provider.asymmetric.ec.KeyPairGenerator;
import org.junit.Test;

import firstSteps.Keyring;

public class KeyRingTest {

	@Test
	public void testFunction() {
		Security.addProvider(new BouncyCastleProvider());
		try {
			java.security.KeyPairGenerator generator =  KeyPairGenerator.getInstance("RSA");
			generator.initialize(512);
			KeyPair originalPair = generator.genKeyPair();
			Keyring k = new Keyring();
			k.put("martin", originalPair, "hallo");
			PrivateKey priv = originalPair.getPrivate();
			PublicKey publ = originalPair.getPublic();
			System.out.println(priv.toString());
			System.out.println(publ.toString());
			
			KeyPair readPair = k.get("martin", "hallo");
			assertArrayEquals(originalPair.getPrivate().getEncoded(), readPair.getPrivate().getEncoded());
			assertArrayEquals(originalPair.getPublic().getEncoded(), readPair.getPublic().getEncoded());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
