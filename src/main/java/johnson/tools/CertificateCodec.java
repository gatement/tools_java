package johnson.tools;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.crypto.Cipher;

public class CertificateCodec {
	public static final String CERT_TYPE = "X.509";

	public static byte[] encryptByPrivateKey(byte[] data, String keyStorePath, String alias, String password)
			throws Exception {
		PrivateKey privateKey = getPrivateKeyByKeyStore(keyStorePath, alias, password);
		Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}

	public static byte[] decryptByPrivateKey(byte[] data, String keyStorePath, String alias, String password)
			throws Exception {
		PrivateKey privateKey = getPrivateKeyByKeyStore(keyStorePath, alias, password);
		Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}

	public static byte[] encryptByPublicKey(byte[] data, String certificatePath) throws Exception {
		PublicKey publicKey = getPublicKeyByCertificate(certificatePath);
		Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(data);
	}

	public static byte[] encryptByPublicKey(byte[] data, String keyStorePath, String alias, String password)
			throws Exception {
		PublicKey publicKey = getPublicKeyByKeyStore(keyStorePath, alias, password);
		Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(data);
	}

	public static byte[] decryptByPublicKey(byte[] data, String certificatePath) throws Exception {
		PublicKey publicKey = getPublicKeyByCertificate(certificatePath);
		Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		return cipher.doFinal(data);
	}

	public static byte[] decryptByPublicKey(byte[] data, String keyStorePath, String alias, String password)
			throws Exception {
		PublicKey publicKey = getPublicKeyByKeyStore(keyStorePath, alias, password);
		Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		return cipher.doFinal(data);
	}

	public static byte[] sign(byte[] data, String keyStorePath, String alias, String password) throws Exception {
		X509Certificate x509Certificate = (X509Certificate) getCertificate(keyStorePath, alias, password);
		Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
		PrivateKey privateKey = getPrivateKeyByKeyStore(keyStorePath, alias, password);
		signature.initSign(privateKey);
		;
		signature.update(data);
		return signature.sign();
	}
	
	public static boolean verify(byte[] data, byte[] sign, String certificatePath) throws Exception {
		X509Certificate x509Certificate = (X509Certificate) getCertificate(certificatePath);
		Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
		signature.initVerify(x509Certificate);
		signature.update(data);
		return signature.verify(sign);
	}

	private static PrivateKey getPrivateKeyByKeyStore(String keyStorePath, String alias, String password)
			throws Exception {
		KeyStore ks = getKeyStore(keyStorePath, password);
		return (PrivateKey) ks.getKey(alias, password.toCharArray());
	}

	private static PublicKey getPublicKeyByCertificate(String certificatePath) throws Exception {
		Certificate certificate = getCertificate(certificatePath);
		return certificate.getPublicKey();
	}

	private static PublicKey getPublicKeyByKeyStore(String keyStorePath, String alias, String password)
			throws Exception {
		Certificate certificate = getCertificate(keyStorePath, alias, password);
		return certificate.getPublicKey();
	}

	private static Certificate getCertificate(String certificatePath) throws Exception {
		CertificateFactory certificateFactory = CertificateFactory.getInstance(CERT_TYPE);
		FileInputStream is = new FileInputStream(certificatePath);
		Certificate certificate = certificateFactory.generateCertificate(is);
		is.close();
		return certificate;
	}

	private static Certificate getCertificate(String keyStorePath, String alias, String password) throws Exception {
		KeyStore ks = getKeyStore(keyStorePath, password);
		return ks.getCertificate(alias);
	}

	private static KeyStore getKeyStore(String keyStorePath, String password) throws Exception {
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		FileInputStream is = new FileInputStream(keyStorePath);
		ks.load(is, password.toCharArray());
		is.close();
		return ks;
	}
}