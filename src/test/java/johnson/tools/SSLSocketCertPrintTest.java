package johnson.tools;

import java.security.cert.Certificate;
import java.util.Base64;

import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.junit.Before;
import org.junit.Test;

public class SSLSocketCertPrintTest {
	private String password = "123456";
	private String keyStorePath = "./certs/SSLSocketFactoryUtil/johnsonlau_keycert.keystore";
	private String trustStorePath = "./certs/SSLSocketFactoryUtil/cacert.keystore";

	private String hostname;
	private int port;
	
	@Before
	public void init() {
		hostname = "local.com";
		port = 40443;
	}

	@Test
	public void test() throws Exception {
		//System.setProperty("javax.net.debug", "all");
		SSLSocketFactory factory = SSLSocketFactoryUtil.getSSLSocketFactory(password, keyStorePath, trustStorePath);

		SSLSocket socket = (SSLSocket) factory.createSocket(hostname, port);
		socket.startHandshake();
		SSLSession session = socket.getSession();
		socket.close();
		
		Certificate[] servercerts = session.getPeerCertificates();
		for(Certificate cer : servercerts) {
			System.out.println("========================================");
			System.out.println("Certificate: " + cer.toString());
			//System.out.println(Base64.getMimeEncoder().encodeToString(cer.getEncoded()));
		}
	}
}
