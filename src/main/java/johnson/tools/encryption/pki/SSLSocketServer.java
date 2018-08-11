package johnson.tools.encryption.pki;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class SSLSocketServer {
	private int port;
	private String serverKeyCertKeystore = "./certs/SSLSocketServer/server_keycert_with_cacerts.keystore";
	private String keyStorePassword = "123456";

	public SSLSocketServer() {
		port = 10000;
		System.setProperty("javax.net.ssl.keyStore", serverKeyCertKeystore);
		System.setProperty("javax.net.ssl.keyStorePassword", keyStorePassword);
		// System.setProperty("javax.net.ssl.trustStore", "");
		// System.setProperty("javax.net.ssl.trustStorePassword", "123456");
	}

	public void run() throws Exception {
		System.out.println("SSL listening on port: " + port);

		SSLServerSocket serverSocket = null;
		try {
			// here use the properties above to set keystore path
			// Alternate: use SSLSocketFactoryUtil.getSSLServerSocketFactory to get SSLServerSocketFactory as following:
			SSLServerSocketFactory socketFactory = SSLSocketFactoryUtil.getSSLServerSocketFactory(keyStorePassword, serverKeyCertKeystore, serverKeyCertKeystore);
			//SSLServerSocketFactory socketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

			serverSocket = (SSLServerSocket) socketFactory.createServerSocket(port);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		while (true) {
			try {
				SSLSocket socket = (SSLSocket) serverSocket.accept();
				ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

				@SuppressWarnings("unchecked")
				Map<String, String> map = (Map<String, String>) input.readObject();
				String username = map.get("USERNAME");
				String password = map.get("PASSWORD");

				System.out.println("username: " + username + ", password: " + password);

				ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
				output.writeUTF("OK");
				output.flush();
				output.close();

				input.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}