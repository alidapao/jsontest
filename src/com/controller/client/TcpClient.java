package com.controller.client;

import java.text.SimpleDateFormat;

import com.controller.client.MyDecoder;
import com.controller.client.MyEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class TcpClient implements Runnable {
	private String address;
	private int port;
	public TcpClient(String address,int port) {
		this.address = address;
		this.port = port;
	}

	/**
	 * 网络事件处理器
	 */
	private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			// 添加自定义协议的编解码工具
			ch.pipeline().addLast(new MyEncoder());
			ch.pipeline().addLast(new MyDecoder());
			ch.pipeline().addLast(new IdleStateHandler(20, 0, 0));
			ch.pipeline().addLast(new TcpClientHandler());
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			// 服务器辅助启动类配置
			Bootstrap b = new Bootstrap();
			b.group(workerGroup)
					.channel(NioSocketChannel.class)
					.handler(new ChildChannelHandler())
					.option(ChannelOption.CONNECT_TIMEOUT_MILLIS,3000); 
			// Here is the line that starts the TCP client:
			ChannelFuture f;
			try {
				f = b.connect(address, port).sync();
				f.channel().closeFuture().sync();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// The following line waits until the client shuts down: 

		} finally {
			// 优雅释放线程资源
			workerGroup.shutdownGracefully();
			Utils.shutdown = true;
			Utils.ctxTemp = null;
			System.out.println("client tcp 线程组资源被释放");
		}
	}
}
