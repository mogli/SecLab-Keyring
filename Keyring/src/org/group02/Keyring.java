package org.group02;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.bouncycastle.util.encoders.Base64;
public class Keyring implements IKeyring{//extends HashMap<String, KeyPair>{

	private boolean keyPairExists(String user){
		if(new File(Utils.INSTANCE.keyFolder+"/"+user+".pub").exists() && new File(Utils.INSTANCE.keyFolder+"/"+user+".priv").exists())
			return true;
		else 
			return false;
	}
	@Override
	public KeyPair create(String user, String password) {
		KeyPair keypair = null;
		if(this.keyPairExists(user))
			return this.get(user, password);
		else
		try {
			keypair = Utils.INSTANCE.generateKeyPair("RSA", 2048);
			FileOutputStream pubStream = new FileOutputStream(Utils.INSTANCE.keyFolder+"/"+user+".pub");
			byte[] pubKey = keypair.getPublic().getEncoded();
			pubStream.write(pubKey);
			pubStream.close();

			DesUtil desCoder = new DesUtil(password);

			byte[] encryptedPrivateKey = desCoder.encrypt(keypair.getPrivate().getEncoded());
			FileOutputStream privStream = new FileOutputStream(Utils.INSTANCE.keyFolder+"/"+user+".priv");
			privStream.write(encryptedPrivateKey);
			privStream.close();
			return keypair;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return keypair;
	}


	@Override
	public String encrypt(String user, String filename, String message) {
		try {
			PublicKey key = this.getPublicKey(user);
			Cipher eCipher = Cipher.getInstance("RSA");
			eCipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] enc = Base64.encode(eCipher.doFinal(message.getBytes()));
			this.writeByteToFile(filename, enc);
			return new String(Base64.encode(enc));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private PublicKey getPublicKey(String user) throws Exception{
		byte[] pubKeyArr = readFileToByte(Utils.INSTANCE.keyFolder+"/"+user+".pub");
		return (PublicKey) getKeyFromArray(pubKeyArr,false);
	}

	@Override
	public String decrypt(String user, String filename, String password) {
		try {
			PrivateKey key = this.getPrivateKey(user,password);
			Cipher dCipher = Cipher.getInstance("RSA");
			dCipher.init(Cipher.DECRYPT_MODE, key);
			byte[] dec = dCipher.doFinal(Base64.decode(this.readFileToByte(filename)));
			return new String(dec);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private PrivateKey getPrivateKey(String user, String password) throws Exception{
		DesUtil desCoder = new DesUtil(password);
		byte[] privKeyArr = readFileToByte(Utils.INSTANCE.keyFolder+"/"+user+".priv");
		return (PrivateKey) getKeyFromArray(desCoder.decrypt(privKeyArr), true);
	}


	public KeyPair get(String user, String password) {
		return loadKeyToRing(user, password);
	}


	private void writeByteToFile(String filename, byte[] content) throws Exception{
		FileOutputStream stream = new FileOutputStream(filename);
		stream.write(content);
		stream.close();
	}
	private byte[] readFileToByte(String filename) throws Exception{
		File file = new File(filename);
		byte[] key = new byte[(int)file.length()];
		FileInputStream pubInput;
		pubInput = new FileInputStream(file);
		pubInput.read(key);
		pubInput.close();
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
			byte[] pubKeyArr = readFileToByte(Utils.INSTANCE.keyFolder+"/"+user+".pub");
			byte[] privKeyArr = desCoder.decrypt(readFileToByte(Utils.INSTANCE.keyFolder+"/"+user+".priv"));
			keyPair = new KeyPair((PublicKey) getKeyFromArray(pubKeyArr,false), (PrivateKey) getKeyFromArray(privKeyArr, true));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return keyPair;
	}
}
