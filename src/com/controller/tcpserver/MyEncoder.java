package com.controller.tcpserver;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MyEncoder extends MessageToByteEncoder<MessageProtocol> {
	// static ByteBuf out ;
	@Override
	protected void encode(ChannelHandlerContext ctx, MessageProtocol msg, ByteBuf out) throws Exception {
		out.writeInt(msg.getContentLength());
		// 3.写入消息的内容(byte[]类型)
		out.writeBytes(msg.getContent());
	}
}
