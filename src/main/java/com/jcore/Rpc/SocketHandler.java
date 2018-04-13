package com.jcore.Rpc;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class SocketHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)


    	 try {
    		 
    		 ByteBuf in = (ByteBuf) msg;
    		 
    		 while (in.isReadable()) { // (1)
    	            System.out.print((char) in.readByte());
    	            
    	            
    	            
    	            System.out.flush();
    	        }
    		 
    	        // Do something with msg
    	    } finally {
    	        ReferenceCountUtil.release(msg);
    	    }
    	
        //((ByteBuf) msg).release(); // (3)
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
    
    
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {


    	 System.out.print("handlerAdded");
    	super.handlerAdded(ctx);
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

//
//    	final ByteBuf time = ctx.alloc().buffer(4); // (2)
//        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));
//        
//        final ChannelFuture f = ctx.writeAndFlush(time); // (3)
//        f.addListener(new ChannelFutureListener() {
//            @Override
//            public void operationComplete(ChannelFuture future) {
//                assert f == future;
//                ctx.close();
//            }
//        }); // (4)
    	
    	super.channelActive(ctx);
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	System.out.print("channelInactive");
    	super.channelInactive(ctx);
    }
    
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
    	System.out.print("channelRegistered");
    	super.channelRegistered(ctx);
    }
    
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
    	System.out.print("channelUnregistered");
    	super.channelUnregistered(ctx);
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    	System.out.print("channelReadComplete");
    	super.channelReadComplete(ctx);
    }
    
    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
    	System.out.print("channelWritabilityChanged");
    	super.channelWritabilityChanged(ctx);
    }
    
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    	System.out.print("userEventTriggered");
    	super.userEventTriggered(ctx, evt);
    }
    
    
    
	
}
