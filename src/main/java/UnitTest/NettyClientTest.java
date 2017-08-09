package UnitTest;

 

import com.jcore.Rpc.PacketClientHander;

import io.netty.bootstrap.*;
import io.netty.buffer.*;
import io.netty.channel.*;
import io.netty.channel.nio.*;
import io.netty.channel.socket.*;
import io.netty.channel.socket.nio.*;
import io.netty.util.*;
import io.netty.util.internal.*;

public class NettyClientTest {

	static final int PORT = Integer.parseInt(System.getProperty("port", "7686"));

	public static void main(String[] args) throws InterruptedException {

		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioDatagramChannel.class).option(ChannelOption.SO_BROADCAST, true)
					.handler(new PacketClientHander());

			Channel ch = b.bind(0).sync().channel();

			// Broadcast the QOTM request to port 8080.
			ch.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("QOTM?", CharsetUtil.UTF_8),
					SocketUtils.socketAddress("255.255.255.255", PORT))).sync();

			// QuoteOfTheMomentClientHandler will close the DatagramChannel when
			// a
			// response is received. If the channel is not closed within 5
			// seconds,
			// print an error message and quit.
			if (!ch.closeFuture().await(5000)) {
				System.err.println("QOTM request timed out.");
			}
		} finally {
			group.shutdownGracefully();
		}

	}

}
