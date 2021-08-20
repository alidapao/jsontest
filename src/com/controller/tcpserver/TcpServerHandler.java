package com.controller.tcpserver;

import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Map.Entry;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

public class TcpServerHandler extends ChannelInboundHandlerAdapter {
	SimpleDateFormat time = Utils.time;
	// private static final ChannelGroup channelGroup = new
	// DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		time.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		// 用于获取客户端发来的数据信息
		MessageProtocol body = (MessageProtocol) msg;
		String revmsg = body.toString();
		// 对接收的数据进行检验
		System.out.println(time.format(new java.util.Date())+" 接收到客户端消息:" + revmsg);

		if (revmsg.contains("PING")) {
			Utils.sendMessage("PONG", ctx);
		}else if (revmsg.contains("connect:")) {
			Utils.onlineMap.put(revmsg.split(":")[1], ctx);
			ctx.flush();
		}else{
			ctx.flush();
		}
		// 读取或手动创建的ByteBuf 需要手动释放
		ReferenceCountUtil.release(body);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		System.out.println(channel.remoteAddress() + "建立连接涌道为活跃状态");
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		System.out.println(channel.remoteAddress() + "加入");
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		System.out.println("进入remove handler........");
		// ctx.handler().handlerRemoved(ctx);
		// 移除离线设备
		String mac = "";
		for (Entry<String, ChannelHandlerContext> entry : Utils.onlineMap.entrySet()) {
			mac = entry.getKey().toString();
			if (Utils.onlineMap.get(mac) == ctx) {
				Utils.onlineMap.remove(mac);
				break;
			}
		}
		super.handlerRemoved(ctx);
		ctx.close();
		System.out.println("检测到" + mac + "断开连接....................");
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent e = (IdleStateEvent) evt;
			switch (e.state()) {
			case READER_IDLE:
				System.out.println("Server Read timeout.....");
				ctx.close();
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("进入异常处理逻辑......");
		ctx.close();
	}
}
