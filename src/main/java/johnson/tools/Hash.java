package johnson.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;
import java.util.zip.Adler32;

public class Hash {
	public static String crc32(byte[] input) {
		CRC32 crc32 = new CRC32();
		crc32.update(input);
		return Long.toHexString(crc32.getValue());
	}

	public static String adler32(byte[] input) {
		Adler32 adler32 = new Adler32();
		adler32.update(input);
		return Long.toHexString(adler32.getValue());
	}

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
