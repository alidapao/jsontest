package com.controller.httpclient;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;

import com.controller.httpserver.ByteBufToBytes;
import com.controller.tcpserver.Utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpChunkedInput;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.stream.ChunkedFile;
import io.netty.handler.stream.ChunkedInput;

public class HttpClientInboundHandler extends ChannelInboundHandlerAdapter {
	private ByteBufToBytes reader;
	private HttpResponse response;
	HttpClientInboundHandler(String province,String group){
		
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof HttpResponse) {
			response = (HttpResponse) msg;
			if (response.headers().get(CONTENT_LENGTH)!=null) {
				if(!response.headers().get(CONTENT_LENGTH).equals("0")){
					reader = new ByteBufToBytes((int) HttpHeaders.getContentLength(response));
				}
				
			}

		}
		if (msg instanceof HttpContent) {
			String resultStr  = "";
			if(response.headers().get("Transfer-Encoding")!=null){
				if(response.headers().get("Transfer-Encoding").equals("chunked")){
					HttpContent httpContent = (HttpContent) msg;
					ByteBuf content = httpContent.content();
					System.out.println("接收到Transfer-Encoding为备处理chunked");
					System.out.println("httpContent is   :"+httpContent.toString());
					if (content.capacity() != 0) {
						reader = new ByteBufToBytes(content.capacity());
						reader.reading(content, ctx);
						content.release();
						if (reader.isEnd()) {
							resultStr = new String(reader.readFull());
							System.out.println(resultStr);
						}
					}
				}

				
			}else{
				HttpContent httpContent = (HttpContent) msg;
				ByteBuf content = httpContent.content();
				if (content.capacity() != 0) {
					reader.reading(content, ctx);
					content.release();
					if (reader.isEnd()) {
						resultStr = new String(reader.readFull());
						System.out.println(resultStr);
					}
				}
			}
			if(resultStr.contains("provinceBoxList")){

			}
			ctx.close();
		}
	}
}