package johnson.tools.encryption.pki;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.ssl.ClientAuth;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.util.CharsetUtil;

public class NettyHttpsServer {
	private final int port = 10002;
	private String serverKeyCertKeyStorePath = "./certs/NettyHttpsServer/server_keycert_with_cacerts.keystore";
	private String serverKeyCertKeyStorePassword = "123456";
	private String clientCacertKeyStorePath = "./certs/NettyHttpsServer/client_cacert.keystore";
	private String clientCacertKeyStorePassword = "123456";

	public void run() throws Exception {
		final SslContext sslContext = SslContextBuilder
				.forServer(getKeyManagerFactory(serverKeyCertKeyStorePath, serverKeyCertKeyStorePassword))
				.trustManager(getTrustManagerFactory(clientCacertKeyStorePath, clientCacertKeyStorePassword))
				.clientAuth(ClientAuth.REQUIRE).build();
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(group).channel(NioServerSocketChannel.class).localAddress(new InetSocketAddress(port))
					.childHandler(new ChannelInitializer<Channel>() {
						@Override
						protected void initChannel(Channel ch) throws Exception {
							ch.pipeline().addFirst("ssl", sslContext.newHandler(ch.alloc()));
							ch.pipeline().addLast("http_codec", new HttpServerCodec());
							ch.pipeline().addLast("decompressor", new HttpContentCompressor());
							ch.pipeline().addLast("aggregator", new HttpObjectAggregator(10240000));
							ch.pipeline().addLast(new MyHttpServerHandler());
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
	public class MyHttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
		@Override
		protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
			String content = request.content().toString(CharsetUtil.UTF_8);
			if (!content.isEmpty()) {
				System.out.println("Server got: " + content);
			}
			ctx.writeAndFlush(getResponse());
		}

		private FullHttpResponse getResponse() {
			FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
					Unpooled.wrappedBuffer("It works!".getBytes()));
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
			response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
			response.headers().set(HttpHeaderNames.CONNECTION, "keep-alive");
			return response;
		}
	}
}
