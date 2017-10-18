package johnson.tools.encryption.pki;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.util.concurrent.CountDownLatch;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

import org.junit.Before;
import org.junit.Test;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.util.CharsetUtil;

public class NettyHttpsServerTest {
	private final String url = "https://localhost:10002";
	private String cacertKeyStorePath = "./certs/NettyHttpsServerTest/server_cacert.keystore";
	private String cacertKeyStorePassword = "123456";
	private String clientKeyStorePath = "./certs/NettyHttpsServerTest/client_keycert.keystore";
	private String clientKeyStorePassword = "123456";
	private CountDownLatch latch = new CountDownLatch(1);
	private String host;
	private int port;

	@Before
	public void init() throws Exception {
		try {
			URI uri = new URI(url);
			this.host = uri.getHost();
			this.port = uri.getPort() == -1 ? 80 : uri.getPort();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Test
	public void test() throws Exception {
		// start server
		new Thread(new Runnable() {
			public void run() {
				NettyHttpsServer server = new NettyHttpsServer();
				try {
					server.run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		Thread.sleep(500);

		System.out.println("start connecting " + host + ":" + port);
		final SslContext sslContext = SslContextBuilder.forClient()
				.trustManager(getTrustManagerFactory(cacertKeyStorePath, cacertKeyStorePassword))
				.keyManager(getKeyManagerFactory(clientKeyStorePath, clientKeyStorePassword)).build();
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).remoteAddress(new InetSocketAddress(host, port))
					.handler(new ChannelInitializer<Channel>() {
						@Override
						protected void initChannel(Channel ch) throws Exception {
							ch.pipeline().addFirst("ssl", sslContext.newHandler(ch.alloc()));
							ch.pipeline().addLast("http_codec", new HttpClientCodec());
							ch.pipeline().addLast("decompressor", new HttpContentDecompressor());
							ch.pipeline().addLast("aggregator", new HttpObjectAggregator(10240000));
							ch.pipeline().addLast(new MyHttpClientHandler());
						}
					});
			ChannelFuture f = b.connect().sync();
			f.channel().writeAndFlush(createHttpRequest());
			latch.await();
			f.channel().close();
		} finally {
			group.shutdownGracefully().sync();
		}
	}

	private TrustManagerFactory getTrustManagerFactory(String trustStorePath, String password) throws Exception {
		TrustManagerFactory trustManagerFactory = TrustManagerFactory
				.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		KeyStore trustStore = getKeyStore(trustStorePath, password);
		trustManagerFactory.init(trustStore);
		return trustManagerFactory;
	}

	private KeyManagerFactory getKeyManagerFactory(String clientKeyStorePath, String password) throws Exception {
		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		KeyStore keyStore = getKeyStore(clientKeyStorePath, password);
		keyManagerFactory.init(keyStore, password.toCharArray());
		return keyManagerFactory;
	}

	private KeyStore getKeyStore(String keyStorePath, String password) throws Exception {
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		FileInputStream is = new FileInputStream(keyStorePath);
		ks.load(is, password.toCharArray());
		is.close();
		return ks;
	}

	private HttpRequest createHttpRequest() {
		HttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/");
		request.headers().add("Host", host + ":" + port); // this header is required for Nginx
		request.headers().set(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP);
		return request;
	}

	@Sharable
	public class MyHttpClientHandler extends SimpleChannelInboundHandler<FullHttpResponse> {
		@Override
		protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse response) throws Exception {
			String content = response.content().toString(CharsetUtil.UTF_8);
			System.out.println("Client got: " + content);
			latch.countDown();
		}
	}
}
