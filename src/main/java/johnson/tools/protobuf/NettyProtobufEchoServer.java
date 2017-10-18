package johnson.tools.protobuf;

import java.net.InetSocketAddress;

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
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import johnson.tools.protobuf.AddressBookProtos3.Person;

// README:
// 1. Command "protoc" and dependency libs could be downloaded from maven.org with "g:com.google.protobuf"
// 2. Generate "AddressBookProtos3.java" by command "protoc --java_out=. addressbook3.proto"
// 3. For protobuf doc refer to "https://developers.google.com/protocol-buffers/docs/proto3"
public class NettyProtobufEchoServer {
	private final int port = 10003;

	public void run() throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(group).channel(NioServerSocketChannel.class).localAddress(new InetSocketAddress(port))
					.childHandler(new ChannelInitializer<Channel>() {
						@Override
						protected void initChannel(Channel ch) throws Exception {
							ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
							ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
							ch.pipeline().addLast(new ProtobufDecoder(Person.getDefaultInstance()));
							ch.pipeline().addLast(new ProtobufEncoder());
							ch.pipeline().addLast(new MyEchoInboundHandler());
						}
					});
			ChannelFuture f = b.bind().sync();
			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully().sync();
		}
	}

	@Sharable
	public class MyEchoInboundHandler extends SimpleChannelInboundHandler<Person> {
		@Override
		protected void channelRead0(ChannelHandlerContext ctx, Person p) throws Exception {
			System.out.println("Server got person: id=" + String.valueOf(p.getId()) + ", name=" + p.getName()
					+ ", email=" + p.getEmail());
			ctx.writeAndFlush(p);
		}
	}
}