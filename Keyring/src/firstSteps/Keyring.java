package firstSteps;
 
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.provider.asymmetric.ec.KeyPairGenerator;
import org.bouncycastle.jce.provider.symmetric.AES.KeyGen;
public class Keyring extends HashMap<String, KeyPair>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2401600415393128183L;

	public KeyPair get(String key, String password) {
		if(!super.containsKey(key))
			loadKeyToRing(key, password);
		return super.get(key);
	}


	public KeyPair put(String user, KeyPair keypair, String password) {
		saveKeyToFile(user, keypair, password);
		return super.put(user, keypair);
	}


	private void saveKeyToFile(String user, KeyPair keypair, String password) {
		try {
			FileOutputStream pubStream = new FileOutputStream(Variables.INSTANCE.keyFolder+"/"+user+".pub");
			byte[] pubKey = keypair.getPublic().getEncoded();
			pubStream.write(pubKey);
			pubStream.close();
			
			byte[] encryptedPrivateKey = encryptKey(keypair.getPrivate(), password);
			FileOutputStream privStream = new FileOutputStream(Variables.INSTANCE.keyFolder+"/"+user+".pub");
			privStream.write(encryptedPrivateKey);
			privStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	
	private byte[] encryptKey(PrivateKey private1, String password) {
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(Variables.INSTANCE.iv);
		
		try {
			SecretKey c = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(private1.getEncoded()));
			Cipher ecipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			ecipher.init(Cipher.ENCRYPT_MODE, private1, paramSpec);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}


	private void loadKeyToRing(String key, String password) {
		
	}



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
