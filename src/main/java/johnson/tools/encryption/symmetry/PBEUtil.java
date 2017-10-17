package johnson.tools.encryption.symmetry;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class PBEUtil {
	private static final String ALOGRITHM = "PBEWITHMD5andDES";
	private static final int ITERATION_COUNT = 100;

	public static byte[] initSalt() throws Exception {
		SecureRandom random = new SecureRandom();
		return random.generateSeed(8);
	}

	public static byte[] encrypt(byte[] data, String password, byte[] salt) throws Exception {
		Key key = toKey(password);
		PBEParameterSpec paramSpec = new PBEParameterSpec(salt, ITERATION_COUNT);
		Cipher cipher = Cipher.getInstance(ALOGRITHM);
		cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
		return cipher.doFinal(data);
	}

	public static byte[] decrypt(byte[] data, String password, byte[] salt) throws Exception {
		Key key = toKey(password);
		PBEParameterSpec paramSpec = new PBEParameterSpec(salt, ITERATION_COUNT);
		Cipher cipher = Cipher.getInstance(ALOGRITHM);
		cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
		return cipher.doFinal(data);
	}

	private static Key toKey(String password) throws Exception {
		PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALOGRITHM);
		SecretKey secretKey = keyFactory.generateSecret(keySpec);
		return secretKey;
	}
}