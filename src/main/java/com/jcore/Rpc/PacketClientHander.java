package com.jcore.Rpc;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

public class PacketClientHander extends SimpleChannelInboundHandler<DatagramPacket> {

/*	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msgPacket) throws Exception {
		String response = msgPacket.content().toString(CharsetUtil.UTF_8);
		
		
		
		
	}*/
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		 
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	protected void messageReceived(ChannelHandlerContext arg0, DatagramPacket arg1) throws Exception {
		// TODO 自动生成的方法存根
		
		String response = arg1.content().toString(CharsetUtil.UTF_8);
		
	}

}
