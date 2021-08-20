package com.controller.client;

import com.controller.client.MessageProtocol;
import com.google.gson.JsonObject;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

public class TcpClientHandler extends ChannelInboundHandlerAdapter {
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			MessageProtocol body = (MessageProtocol) msg;
			String revmsg = body.toString();
			// 对接收的数据进行检验
			System.out.println("客户端接收到的数据是:---------------" + revmsg);
			if (revmsg.contains("startTask")) {

			} else if (revmsg.contains("sendSuccess")) {

				ctx.flush();
			} else if (revmsg.contains("cancelTask")) {

			} else if (revmsg.contains("checkRoute")) {

			} else if (revmsg.contains("checkWifi")) {

			} else if (revmsg.contains("PONG")) {
				ctx.flush();
				System.out.println("收到服务器心跳返回:PONG");
			}

			// else{
			// //其他一律关掉
			// ctx.close();
			// }
			//
		} finally {
			// 读取或手动创建的ByteBuf 需要手动释放
			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("客户端状态为==============channelActive");
		Utils.shutdown = false;
		Utils.ctxTemp = ctx;
		System.out.println("客户端消息发送成功");
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		// TODO Auto-generated method stub
		super.userEventTriggered(ctx, evt);
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent e = (IdleStateEvent) evt;
			switch (e.state()) {
			case READER_IDLE:
				System.out.println("client Read timeout.....");
				ctx.pipeline().remove(this);
				ctx.close();
			
				break;
			default:
				break;
			}

		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("客户端状态为==============channelInactive");
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("客户端状态为==============channelRegistered");
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		Utils.ctxTemp = null;
		System.out.println("客户端状态为==============channelUnregistered");
	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		System.out.println("客户端状态为==============channelWritabilityChanged");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("进入异常处理逻辑");
		super.exceptionCaught(ctx, cause);
		ctx.close();
		Utils.ctxTemp = null;
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
	
		// TODO Auto-generated method stub
		System.out.println("客户端状态为===============handlerRemoved");
		super.handlerRemoved(ctx);
		ctx.pipeline().remove(this);
		ctx.close();
		Utils.ctxTemp = null;
	}
	
}
