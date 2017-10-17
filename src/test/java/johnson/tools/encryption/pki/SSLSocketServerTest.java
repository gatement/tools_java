package johnson.tools.encryption.pki;

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

public class SSLSocketServerTest {
	private String hostname;
	private int port;
	private String cacertKeyStore = "./certs/SSLSocketServer/cacert.keystore";
	private String keyStorePassword = "123456";

	@Before
	public void init() {
		hostname = "local.com";
		port = 10000;
		System.setProperty("javax.net.ssl.trustStore", cacertKeyStore);
		System.setProperty("javax.net.ssl.trustStorePassword", keyStorePassword);
	}

	@Test
	public void test() throws Exception {
		try {
			// start server
			new Thread(new Runnable() {
				public void run() {
					SSLSocketServer server = new SSLSocketServer();
			        try {
						server.run();
					} catch (Exception e) {
						e.printStackTrace();
					}		
				}
			}).start();
			Thread.sleep(500);
			
			// here use the properties above to set keystore path
			// Alternate: use SSLSocketFactoryUtil.getSSLSocketFactory to get SSLSocketFactory as following:
			SSLSocketFactory socketFactory = SSLSocketFactoryUtil.getSSLSocketFactory(keyStorePassword, cacertKeyStore, cacertKeyStore);
			//SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();

			SSLSocket socket = (SSLSocket) socketFactory.createSocket(hostname, port);

			ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
			Map<String, String> map = new HashMap<String, String>();
			map.put("USERNAME", "JohnsonLau");
			map.put("PASSWORD", "123456");
			output.writeObject(map);
			output.flush();

			ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
			String result = input.readUTF();
			input.close();

			System.out.println("return: " + result);
			assertEquals("OK", result);

			output.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}
}