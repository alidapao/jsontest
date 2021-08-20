package com.controller.httpserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class HttpServer implements Runnable {
	private int port;

	public HttpServer(int port) {
		this.port = port;
	}

	@Override
	public void run() {
		try {
			EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
			EventLoopGroup workerGroup = new NioEventLoopGroup();
			try {
				ServerBootstrap b = new ServerBootstrap(); // (2)
				b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class) // (3)
						.childHandler(new ChannelInitializer<SocketChannel>() { // (4)
							@Override
							public void initChannel(SocketChannel ch) throws Exception {
								// server端发送的是httpResponse，所以要使用HttpResponseEncoder进行编码
								ch.pipeline().addLast(new HttpResponseEncoder());
								// server端接收到的是httpRequest，所以要使用HttpRequestDecoder进行解码
								ch.pipeline().addLast(new HttpRequestDecoder());
								ch.pipeline().addLast(new HttpServerInboundHandler());
							}
						}).option(ChannelOption.SO_BACKLOG, 128) // (5)
						.childOption(ChannelOption.SO_KEEPALIVE, true); // (6)
				ChannelFuture f = b.bind(port).sync(); // (7)
				f.channel().closeFuture().sync();
			} finally {
				workerGroup.shutdownGracefully();
				bossGroup.shutdownGracefully();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}