package com.controller.client;

import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class MyDecoder extends ByteToMessageDecoder {
	public final int BASE_LENGTH = 1;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
		// 可读长度必须大于基本长度
		if (buffer.readableBytes() >= BASE_LENGTH) {
			// 防止socket字节流攻击
			// 防止，客户端传来的数据过大
			// 因为，太大的数据，是不合理的
			if (buffer.readableBytes() > 10000) {
				buffer.skipBytes(buffer.readableBytes());
			}
			// 记录包头开始的index
			int beginReader = buffer.readerIndex();
			int length = buffer.readInt();
			// 判断请求数据包数据是否到齐
			if (buffer.readableBytes() != length) {
				// 还原读指针
				buffer.readerIndex(beginReader);
				return;
			}
			// 读取data数据
			byte[] data = new byte[length];
			buffer.readBytes(data);
			MessageProtocol protocol = new MessageProtocol(data.length, data);
			out.add(protocol);
		}
	}
}