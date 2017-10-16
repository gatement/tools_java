package johnson.tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.junit.Before;
import org.junit.Test;

public class SSLSocketClientTest {
	private String hostname;
	private int port;
	private String cacertKeyStore = "./certs/SSLSocketServer/cacert.keystore";
	private String keyStorePassword = "123456";

	@Before
	public void init() {
		hostname = "local.com";
		port = 7070;
		System.setProperty("javax.net.ssl.trustStore", cacertKeyStore);
		System.setProperty("javax.net.ssl.trustStorePassword", keyStorePassword);
	}

	@Test
	public void test() throws Exception {
		try {
			// here use the properties above to set keystore path
			// Alternate: use SSLSocketFactoryUtil.getSSLSocketFactory to get SSLSocketFactory
			//SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			SSLSocketFactory socketFactory = SSLSocketFactoryUtil.getSSLSocketFactory(keyStorePassword, cacertKeyStore, cacertKeyStore);

			SSLSocket socket = (SSLSocket) socketFactory.createSocket(hostname, port);
			ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
			Map<String, String> map = new HashMap<String, String>();
			map.put("USERNAME", "JohnsonLau");
			map.put("PASSWORD", "123456");
			output.writeObject(map);
			output.flush();
			ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
			String result = input.readUTF();
			System.out.println("return: " + result);
			assertEquals("OK", result);
			output.close();
			input.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}

}
