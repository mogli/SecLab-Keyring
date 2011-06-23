package firstSteps;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

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

			DesUtil desCoder = new DesUtil(password);
			
			byte[] encryptedPrivateKey = desCoder.encrypt(keypair.getPrivate().getEncoded());
			FileOutputStream privStream = new FileOutputStream(Variables.INSTANCE.keyFolder+"/"+user+".priv");
			privStream.write(encryptedPrivateKey);
			privStream.close();
		} catch (Exception e) {
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
	private Key getKeyFromArray(byte[] arr, boolean priv) throws Exception{
			KeyFactory kf = KeyFactory.getInstance("RSA");
			if(priv) 
				return kf.generatePrivate(new PKCS8EncodedKeySpec(arr));
			else
				return kf.generatePublic(new X509EncodedKeySpec(arr));
	}
	private KeyPair loadKeyToRing(String user, String password) {
		KeyPair keyPair = null;
		try{
			DesUtil desCoder = new DesUtil(password);
			byte[] pubKeyArr = readFileToByte(Variables.INSTANCE.keyFolder+"/"+user+".pub");
			byte[] privKeyArr = desCoder.decrypt(readFileToByte(Variables.INSTANCE.keyFolder+"/"+user+".priv"));
			//PrivateKey privKey = DESEncryptor.INSTANCE.decryptKey(readFileToByte(Variables.INSTANCE.keyFolder+"/"+user+".priv"),password);
			//			KeyFactory kf = KeyFactory.getInstance("RSA");
			//			 PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(pubKeyArr);
			keyPair = new KeyPair((PublicKey) getKeyFromArray(pubKeyArr,false), (PrivateKey) getKeyFromArray(privKeyArr, true));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return keyPair;
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
