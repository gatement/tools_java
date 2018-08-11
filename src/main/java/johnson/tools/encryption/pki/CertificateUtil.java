package johnson.tools.encryption.pki;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.crypto.Cipher;

public class CertificateUtil {
	public static final String CERT_TYPE = "X.509";

	// == encrypt =============================================================
	public static byte[] encryptByPrivateKey(byte[] data, String keyStorePath, String alias, String password)
			throws Exception {
		PrivateKey privateKey = getPrivateKeyFromKeyStoreFile(keyStorePath, alias, password);
		Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}

	public static byte[] encryptByPublicKey(byte[] data, String keyStorePath, String alias, String password)
			throws Exception {
		PublicKey publicKey = getPublicKeyFromKeyStoreFile(keyStorePath, alias, password);
		Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(data);
	}

	public static byte[] encryptByPublicKey(byte[] data, String certificatePath) throws Exception {
		PublicKey publicKey = getPublicKeyFromCertificateFile(certificatePath);
		Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(data);
	}

	// == decrypt =============================================================
	public static byte[] decryptByPrivateKey(byte[] data, String keyStorePath, String alias, String password)
			throws Exception {
		PrivateKey privateKey = getPrivateKeyFromKeyStoreFile(keyStorePath, alias, password);
		Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}

	public static byte[] decryptByPublicKey(byte[] data, String keyStorePath, String alias, String password)
			throws Exception {
		PublicKey publicKey = getPublicKeyFromKeyStoreFile(keyStorePath, alias, password);
		Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		return cipher.doFinal(data);
	}

	public static byte[] decryptByPublicKey(byte[] data, String certificatePath) throws Exception {
		PublicKey publicKey = getPublicKeyFromCertificateFile(certificatePath);
		Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		return cipher.doFinal(data);
	}

	// == signing =============================================================
	public static byte[] sign(byte[] data, String keyStorePath, String alias, String password) throws Exception {
		X509Certificate x509Certificate = (X509Certificate) getCertificateFromKeyStoreFile(keyStorePath, alias, password);
		Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
		PrivateKey privateKey = getPrivateKeyFromKeyStoreFile(keyStorePath, alias, password);
		signature.initSign(privateKey);
		signature.update(data);
		return signature.sign();
	}
	
	public static boolean verify(byte[] data, byte[] sign, String certificatePath) throws Exception {
		X509Certificate x509Certificate = (X509Certificate) getCertificateFromCertificateFile(certificatePath);
		Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
		signature.initVerify(x509Certificate);
		signature.update(data);
		return signature.verify(sign);
	}

	// == helper ==============================================================
	private static PrivateKey getPrivateKeyFromKeyStoreFile(String keyStorePath, String alias, String password)
			throws Exception {
		KeyStore ks = getKeyStoreFromKeyStoreFile(keyStorePath, password);
		return (PrivateKey) ks.getKey(alias, password.toCharArray());
	}

	private static PublicKey getPublicKeyFromKeyStoreFile(String keyStorePath, String alias, String password)
			throws Exception {
		Certificate certificate = getCertificateFromKeyStoreFile(keyStorePath, alias, password);
		return certificate.getPublicKey();
	}

	private static Certificate getCertificateFromKeyStoreFile(String keyStorePath, String alias, String password) throws Exception {
		KeyStore ks = getKeyStoreFromKeyStoreFile(keyStorePath, password);
		return ks.getCertificate(alias);
	}

	private static KeyStore getKeyStoreFromKeyStoreFile(String keyStorePath, String password) throws Exception {
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		FileInputStream is = new FileInputStream(keyStorePath);
		ks.load(is, password.toCharArray());
		is.close();
		return ks;
	}

	private static PublicKey getPublicKeyFromCertificateFile(String certificatePath) throws Exception {
		Certificate certificate = getCertificateFromCertificateFile(certificatePath);
		return certificate.getPublicKey();
	}

	private static Certificate getCertificateFromCertificateFile(String certificatePath) throws Exception {
		CertificateFactory certificateFactory = CertificateFactory.getInstance(CERT_TYPE);
		FileInputStream is = new FileInputStream(certificatePath);
		Certificate certificate = certificateFactory.generateCertificate(is);
		is.close();
		return certificate;
	}
}