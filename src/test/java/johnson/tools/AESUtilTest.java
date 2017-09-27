package johnson.tools;

import static org.junit.Assert.assertEquals;

import java.util.Base64;

import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

public class AESUtilTest {

	@Test
	public void testAES128NoPadding() throws Exception {
		String inputStr = "testAES128PKCS5_";
		System.out.println("plain text: " + inputStr);
		byte[] inputData = inputStr.getBytes("UTF-8");

		byte[] key = AESUtil.initKey(128);
		//System.out.println("secret key: " + Base64.getEncoder().encodeToString(key));
		System.out.println("secret key: " + Hex.encodeHexString(key));

		byte[] encryptedData = AESUtil.encryptNoPadding(inputData, key);
		//System.out.println("encrypted data: " + Base64.getEncoder().encodeToString(encryptedData));
		System.out.println("encrypted data: " + Hex.encodeHexString(encryptedData));

		byte[] decryptedData = AESUtil.decryptNoPadding(encryptedData, key);
		String decryptedStr = new String(decryptedData, "UTF-8");
		System.out.println("decrypted text: " + decryptedStr);
		System.out.println();

		assertEquals(inputStr, decryptedStr);
	}

	@Test
	public void testAES128PKCS5() throws Exception {
		String inputStr = "testAES128PKCS5";
		System.out.println("plain text: " + inputStr);
		byte[] inputData = inputStr.getBytes("UTF-8");

		byte[] key = AESUtil.initKey(128);
		//System.out.println("secret key: " + Base64.getEncoder().encodeToString(key));
		System.out.println("secret key: " + Hex.encodeHexString(key));

		byte[] encryptedData = AESUtil.encryptPKCS5(inputData, key);
		//System.out.println("encrypted data: " + Base64.getEncoder().encodeToString(encryptedData));
		System.out.println("encrypted data: " + Hex.encodeHexString(encryptedData));

		byte[] decryptedData = AESUtil.decryptPKCS5(encryptedData, key);
		String decryptedStr = new String(decryptedData, "UTF-8");
		System.out.println("decrypted text: " + decryptedStr);
		System.out.println();

		assertEquals(inputStr, decryptedStr);
	}

	@Test
	public void testAES128PKCS7() throws Exception {
		String inputStr = "testAES128PKCS";
		System.out.println("plain text: " + inputStr);
		byte[] inputData = inputStr.getBytes("UTF-8");

		byte[] key = AESUtil.initKey(128);
		//System.out.println("secret key: " + Base64.getEncoder().encodeToString(key));
		System.out.println("secret key: " + Hex.encodeHexString(key));

		byte[] encryptedData = AESUtil.encryptPKCS7(inputData, key);
		//System.out.println("encrypted data: " + Base64.getEncoder().encodeToString(encryptedData));
		System.out.println("encrypted data: " + Hex.encodeHexString(encryptedData));

		byte[] decryptedData = AESUtil.decryptPKCS7(encryptedData, key);
		String decryptedStr = new String(decryptedData, "UTF-8");
		System.out.println("decrypted text: " + decryptedStr);
		System.out.println();

		assertEquals(inputStr, decryptedStr);
	}
}
