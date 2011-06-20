package firstSteps;
 
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.provider.asymmetric.ec.KeyPairGenerator;
//import org.bouncycastle.jce.provider.asymmetric.ec.KeyPairGenerator;
public class Keyring {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Security.addProvider(new BouncyCastleProvider());
		try {
			Cipher c = Cipher.getInstance("DES/CBC/PKCS5Padding");
			System.out.println(c.toString());
			
			java.security.KeyPairGenerator generator =  KeyPairGenerator.getInstance("RSA");
			generator.initialize(4096);
			KeyPair pair = generator.genKeyPair();
			PrivateKey priv = pair.getPrivate();
			PublicKey publ = pair.getPublic();
			System.out.println(priv.toString());
			System.out.println(publ.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
