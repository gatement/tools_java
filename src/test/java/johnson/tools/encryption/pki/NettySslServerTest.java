package johnson.tools.encryption.pki;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.security.KeyStore;

import javax.net.ssl.TrustManagerFactory;

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
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.LineEncoder;
import io.netty.handler.codec.string.LineSeparator;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.util.CharsetUtil;

public class NettySslServerTest {
	private final String host = "localhost";
	private final int port = 10001;
	private String cacertKeyStore = "./certs/NettySslServer/cacert.keystore";
	private String keyStorePassword = "123456";

	@Test
	public void test() throws Exception {
		// start server
		new Thread(new Runnable() {
			public void run() {
				NettySslServer server = new NettySslServer();
				try {
					server.run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		Thread.sleep(500);

		// start client
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
							ch.pipeline().addLast("lineEncoder",
									new LineEncoder(LineSeparator.UNIX, CharsetUtil.UTF_8));
							ch.pipeline().addLast("frameDecoder", new LineBasedFrameDecoder(100));
							ch.pipeline().addLast("stringDecoder", new StringDecoder(CharsetUtil.UTF_8));
							ch.pipeline().addLast("myHandler", new MyInboundHandler());
						}
					});
			ChannelFuture f = b.connect().sync();
			f.channel().closeFuture().sync();
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

	private KeyStore getKeyStore(String keyStorePath, String password) throws Exception {
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		FileInputStream is = new FileInputStream(keyStorePath);
		ks.load(is, password.toCharArray());
		is.close();
		return ks;
	}

	@Sharable
	private class MyInboundHandler extends SimpleChannelInboundHandler<String> {
		@Override
		public void channelActive(ChannelHandlerContext ctx) {
			ctx.writeAndFlush("123\n");
		}

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
			//System.out.println("Client got: " + msg);
			ctx.channel().close();
		}
	}
}