package johnson.tools.encryption.asymmetry;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import johnson.tools.Util;

public class RSAUtilTest {

	@Test
	public void testEncryption() throws Exception {
		String inputStr = "Johnson";
		byte[] inputData = inputStr.getBytes("UTF-8");
		
		Map<String, Object> keyMap = RSAUtil.initKey();
		byte[] privateKey = RSAUtil.getPrivatekey(keyMap);
		byte[] publicKey = RSAUtil.getPublickey(keyMap);
		//System.out.println("private key = " + Base64.encodeBase64String(privateKey));
		//System.out.println("public key = " + Base64.encodeBase64String(publicKey));
		
		byte[] privateKeyEncrypted = RSAUtil.encryptByPrivateKey(inputData, privateKey);
		byte[] publicKeyDecrypted = RSAUtil.decryptByPublicKey(privateKeyEncrypted, publicKey);
		assertArrayEquals(inputData, publicKeyDecrypted);
		
		byte[] publicKeyEncrypted = RSAUtil.encryptByPublicKey(inputData, publicKey);
		byte[] privateKeyDecrypted = RSAUtil.decryptByPrivateKey(publicKeyEncrypted, privateKey);
		assertArrayEquals(inputData, privateKeyDecrypted);
	}

	@Test
	public void testSignature() throws Exception {
		String inputStr = "Johnson";
		System.out.println("text to signature = " + inputStr);
		byte[] inputData = inputStr.getBytes("UTF-8");
		Util.printBytes(inputData);
		
		Map<String, Object> keyMap = RSAUtil.initKey();
		byte[] privateKey = RSAUtil.getPrivatekey(keyMap);
		byte[] publicKey = RSAUtil.getPublickey(keyMap);

		byte[] sign = RSAUtil.sign(inputData, privateKey);
		System.out.println("RSA signature = " + Base64.encodeBase64String(sign));
		Util.printBytes(sign);
		
		boolean verification = RSAUtil.verify(inputData, publicKey, sign);
		assertTrue(verification);
	}
}
