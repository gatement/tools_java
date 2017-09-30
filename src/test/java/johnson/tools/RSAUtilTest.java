package johnson.tools;

import static org.junit.Assert.assertArrayEquals;

import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

public class RSAUtilTest {

	@Test
	public void test() throws Exception {
		String inputStr = "Johnson";
		byte[] inputData = inputStr.getBytes("UTF-8");
		
		Map<String, Object> keyMap = RSAUtil.initKey();
		byte[] privateKey = RSAUtil.getPrivatekey(keyMap);
		byte[] publicKey = RSAUtil.getPublickey(keyMap);
		System.out.println("private key = " + Base64.encodeBase64String(privateKey));
		System.out.println("public key = " + Base64.encodeBase64String(publicKey));
		
		byte[] privateKeyEncrypted = RSAUtil.encryptByPrivateKey(inputData, privateKey);
		byte[] publicKeyDecrypted = RSAUtil.decryptByPublicKey(privateKeyEncrypted, publicKey);
		assertArrayEquals(inputData, publicKeyDecrypted);
		
		byte[] publicKeyEncrypted = RSAUtil.encryptByPublicKey(inputData, publicKey);
		byte[] privateKeyDecrypted = RSAUtil.decryptByPrivateKey(publicKeyEncrypted, privateKey);
		assertArrayEquals(inputData, privateKeyDecrypted);
	}
}
