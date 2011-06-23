package org.group02;

import java.security.KeyPair;

public interface IKeyring {
	public KeyPair create(String user, String password);
	public String encrypt(String user, String filename, String message);
	public String decrypt(String user, String filename, String password);
}
