package org.group02;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

import org.bouncycastle.jce.provider.asymmetric.ec.KeyPairGenerator;

public enum Utils {
	INSTANCE;
	public final String keyFolder = "keypairs";
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
}
