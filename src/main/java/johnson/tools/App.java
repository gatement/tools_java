package johnson.tools;

public class App {
	public static void main(String[] args) throws Exception {
		SSLSocketServer server = new SSLSocketServer();
		server.run();
	}
}
