package johnson.tools.encryption.pki;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.LineEncoder;
import io.netty.handler.codec.string.LineSeparator;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.util.CharsetUtil;

public class NettySslServer {
	private final int port = 10001;
	private String serverKeyCertKeyStore = "./certs/NettySslServer/server_keycert_with_intermediate_ca.keystore";
	private String keyStorePassword = "123456";

	public void run() throws Exception {
		final SslContext sslContext = SslContextBuilder
				.forServer(getKeyManagerFactory(serverKeyCertKeyStore, keyStorePassword)).build();
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(group).channel(NioServerSocketChannel.class).localAddress(new InetSocketAddress(port))
					.childHandler(new ChannelInitializer<Channel>() {
						@Override
						protected void initChannel(Channel ch) throws Exception {
							ch.pipeline().addFirst("ssl", sslContext.newHandler(ch.alloc()));
							ch.pipeline().addLast("lineEncoder",
									new LineEncoder(LineSeparator.UNIX, CharsetUtil.UTF_8));
							ch.pipeline().addLast("frameDecoder", new LineBasedFrameDecoder(100));
							ch.pipeline().addLast("stringDecoder", new StringDecoder(CharsetUtil.UTF_8));
							ch.pipeline().addLast("echoHandler", new MyInboundHandler());
						}
					});
			ChannelFuture f = b.bind().sync();
			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully().sync();
		}
	}

	private KeyManagerFactory getKeyManagerFactory(String keyStorePath, String password) throws Exception {
		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		KeyStore keyStore = getKeyStore(keyStorePath, password);
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

	@Sharable
	private class MyInboundHandler extends SimpleChannelInboundHandler<String> {

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
			//System.out.println("Server got: " + msg);
			ctx.writeAndFlush(msg);
		}
	}
}