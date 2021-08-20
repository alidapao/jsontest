package com.controller.tcpserver;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.FileReader;
//import java.io.IOException;
//import java.rmi.server.ExportException;
import java.text.SimpleDateFormat;
//import java.util.HashSet;
//import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import com.controller.httpclient.HttpClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

//import javax.swing.tree.ExpandVetoException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders.Values;

public class Utils {
	public static ConcurrentHashMap<String, ChannelHandlerContext> onlineMap = new ConcurrentHashMap<String, ChannelHandlerContext>();

	// 对象转为json字符串
	public static String transToJsonStr(Object obj) {
		try {
			Gson gson = new GsonBuilder().disableHtmlEscaping().create(); // 防止有的字符串被转为Unicode
			String json = gson.toJson(obj);
			return json;
		} catch (Exception e) {
			return "";
		}
	}

	public static SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static void sendMessage(String message, ChannelHandlerContext ctx) {
		try {
			MessageProtocol response = new MessageProtocol(message.getBytes().length, message.getBytes());
			ctx.channel().writeAndFlush(response);
		} catch (Exception e) {
			System.out.println("服务器发送消息失败....");
		}
	}

	// json字符串转成json
	public static JsonObject jsonObj(String jsonString) {
		JsonObject tempJson;
		try {
			tempJson = new JsonParser().parse(jsonString).getAsJsonObject();
			return tempJson;
		} catch (Exception e) {
			tempJson = new JsonParser().parse("{}").getAsJsonObject();
			return tempJson;
		}
	}

	// 测试盒子是否在线检测
	public static boolean checkOnline(String mac) {
		if (Utils.onlineMap.get(mac) != null) {
			return true;
		} else {
			return false;
		}
	}



	public static void jsonFail(ChannelHandlerContext ctx, FullHttpResponse response) {
		response = new DefaultFullHttpResponse(HTTP_1_1, OK,
				Unpooled.wrappedBuffer("{\"task\":\"jsonError\"}".getBytes()));
		response.headers().set(CONTENT_TYPE, "text/plain");
		response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
		response.headers().set(CONNECTION, Values.KEEP_ALIVE);
		ctx.write(response);
		ctx.flush();
		ctx.close();
	}


	public static void taskSendSuccess(ChannelHandlerContext ctx, FullHttpResponse response) {
		response = new DefaultFullHttpResponse(HTTP_1_1, OK,
				Unpooled.wrappedBuffer("{\"task\":\"success\"}".getBytes()));
		response.headers().set(CONTENT_TYPE, "text/plain");
		response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
		response.headers().set(CONNECTION, Values.KEEP_ALIVE);
		ctx.write(response);
		ctx.flush();
		ctx.close();
	}


}
