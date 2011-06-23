package test;

import static org.junit.Assert.*;

import org.group02.DesUtil;
import org.junit.Test;


public class DESCodeTest {
	@Test
	public void try1(){
		try {
			DesUtil encryptor = new DesUtil("Hallo Welt");
			byte[] toEncrypt = "Test".getBytes();
			byte[] cypher = encryptor.encrypt(toEncrypt);
			System.out.println(new String(cypher));
			byte[] cmp = encryptor.decrypt(cypher);
			System.out.println(new String(cmp));
			assertArrayEquals(toEncrypt, cmp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
