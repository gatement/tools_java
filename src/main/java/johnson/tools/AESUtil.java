package johnson.tools;

import java.security.Key;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class AESUtil {
	public static final String KEY_ALGORITHM = "AES";
	public static final String CIPHER_ALGORITHM_ECB_PKCS5 = "AES/ECB/PKCS5Padding";
	public static final String CIPHER_ALGORITHM_ECB_PKCS7 = "AES/ECB/PKCS7Padding";

	public static byte[] initKey(int keySize) throws Exception {
		KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
		kg.init(keySize);
		SecretKey secretKey = kg.generateKey();
		return secretKey.getEncoded();
	}

	public static Key toKey(byte[] key) throws Exception {
		SecretKey secretKey = new SecretKeySpec(key, KEY_ALGORITHM);
		return secretKey;
	}

	public static byte[] encryptPKCS5(byte[] data, byte[] key) throws Exception {
		Key k = toKey(key);
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_ECB_PKCS5);
		cipher.init(Cipher.ENCRYPT_MODE, k);
		return cipher.doFinal(data);
	}

	public static byte[] decryptPKCS5(byte[] data, byte[] key) throws Exception {
		Key k = toKey(key);
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_ECB_PKCS5);
		cipher.init(Cipher.DECRYPT_MODE, k);
		return cipher.doFinal(data);
	}

	public static byte[] encryptPKCS7(byte[] data, byte[] key) throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		Key k = toKey(key);
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_ECB_PKCS7);
		cipher.init(Cipher.ENCRYPT_MODE, k);
		return cipher.doFinal(data);
	}

	public static byte[] decryptPKCS7(byte[] data, byte[] key) throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		Key k = toKey(key);
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_ECB_PKCS7);
		cipher.init(Cipher.DECRYPT_MODE, k);
		return cipher.doFinal(data);
	}
}