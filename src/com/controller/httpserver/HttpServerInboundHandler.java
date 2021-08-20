package com.controller.httpserver;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpResponseStatus.NON_AUTHORITATIVE_INFORMATION;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.handler.codec.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.controller.tcpserver.Utils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class HttpServerInboundHandler extends ChannelInboundHandlerAdapter {
	private static Logger logger = LoggerFactory.getLogger(HttpServerInboundHandler.class);
	private ByteBufToBytes reader;
	String uri;
	String method;
	FullHttpResponse response;
	// HttpTr69Check httpTr69Check = new HttpTr69Check();
	HttpRequest request;
	public static SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static {
		time.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof HttpRequest) {
			request = (HttpRequest) msg;
			uri = request.getUri();
			System.out.println("uri:" + uri);
			method = request.getMethod().toString();
			System.out.println("method:" + method);
			if (HttpHeaders.isContentLengthSet(request)) {
				if (!(request.headers().get(CONTENT_LENGTH).equals("0"))) {
					reader = new ByteBufToBytes((int) HttpHeaders.getContentLength(request));
					System.out.println("Request发过来的数据长度  --" + (int) HttpHeaders.getContentLength(request));
				}
			} else {
				response = new DefaultFullHttpResponse(HTTP_1_1, OK,
						Unpooled.wrappedBuffer("url not match".getBytes()));
				response.headers().set(CONTENT_TYPE, "text/plain");
				response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
				response.headers().set(CONNECTION, Values.KEEP_ALIVE);
				ctx.write(response);
				ctx.flush();
				ctx.close();
			}
		}

		if (msg instanceof HttpContent && reader != null) {
			HttpContent httpContent = (HttpContent) msg;
			ByteBuf content = httpContent.content();
			if (content.capacity() != 0) {
				reader.reading(content, ctx);
				content.release();
				if (reader.isEnd()) {
					String resultStr = new String(reader.readFull());
					System.out.println("httpServer 接收到:" + resultStr);
					if (uri.equals("/randomNumber")) {
						// 预留json信息包含测试路由器mac
						// 通过mac查询结果集，为空就返回对应状态码，有整个结果就返回对应状态码
						response = new DefaultFullHttpResponse(HTTP_1_1, OK,
								Unpooled.wrappedBuffer("{\"rnd\":\"123eb\"}".getBytes()));
						response.headers().set(CONTENT_TYPE, "text/plain");
						response.headers().set("Access-Control-Allow-Origin", "*");
						response.headers().set("Access-Control-Allow-Method", "*");
						response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
						response.headers().set(CONNECTION, Values.KEEP_ALIVE);
						ctx.write(response);
						ctx.flush();
						ctx.close();
					}else if (uri.equals("/checkWifi")) {
						try {
							response = new DefaultFullHttpResponse(HTTP_1_1, OK,
									Unpooled.wrappedBuffer("testdata".getBytes()));
							response.headers().set(CONTENT_TYPE, "text/plain");
							response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
							response.headers().set(CONNECTION, Values.KEEP_ALIVE);
							ctx.write(response);
							ctx.flush();
							ctx.close();
						} catch (Exception e) {
							Utils.jsonFail(ctx, response);
						}
					} 
					else {
						System.out.println("错误参数或uri");
						response = new DefaultFullHttpResponse(HTTP_1_1, NON_AUTHORITATIVE_INFORMATION,
								Unpooled.wrappedBuffer("".getBytes()));
						response.headers().set(CONTENT_TYPE, "text/plain");
						response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
						response.headers().set(CONNECTION, Values.KEEP_ALIVE);
						ctx.write(response);
						ctx.flush();
						ctx.close();
					}
				}
			}
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		logger.info("HttpServerInboundHandler.channelReadComplete");
	}
}