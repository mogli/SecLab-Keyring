package org.group02;

import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.bouncycastle.util.encoders.Base64;


public class DesUtil {
	Cipher ecipher;
	Cipher dcipher;
	byte[] salt = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
			(byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03 };

	public DesUtil(String passPhrase) throws Exception {
		int iterationCount = 2;
		KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt,
				iterationCount);
		SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndTripleDES")
				.generateSecret(keySpec);
		ecipher = Cipher.getInstance(key.getAlgorithm());
		dcipher = Cipher.getInstance(key.getAlgorithm());
		AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt,
				iterationCount);
		ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
		dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
	}
	public byte[] encrypt(byte[] str) throws Exception {
		return Base64.encode(ecipher.doFinal(str));
	}

	public byte[] decrypt(byte[] str) throws Exception {
		return dcipher.doFinal(Base64.decode(str));
	}
}
