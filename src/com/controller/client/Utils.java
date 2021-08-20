package com.controller.client;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Properties;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.netty.channel.ChannelHandlerContext;

public class Utils {
	public static SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static ChannelHandlerContext ctxTemp = null;
	public static boolean shutdown = false;
	public static String serverAdress = "";
	public static int port = 0;

	public static void sendMessage(String message, ChannelHandlerContext ctx) {
		try {
			MessageProtocol response = new MessageProtocol(message.getBytes().length, message.getBytes());
			ctx.channel().writeAndFlush(response);
		} catch (Exception e) {
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

	public static String transToJsonStr(Object obj) {
		try {
			Gson gson = new GsonBuilder().disableHtmlEscaping().create(); // 防止有的字符串被转为Unicode
			String json = gson.toJson(obj);
			return json;
		} catch (Exception e) {
			return "";
		}
	}


	public static void iniClient() {
		Properties prop = new Properties();
		InputStream inp = null;
		try {
			inp = new BufferedInputStream (new FileInputStream("d:\\Debug\\info.properties"));
			prop.load(inp);
			Utils.serverAdress = prop.getProperty("serverAdress").trim();
			Utils.port = Integer.valueOf(prop.getProperty("port").trim());
			inp.close();
			prop.clear();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
	}





}
