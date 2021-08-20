package com.json.test;



import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Utils {
	//json获取值
	public static String  getValue(JsonObject js,String rootNode1,String rootNode2 ,String key){
		String value = "error";
		try{
			//value = js.get(rootNode).getAsJsonObject().get(key).getAsString();
			value=js.get(rootNode1).getAsJsonObject().get(rootNode2).getAsJsonObject().get(key).getAsString();
			
		}catch(Exception e){
			
			System.out.println("<--------------获取"+rootNode1+"和"+rootNode2+"下的"+key+"异常------------>");
		}
		return value;
	}
	
	public static String  getValue1(JsonObject js,String rootNode,String key){
		String value = "error";
		try{
			value = js.get(rootNode).getAsJsonObject().get(key).getAsString();
			
			
		}catch(Exception e){
			
			System.out.println("<--------------获取"+rootNode+"下的"+key+"异常------------>");
		}
		return value;
	}
	public static JsonObject jsonObj(String jsonString) {
		JsonObject tempJson;
		try {
			tempJson = new JsonParser().parse(jsonString).getAsJsonObject();
			System.out.println("json ok######################");
			return tempJson;
		} catch (Exception e) {
			System.out.println("json fail######################");
			tempJson = new JsonParser().parse("{}").getAsJsonObject();
			return tempJson;
		}
	}
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
			System.out.println("dest:"+dest);
		}
		return dest;
	}
	
	
}
