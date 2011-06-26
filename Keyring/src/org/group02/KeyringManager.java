package org.group02;

import java.io.File;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.bouncycastle.util.encoders.Base64;
public class KeyringManager implements IKeyring{
	
	public KeyringManager(){
		createKeyringDirectoryIfnotExistent();
	}

	private void createKeyringDirectoryIfnotExistent() {
		File keyringDirectory = new File(Utils.INSTANCE.keyFolder);
		if(!keyringDirectory.exists()){
			if(keyringDirectory.mkdir()){
				System.out.println("Keyring directory created!");
			}else{
				System.out.println("Could not create keyring directory!");
			}
		}
	}
	
	@Override
	public KeyPair create(String user, String password) {
		KeyPair keypair = null;
		try {
			if(this.keyPairExists(user))
				return new KeyPair(this.getPublicKey(user), this.getPrivateKey(user, password));
			else								// Generate new pair
			{
				keypair = Utils.INSTANCE.generateKeyPair(Utils.INSTANCE.encAlgo, Utils.INSTANCE.keySize);
				Utils.INSTANCE.writeByteToFile(Utils.INSTANCE.keyFolder+"/"+user+".pub", keypair.getPublic().getEncoded());

				byte[] encryptedPrivateKey = new DesUtil(password).encrypt(keypair.getPrivate().getEncoded());
				Utils.INSTANCE.writeByteToFile(Utils.INSTANCE.keyFolder+"/"+user+".priv", encryptedPrivateKey);
				return keypair;
			} 
		}catch (Exception e) {
			e.printStackTrace();
		}
		return keypair;
	}

	@Override
	public String encrypt(String user, String filename, String message) {
		try {
			PublicKey key = this.getPublicKey(user);
			Cipher eCipher = Cipher.getInstance(Utils.INSTANCE.encAlgo);
			eCipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] enc = Base64.encode(eCipher.doFinal(message.getBytes()));
			Utils.INSTANCE.writeByteToFile(filename, enc);
			return new String(Base64.encode(enc));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String decrypt(String user, String filename, String password) {
		try {
			PrivateKey key = this.getPrivateKey(user,password);
			Cipher dCipher = Cipher.getInstance(Utils.INSTANCE.encAlgo);
			dCipher.init(Cipher.DECRYPT_MODE, key);
			byte[] dec = dCipher.doFinal(Base64.decode(Utils.INSTANCE.readFileToByte(filename)));
			return new String(dec);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private boolean keyPairExists(String user){
		if(new File(Utils.INSTANCE.keyFolder+"/"+user+".pub").exists() && new File(Utils.INSTANCE.keyFolder+"/"+user+".priv").exists())
			return true;
		else 
			return false;
	}
	private PublicKey getPublicKey(String user) throws Exception{
		return (PublicKey) getKeyFromArray(Utils.INSTANCE.readFileToByte(Utils.INSTANCE.keyFolder+"/"+user+".pub"),false);
	}
	private PrivateKey getPrivateKey(String user, String password) throws Exception{
		DesUtil desCoder = new DesUtil(password);
		return (PrivateKey) getKeyFromArray(desCoder.decrypt(Utils.INSTANCE.readFileToByte(Utils.INSTANCE.keyFolder+"/"+user+".priv")), true);
	}
	private Key getKeyFromArray(byte[] arr, boolean priv) throws Exception{
		KeyFactory kf = KeyFactory.getInstance(Utils.INSTANCE.encAlgo);
		if(priv) 
			return kf.generatePrivate(new PKCS8EncodedKeySpec(arr));
		else
			return kf.generatePublic(new X509EncodedKeySpec(arr));
	}
}
