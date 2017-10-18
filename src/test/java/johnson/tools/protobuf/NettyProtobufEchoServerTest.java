package johnson.tools.protobuf;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import johnson.tools.protobuf.AddressBookProtos3.Person;

public class NettyProtobufEchoServerTest {
	private final String host = "localhost";
	private final int port = 10003;
	private CountDownLatch latch = new CountDownLatch(1);

	@Test
	public void test() throws Exception {
		// start server
		new Thread(new Runnable() {
			public void run() {
				NettyProtobufEchoServer server = new NettyProtobufEchoServer();
				try {
					server.run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		Thread.sleep(500);

		// start client
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).remoteAddress(new InetSocketAddress(host, port))
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
							ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
							ch.pipeline().addLast(new ProtobufDecoder(Person.getDefaultInstance()));
							ch.pipeline().addLast(new ProtobufEncoder());
							ch.pipeline().addLast(new MyInboundHandler());
						}
					});
			ChannelFuture f = b.connect().sync();
			latch.await();
			f.channel().close();
		} finally {
			group.shutdownGracefully().sync();
		}
	}

	@Sharable
	public class MyInboundHandler extends SimpleChannelInboundHandler<Person> {
		@Override
		public void channelActive(ChannelHandlerContext ctx) {
			Person p = Person.newBuilder().setId(10).setName("Johnson").setEmail("gatemetn@gmail.com").build();
			ctx.writeAndFlush(p);
		}

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, Person p) throws Exception {
			System.out.println("Client got person: id=" + String.valueOf(p.getId()) + ", name=" + p.getName()
					+ ", email=" + p.getEmail());
			latch.countDown();
		}
	}
}
