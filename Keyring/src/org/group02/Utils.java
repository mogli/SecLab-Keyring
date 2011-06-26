package org.group02;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

import org.bouncycastle.jce.provider.asymmetric.ec.KeyPairGenerator;

public enum Utils {
	INSTANCE;
	public final String keyFolder = "keypairs",
	encAlgo = "RSA";
	public final int keySize = 2048;
	public final byte[] iv = new byte[]{
			(byte)0x8E, 0x12, 0x39, (byte)0x9C,
			0x07, 0x72, 0x6F, 0x5A
	};
	public KeyPair generateKeyPair(String algo, int strength) throws NoSuchAlgorithmException {
		java.security.KeyPairGenerator generator =  KeyPairGenerator.getInstance(algo);
		generator.initialize(strength);
		KeyPair originalPair = generator.genKeyPair();
		return originalPair;
	}
	public void writeByteToFile(String filename, byte[] content) throws Exception{
		FileOutputStream stream = new FileOutputStream(filename);
		stream.write(content);
		stream.close();
	}
	public byte[] readFileToByte(String filename) throws Exception{
		File file = new File(filename);
		byte[] key = new byte[(int)file.length()];
		FileInputStream pubInput;
		pubInput = new FileInputStream(file);
		pubInput.read(key);
		pubInput.close();
		return key;
	}
	
}
