package UnitTest;

 

import com.jcore.Rpc.PacketServiceHander;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class NettyServiceTest {

	private static final int PORT = Integer.parseInt(System.getProperty("port", "7686"));

	public static void main(String[] args) throws InterruptedException {

		EventLoopGroup group = new NioEventLoopGroup();

		try {

			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioDatagramChannel.class).option(ChannelOption.SO_BROADCAST, true)
					.handler(new PacketServiceHander());

			b.bind(PORT).sync().channel().closeFuture().await();

		} finally {
			group.shutdownGracefully();
		}
	}

}
