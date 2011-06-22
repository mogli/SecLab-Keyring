package firstSteps;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public enum DESEncryptor {
	INSTANCE;
	private  final byte[] SALT = {
        (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
        (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
    };
	private final String encodingAlgo = "DES/CBC/PKCS5Padding";
	public byte[] encryptKey(PrivateKey private1, String password) {
//		AlgorithmParameterSpec paramSpec = new IvParameterSpec(Variables.INSTANCE.iv);
		byte [] result = null;
		
		try {
	        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(encodingAlgo);
	        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(password.toCharArray()));
	        
			//SecretKey c = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(password.getBytes()));
			Cipher ecipher = Cipher.getInstance(encodingAlgo);
			ecipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
			result = ecipher.doFinal(private1.getEncoded());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public PrivateKey decryptKey(byte[] private1, String password) {
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(Variables.INSTANCE.iv);
		PrivateKey result = null;
		try {

			KeyFactory kf = KeyFactory.getInstance("RSA");
			

//			SecretKey c = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(password.getBytes()));
			
			
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(encodingAlgo);
	        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(password.toCharArray()));
			Cipher ecipher = Cipher.getInstance(encodingAlgo);
			ecipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
			PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(ecipher.doFinal(ecipher.doFinal(private1)));

			result = kf.generatePrivate(ks);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
