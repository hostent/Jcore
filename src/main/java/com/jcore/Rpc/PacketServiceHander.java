package com.jcore.Rpc;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

public class PacketServiceHander extends SimpleChannelInboundHandler<DatagramPacket> {

	 
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packetReceive) throws Exception {
		
		String receiveMsg = packetReceive.content().toString(CharsetUtil.UTF_8);
		
		DatagramPacket packetSend = new DatagramPacket(Unpooled.copiedBuffer("Hello!" , CharsetUtil.UTF_8), packetReceive.sender());
		
		
		ctx.write(packetSend);
		
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		 ctx.flush();
	}
	
	
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		
		// We don't close the channel because we can keep serving requests.
	}

	@Override
	protected void messageReceived(ChannelHandlerContext arg0, DatagramPacket arg1) throws Exception {
		// TODO 自动生成的方法存根
		
		String receiveMsg = arg1.content().toString(CharsetUtil.UTF_8);
				
				DatagramPacket packetSend = new DatagramPacket(Unpooled.copiedBuffer("Hello!" , CharsetUtil.UTF_8), arg1.sender());
				
				
				arg0.write(packetSend);
		
	}
	
	
}
