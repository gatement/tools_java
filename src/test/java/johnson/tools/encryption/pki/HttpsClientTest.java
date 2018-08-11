package johnson.tools.encryption.pki;

import static org.junit.Assert.assertNotNull;

import java.io.DataInputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import org.junit.Test;

public class HttpsClientTest {
	private String httpsUrl = "https://local.com:40443";
	private String password = "123456";
	private String keyStorePath = "./certs/HttpsClientTest/client_keycert.keystore";
	private String trustStorePath = "./certs/HttpsClientTest/server_cacert.keystore";

	@Test
	public void test() throws Exception {
		URL url = new URL(httpsUrl);
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.setDoInput(true);

		SSLSocketFactory sslSocketFactory = SSLSocketFactoryUtil.getSSLSocketFactory(password, keyStorePath, trustStorePath);
		conn.setSSLSocketFactory(sslSocketFactory);

		int length = conn.getContentLength();
		byte[] data = null;
		if(length != -1) {
			DataInputStream dis = new DataInputStream(conn.getInputStream());
			data = new byte[length];
			dis.readFully(data);
			dis.close();
			//System.out.println(new String(data));
		}
		conn.disconnect();
		assertNotNull(data);
	}
}
