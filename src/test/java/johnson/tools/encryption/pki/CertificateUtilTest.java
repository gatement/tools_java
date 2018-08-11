package johnson.tools.encryption.pki;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

public class CertificateUtilTest {
	private final String keyStorePath = "./certs/CertificateUtilTest/key.keystore";
	private final String keyStoreAlias = "1";
	private final String keyStorePassword = "123456";
	private final String certPath= "./certs/CertificateUtilTest/cert.pem"; // or "cert.cer"

	@Test
	public void testPrivateKeyEncryptPublicKeyDecrypt() throws Exception {
		String inputStr = "Johnson";
		byte[] inputData = inputStr.getBytes("UTF-8");

		byte[] encryptedData = CertificateUtil.encryptByPrivateKey(inputData, keyStorePath, keyStoreAlias, keyStorePassword);

		byte[] decryptedData1 = CertificateUtil.decryptByPublicKey(encryptedData, certPath);
		byte[] decryptedData2 = CertificateUtil.decryptByPublicKey(encryptedData, keyStorePath, keyStoreAlias, keyStorePassword);

		assertArrayEquals(inputData, decryptedData1);
		assertArrayEquals(inputData, decryptedData2);
	}

	@Test
	public void testPublicKeyEncryptPrivateKeyDecrypt() throws Exception {
		String inputStr = "Johnson";
		byte[] inputData = inputStr.getBytes("UTF-8");

		byte[] encryptedData1 = CertificateUtil.encryptByPublicKey(inputData, keyStorePath, keyStoreAlias, keyStorePassword);
		byte[] encryptedData2 = CertificateUtil.encryptByPublicKey(inputData, certPath);

		byte[] decryptedData1 = CertificateUtil.decryptByPrivateKey(encryptedData1, keyStorePath, keyStoreAlias, keyStorePassword);
		byte[] decryptedData2 = CertificateUtil.decryptByPrivateKey(encryptedData2, keyStorePath, keyStoreAlias, keyStorePassword);
		assertArrayEquals(inputData, decryptedData1);
		assertArrayEquals(inputData, decryptedData2);
	}

	@Test
	public void testSignature() throws Exception {
		String inputStr = "Johnson";
		byte[] inputData = inputStr.getBytes("UTF-8");

		byte[] sign = CertificateUtil.sign(inputData, keyStorePath, keyStoreAlias, keyStorePassword);
		boolean verified = CertificateUtil.verify(inputData, sign, certPath);
		//System.out.println("signature: " + Base64.encodeBase64String(sign));

		assertTrue(verified);
	}
}
