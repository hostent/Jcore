package com.jcore.Rpc;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.jcore.Frame.Request;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class RpcClient   implements Runnable{
	
	
	static HashMap<String,RpcClient> clientMaps=null;
	
	
	private static final Charset UTF_8 = Charset.forName("utf-8");  
	
	SocketHandler clientHandler = new SocketHandler();
	ChannelFuture  future=null;
	String address=null;
	int port =0;
	
	public IEvent event=null;
	
	
	static Lock maplock =   new  ReentrantLock();
	
	
	public RpcClient(String address,int port) {

		
	}
	
	public static void send(String serviceName,Request req)
	{
		RpcClient client;
		
		client = getClient(serviceName);
		
		if(client.event==null)
		{
			client.event = new IEvent() {
				
				@Override
				public void OnConnect() {
					
					
					
				}
			};
		}
			
		connect(client);
		
		ChannelHandlerContext context = client.clientHandler.channelHandlerContext;
		
		
		String reqJson ="123456"; //TODO
		
		ByteBuf b = context.alloc().buffer();
		b.writeBytes(reqJson.getBytes());
		
		
		context.write(b);
		context.flush();
		
		//return client; 
		
		
	}

	private static void connect(RpcClient client) {
		if(!client.future.isSuccess())			
		{				
		     new Thread(client).start(); 		     		     
		}
	}

	private static RpcClient getClient(String serviceName) {
		RpcClient client;
		if(clientMaps.containsKey(serviceName))
		{
			client = clientMaps.get(serviceName);
			
			 
		}
		else
		{
			maplock.lock();
			try {
				if(!clientMaps.containsKey(serviceName))
				{
					client = new RpcClient("127.0.0.1",5053); //TODO
					clientMaps.put(serviceName, client);
				}
				else
				{
					client = clientMaps.get(serviceName);
				}
				
			} finally {
				maplock.unlock();
			}			
			
		}
		return client;
	}
	



	@Override
	public void run() {
		
	     EventLoopGroup workerGroup = new NioEventLoopGroup(2);//1 is OK  
	        try {  
	        	Bootstrap   bootstrap = new Bootstrap();  
	            bootstrap.group(workerGroup)  
	                    .channel(NioSocketChannel.class) //create SocketChannel transport  
	                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,10000)  
	                    .handler(new ChannelInitializer<SocketChannel>() {  
	                        @Override  
	                        protected void initChannel(SocketChannel ch) throws Exception {  
	                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(10240, 0, 2, 0, 2))  
	                                    .addLast(new StringDecoder(UTF_8))  
	                                    .addLast(new LengthFieldPrepender(2))  
	                                    .addLast(new StringEncoder(UTF_8))  
	                                    .addLast(clientHandler);//the same as ServerBootstrap  
	                        }  
	                    });  
	            //keep the connection with server，and blocking until closed!   
	            future = bootstrap.connect(new InetSocketAddress(address, port)).sync();  
			} catch (Exception e) {  
				workerGroup.shutdownGracefully();
		    } finally {  
		        
		    } 
	}
	
	
	
	public void callBack()
	{
		
	}

	

	

}
