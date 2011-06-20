package firstSteps;
 
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.provider.asymmetric.ec.KeyPairGenerator;
public class Keyring {//extends HashMap<String, KeyPair>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2401600415393128183L;

	public KeyPair get(String key, String password) {
			return loadKeyToRing(key, password);
	}


	public void put(String user, KeyPair keypair, String password) {
		saveKeyToFile(user, keypair, password);
	}


	private void saveKeyToFile(String user, KeyPair keypair, String password) {
		try {
			FileOutputStream pubStream = new FileOutputStream(Variables.INSTANCE.keyFolder+"/"+user+".pub");
			byte[] pubKey = keypair.getPublic().getEncoded();
			pubStream.write(pubKey);
			pubStream.close();
			
			byte[] encryptedPrivateKey = DESEncryptor.INSTANCE.encryptKey(keypair.getPrivate(), password);
			FileOutputStream privStream = new FileOutputStream(Variables.INSTANCE.keyFolder+"/"+user+".priv");
			privStream.write(encryptedPrivateKey);
			privStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private KeyPair loadKeyToRing(String key, String password) {
		return null;
	}



	public static void main(String[] args) {
		Security.addProvider(new BouncyCastleProvider());
		try {
			Cipher c = Cipher.getInstance("DES/CBC/PKCS5Padding");
			
			System.out.println(c.toString());
			
			java.security.KeyPairGenerator generator =  KeyPairGenerator.getInstance("RSA");
			generator.initialize(4096);
			KeyPair pair = generator.genKeyPair();
			Keyring k = new Keyring();
			k.put("martin", pair, "hallo");
			PrivateKey priv = pair.getPrivate();
			PublicKey publ = pair.getPublic();
			System.out.println(priv.toString());
			System.out.println(publ.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
