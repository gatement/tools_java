package johnson.tools;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

public class CertificateCodecTest {

	@Test
	public void testPrivateKeyEncryptPublicKeyDecrypt() throws Exception {
		String inputStr = "Johnson";
		byte[] inputData = inputStr.getBytes("UTF-8");

		byte[] encryptedData = CertificateCodec.encryptByPrivateKey(inputData, "./certs/CertificateCodecTest/test.keystore", "1", "123456");

		byte[] decryptedData1 = CertificateCodec.decryptByPublicKey(encryptedData, "./certs/CertificateCodecTest/cert.pem");
		//byte[] decryptedData1 = CertificateCodec.decryptByPublicKey(encryptedData, "./certs/CertificateCodecTest/cert.cer");
		byte[] decryptedData2 = CertificateCodec.decryptByPublicKey(encryptedData, "./certs/CertificateCodecTest/test.keystore", "1",
				"123456");

		assertArrayEquals(inputData, decryptedData1);
		assertArrayEquals(inputData, decryptedData2);
	}

	@Test
	public void testPublicKeyEncryptPrivateKeyDecrypt() throws Exception {
		String inputStr = "Johnson";
		byte[] inputData = inputStr.getBytes("UTF-8");

		byte[] encryptedData1 = CertificateCodec.encryptByPublicKey(inputData, "./certs/CertificateCodecTest/test.keystore", "1", "123456");
		byte[] encryptedData2 = CertificateCodec.encryptByPublicKey(inputData, "./certs/CertificateCodecTest/cert.pem");
		//byte[] encryptedData2 = CertificateCodec.encryptByPublicKey(inputData, "./certs/CertificateCodecTest/cert.cer");

		byte[] decryptedData1 = CertificateCodec.decryptByPrivateKey(encryptedData1, "./certs/CertificateCodecTest/test.keystore", "1",
				"123456");
		byte[] decryptedData2 = CertificateCodec.decryptByPrivateKey(encryptedData2, "./certs/CertificateCodecTest/test.keystore", "1",
				"123456");
		assertArrayEquals(inputData, decryptedData1);
		assertArrayEquals(inputData, decryptedData2);
	}

	@Test
	public void testSignature() throws Exception {
		String inputStr = "Johnson";
		byte[] inputData = inputStr.getBytes("UTF-8");

		byte[] sign = CertificateCodec.sign(inputData, "./certs/CertificateCodecTest/test.keystore", "1", "123456");
		boolean verified = CertificateCodec.verify(inputData, sign, "./certs/CertificateCodecTest/cert.pem");
		//boolean verified = CertificateCodec.verify(inputData, sign, "./certs/CertificateCodecTest/cert.cer");
		System.out.println("signature: " + Base64.encodeBase64String(sign));

		assertTrue(verified);
	}
}
