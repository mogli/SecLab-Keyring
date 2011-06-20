package firstSteps;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

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

	private byte[] readFileToByte(String filename){
		File file = new File(filename);
		byte[] key = new byte[(int)file.length()];
		FileInputStream pubInput;
		try {
			pubInput = new FileInputStream(file);
			pubInput.read(key);
			pubInput.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return key;
	}
	private KeyPair loadKeyToRing(String user, String password) {
		byte[] pubKeyArr = readFileToByte(Variables.INSTANCE.keyFolder+"/"+user+".pub");
		PrivateKey privKey = DESEncryptor.INSTANCE.decryptKey(readFileToByte(Variables.INSTANCE.keyFolder+"/"+user+".priv"),password);
		KeyPair bla = null;
		try {
			KeyFactory kf = KeyFactory.getInstance("RSA");

			PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(pubKeyArr);

			bla = new KeyPair(kf.generatePublic(ks), privKey);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
		return bla;
	}



	public static void main(String[] args) {
		try {

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
