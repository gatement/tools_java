package johnson.tools.encryption.pki;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.security.KeyStore;

import javax.net.ssl.TrustManagerFactory;

import org.junit.Test;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

public class NettySSLServerTest {
	private final String host = "localhost";
	private final int port = 10001;
	private String cacertKeyStore = "./certs/NettySSLServer/cacert.keystore";
	private String keyStorePassword = "123456";

	@Test
	public void test() throws Exception {
		// start server
		new Thread(new Runnable() {
			public void run() {
				NettySSLServer server = new NettySSLServer();
				try {
					server.run();
				} catch (Exception e) {
					e.printStackTrace();
				}		
			}
		}).start();
		Thread.sleep(500);

		final SslContext sslContext = SslContextBuilder.forClient()
				.trustManager(getTrustManagerFactory(cacertKeyStore, keyStorePassword)).build();
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).remoteAddress(new InetSocketAddress(host, port))
					.handler(new ChannelInitializer<Channel>() {
						@Override
						protected void initChannel(Channel ch) throws Exception {
							ch.pipeline().addFirst("ssl", sslContext.newHandler(ch.alloc()));
						}
					});
			ChannelFuture f = b.connect().sync();
			Thread.sleep(1000);
			System.out.println("connect netty ssl server successfully!");
			f.channel().close();
		} finally {
			group.shutdownGracefully().sync();
		}
	}

	private TrustManagerFactory getTrustManagerFactory(String trustStorePath, String password) throws Exception {
		TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		KeyStore trustStore = getKeyStore(trustStorePath, password);
		trustManagerFactory.init(trustStore);
		return trustManagerFactory;
	}

	private KeyStore getKeyStore(String keyStorePath, String password) throws Exception {
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		FileInputStream is = new FileInputStream(keyStorePath);
		ks.load(is, password.toCharArray());
		is.close();
		return ks;
	}
}
