package johnson.tools;

import static org.junit.Assert.assertArrayEquals;

import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

public class DHUtilTest {

	@Test
	public void test() throws Exception {
		String inputStr = "abc";
		System.out.println("input data = " + inputStr);
		byte[] inputData = inputStr.getBytes("UTF-8");

		Map<String, Object> keyMap1 = DHUtil.initKey();
		Map<String, Object> keyMap2 = DHUtil.initKey(DHUtil.getPublicKey(keyMap1));

		byte[] secretKey1 = DHUtil.getSecretKey(DHUtil.getPublicKey(keyMap2), DHUtil.getPrivatekey(keyMap1));
		byte[] secretKey2 = DHUtil.getSecretKey(DHUtil.getPublicKey(keyMap1), DHUtil.getPrivatekey(keyMap2));
		System.out.println("secretKey1=" + Base64.encodeBase64String(secretKey1));
		System.out.println("secretKey2=" + Base64.encodeBase64String(secretKey2));

		byte[] encryptedData1 = DHUtil.encrypt(inputData, secretKey1);
		byte[] encryptedData2 = DHUtil.encrypt(inputData, secretKey2);
		System.out.println("encryptedData1=" + Base64.encodeBase64String(encryptedData1));
		System.out.println("encryptedData2=" + Base64.encodeBase64String(encryptedData2));
		assertArrayEquals(encryptedData1, encryptedData2);

		byte[] decryptedData1 = DHUtil.decrypt(encryptedData1, secretKey2);
		byte[] decryptedData2 = DHUtil.decrypt(encryptedData2, secretKey1);
		assertArrayEquals(decryptedData1, decryptedData2);
		
		System.out.println("decrypted data = " + new String(decryptedData1, "UTF-8"));
	}
}
