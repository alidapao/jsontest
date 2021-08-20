package com.controller.httpclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpVersion;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class HttpClient {
	String province;
	String group;
	ChannelFuture f;
	Bootstrap b;
	DefaultFullHttpRequest request;
	public static SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static {

		time.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
	}

	public HttpClient() {
		// TODO Auto-generated constructor stub
	}
	public void connect(String host, int port, String uris, String message) throws Exception {
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			b = new Bootstrap();
			b.group(workerGroup);
			b.channel(NioSocketChannel.class);
			b.option(ChannelOption.SO_KEEPALIVE, true);
			b.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					// 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
					ch.pipeline().addLast(new HttpResponseDecoder());
					// 客户端发送的是httprequest，所以要使用HttpRequestEncoder进行编码
					ch.pipeline().addLast(new HttpRequestEncoder());
					ch.pipeline().addLast(new HttpClientInboundHandler(province,group));
				}
			});

			// Start the client.
			f = b.connect(host, port).sync();
			URI uri = new URI(uris);
			if (!message.equals("")) {
				request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, uri.toASCIIString(),
						Unpooled.wrappedBuffer(message.getBytes("UTF-8")));
			} else {
				request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, uri.toASCIIString());
			}

			// 构建http请求
			request.headers().set(HttpHeaders.Names.HOST, host);
			request.headers().set("X-Auth-Token","dhome-reply:111:222");
			request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
			request.headers().set(HttpHeaders.Names.CONTENT_TYPE, HttpHeaders.Values.APPLICATION_JSON);
			request.headers().set(HttpHeaders.Names.CONTENT_LENGTH, request.content().readableBytes());
			// 发送http请求
			f.channel().write(request);
			f.channel().flush();
			f.channel().closeFuture().sync();
			System.out.println("关闭closeFuture");
		} finally {
			workerGroup.shutdownGracefully();
			System.out.println("关闭workerGroup.shutdownGracefully()");
		}
	}
}
