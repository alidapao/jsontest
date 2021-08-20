package com.controller.httpserver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

public class ByteBufToBytes {
	private ByteBuf temp;
	private boolean end = true;

	public ByteBufToBytes(int length) {
		temp = Unpooled.buffer(length);
	}

	public void reading(ByteBuf datas, ChannelHandlerContext ctx) {
		datas.readBytes(temp, datas.readableBytes());
		if (this.temp.writableBytes() != 0) {
			end = false;
		} else {
			if (temp.readableBytes() > 50000) {
				System.out.println(HttpServerInboundHandler.time.format(new java.util.Date()) + "传入数据大于50000字节丢弃");
				temp.skipBytes(temp.readableBytes());
				ctx.close();
			}
			end = true;
		}
	}

	public boolean isEnd() {
		return end;
	}

	public byte[] readFull() {
		if (end) {
			byte[] contentByte = new byte[this.temp.readableBytes()];
			this.temp.readBytes(contentByte);
			this.temp.release();
			return contentByte;
		} else {
			return null;
		}
	}

	public byte[] read(ByteBuf datas) {
		byte[] bytes = new byte[datas.readableBytes()];
		datas.readBytes(bytes);
		return bytes;
	}
}