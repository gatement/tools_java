package johnson.tools;

import static org.junit.Assert.assertNotNull;

import java.io.DataInputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.junit.Test;

public class HttpsTest {
	private String password = "123456";
	private String keyStorePath = "./certs/SSLSocketFactoryUtil/johnsonlau_keycert.keystore";
	private String trustStorePath = "./certs/SSLSocketFactoryUtil/cacert.keystore";
	private String httpsUrl = "https://local.com:40443";

	@Test
	public void test() throws Exception {
		URL url = new URL(httpsUrl);
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.setDoInput(true);
		SSLSocketFactoryUtil.configSSLSocketFactory(conn, password, keyStorePath, trustStorePath);
		int length = conn.getContentLength();
		byte[] data = null;
		if(length != -1) {
			DataInputStream dis = new DataInputStream(conn.getInputStream());
			data = new byte[length];
			dis.readFully(data);
			dis.close();
			System.out.println(new String(data));
		}
		conn.disconnect();
		assertNotNull(data);
	}
}
