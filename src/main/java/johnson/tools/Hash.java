package johnson.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {
	public static byte[] md5(byte[] input) throws NoSuchAlgorithmException {
		return hash("MD5", input);
	}

	public static byte[] sha(byte[] input) throws NoSuchAlgorithmException {
		return hash("SHA", input);
	}

	private static byte[] hash(String algorithm, byte[] input) throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
		messageDigest.update(input);
		return messageDigest.digest();
	}
}
