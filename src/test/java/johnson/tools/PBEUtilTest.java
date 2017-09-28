package johnson.tools;

import static org.junit.Assert.assertEquals;

import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

public class PBEUtilTest {

	@Test
	public void test() throws Exception {
		String password = "mypassword";
		String inputStr = "Johnson";

		System.out.println("plain text: " + inputStr);
		byte[] inputData = inputStr.getBytes("UTF-8");

		byte[] salt = PBEUtil.initSalt();
		byte[] encryptedData = PBEUtil.encrypt(inputData, password, salt);
		System.out.println("encrypted data: " + Hex.encodeHexString(encryptedData));

		byte[] decryptedData = PBEUtil.decrypt(encryptedData, password, salt);
		String decryptedStr = new String(decryptedData, "UTF-8");
		System.out.println("decrypted text: " + decryptedStr);
		System.out.println();

		assertEquals(inputStr, decryptedStr);
	}
}
