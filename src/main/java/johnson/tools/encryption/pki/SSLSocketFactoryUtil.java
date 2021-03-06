package johnson.tools.encryption.pki;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class SSLSocketFactoryUtil {
	public static final String PROTOCOL = "TLS";

	public static SSLSocketFactory getSSLSocketFactory(String password, String keyStorePath, String trustStorePath)
			throws Exception {
		SSLContext ctx = getSSLContext(password, keyStorePath, trustStorePath);
		return ctx.getSocketFactory();
	}

	public static SSLServerSocketFactory getSSLServerSocketFactory(String password, String keyStorePath, String trustStorePath)
			throws Exception {
		SSLContext ctx = getSSLContext(password, keyStorePath, trustStorePath);
		return ctx.getServerSocketFactory();
	}

	private static KeyStore getKeyStore(String keyStorePath, String password) throws Exception {
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		FileInputStream is = new FileInputStream(keyStorePath);
		ks.load(is, password.toCharArray());
		is.close();
		return ks;
	}

	private static SSLContext getSSLContext(String password, String keyStorePath, String trustStorePath)
			throws Exception {
		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		KeyStore keyStore = getKeyStore(keyStorePath, password);
		keyManagerFactory.init(keyStore, password.toCharArray());

		TrustManagerFactory trustManagerFactory = TrustManagerFactory
				.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		KeyStore trustStore = getKeyStore(trustStorePath, password);
		trustManagerFactory.init(trustStore);

		SSLContext ctx = SSLContext.getInstance(PROTOCOL);
		ctx.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
		return ctx;
	}
}