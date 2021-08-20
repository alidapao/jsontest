package com.json.test;


import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import com.controller.httpclient.HttpClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class JsonTest {
	/******
	 * 从本地配置文件里面取值，然后存到hashmap中去，并且组装成新的json
	 */
	public static HashMap<String, String> CONFIG_MAP = new HashMap<String, String>();
	public static HashMap<String, String> INFO_MAP = new HashMap<String, String>();
	public static  String CONFIG_FILE_PATH = "D:\\test1\\properties\\config.properties";
	public static void ini() {
		// 初始化测试项名称集合
		propToMap(CONFIG_MAP, CONFIG_FILE_PATH);
	}	
	public static void propToMap(HashMap<String, String> tempMap, String filePath) {
		Properties properties = new Properties();
		InputStream inp = null;
		try {

			inp = new BufferedInputStream(new FileInputStream(filePath));
			properties.load(inp);
			Set<Object> keys = properties.keySet();
			//遍历所有的key
			for (Object object : keys) {
				// 将object强转成String
				String key = (String) object;
				// 通过key获取到相对应的值
				/*String value = properties.getProperty("name");
				System.out.println("value is:"+value);*/
				//System.out.println(key.toString() + "=" + properties.get(key));
				//往map里面添加是无序的
				tempMap.put(key,(String) properties.get(key));
				//System.out.println(tempMap);
			}
		
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
	public static void main(String[] args) {
		
	
	/*********
	 * 获取数组中对应的key值
	 */
	String test="{\"data\":{\"Devices\":[{\"IPAddress\":\"192.168.1.3\",\"STAIPv6IPAddress\":\"::\",\"HostName\":\"DESKTOP-L52LT7B\",\"MacAddress\":\"00E35C683ED1\",\"VMacAddress\":\"00E35C683ED1\",\"STAType\":\"pc\",\"UpTime\":\"50\",\"Radio\":\"\",\"SSID\":\"\",\"RSSI\":\"\",\"RxRate\":\"1000\",\"TxRate\":\"1000\",\"RxRate_rt\":\"0.007\",\"TxRate_rt\":\"0.000\"},{\"IPAddress\":\"192.168.1.5\",\"STAIPv6IPAddress\":\"fe80::a57a:f687:bdc6:6559\",\"HostName\":\"DESKTOP-OOR70UV\",\"MacAddress\":\"00E04C563F47\",\"VMacAddress\":\"00E04C563F47\",\"STAType\":\"pc\",\"UpTime\":\"51385\",\"Radio\":\"\",\"SSID\":\"\",\"RSSI\":\"\",\"RxRate\":\"1000\",\"TxRate\":\"1000\",\"RxRate_rt\":\"0.003\",\"TxRate_rt\":\"0.001\"},{\"IPAddress\":\"192.168.1.11\",\"STAIPv6IPAddress\":\"fe80::2062:d82f:e1b0:d51e\",\"HostName\":\"DESKTOP-L52LT7B\",\"MacAddress\":\"A46BB640306C\",\"VMacAddress\":\"A46BB640306C\",\"STAType\":\"pc\",\"UpTime\":\"6\",\"Radio\":\"5G\",\"SSID\":\"CMCC-FYC9-5G\",\"RSSI\":\"-83\",\"RxRate\":\"28\",\"TxRate\":\"195\",\"RxRate_rt\":\"0.003\",\"TxRate_rt\":\"0.008\"}],\"STANumber\":3},\"deviceId\":\"CMCC-30103-C08F20236459\",\"timestamp\":1628835813221,\"eventType\":\"XData_STAInfo\",\"seqId\":\"4595b0e4\"}";
	
	JsonParser parser=new JsonParser();  //创建JSON解析器
	 JsonObject object=(JsonObject) parser.parse(test);  //创建JsonObject对象   
	 //取出来data的内容
	 JsonObject rootObject = object.getAsJsonObject("data");
	 //取出数组
	 JsonArray jsonArray = rootObject.get("Devices").getAsJsonArray();
	 //取出数组的第一个元素
	 JsonElement tempJs=jsonArray.get(0);
	 //JsonElement转string
	 String str=tempJs.toString();
	 //string转jsonObj
	 JsonObject temps=Utils.jsonObj(str);
	 String IPAddress=temps.get("IPAddress").getAsString();
	 String HostName=temps.get("HostName").getAsString();
	 String MacAddress=temps.get("MacAddress").getAsString();
	 String SSID=temps.get("SSID").getAsString();
	 String TxRate=temps.get("TxRate").getAsString();
	 
	 System.out.println("IPAddress="+IPAddress);
	 
	 /*******
	  * 获取对应json字符串中的值
	  */ 
	 String resultStr = "{\"status\": 0,\"message\": \"query ok\","+
			 "\"result\": {\"address\": \"北京市海淀区镜桥\","+
			 "\"address_component\": {\"nation\": \"中国\",\"province\": \"北京市\","+
			 "\"city\": \"北京市\",\"district\": \"海淀区\","+
			 "\"street\": \"镜桥\",\"street_number\": \"镜桥\"}}}";
			 
	 JsonParser jp = new JsonParser();
	 //将json字符串转化成json对象
	 JsonObject jo = jp.parse(resultStr).getAsJsonObject();
	 //获取message对应的值
	 String message = jo.get("message").getAsString();
	 System.out.println("message：" + message);
	 //获取address对应的值
	 String address = jo.get("result").getAsJsonObject().get("address").getAsString();
	 System.out.println("address：" + address);
	 //获取city对应的值
	 String nation = jo.get("result").getAsJsonObject().get("address_component")
	 .getAsJsonObject().get("nation").getAsString();
	 System.out.println("nation：" + nation);
	 
	 /*******
	  **
	  **
	  **拼接结果,并将结果存到INFO这张hashmap中去***
	  ***/
	 JsonObject detailInoJs = new JsonObject();
	 detailInoJs.addProperty("IPAddress", IPAddress);
	 INFO_MAP.put("IPAddress", IPAddress);
	 detailInoJs.addProperty("HostName", HostName);
	 INFO_MAP.put("HostName", HostName);
	 detailInoJs.addProperty("MacAddress", MacAddress);
	 INFO_MAP.put("MacAddress", MacAddress);
	 detailInoJs.addProperty("SSID", SSID);
	 INFO_MAP.put("SSID", SSID);
	 detailInoJs.addProperty("TxRate",TxRate);
	 INFO_MAP.put("TxRate", TxRate);
	 System.out.println("detailInoJs:"+detailInoJs);
	 
	 /********
	  * 抓取流，获取特定的报文里面的字段
	  */
	 
	 /*****
	  * 从配置文件中拿一个值再进行结果的拼接, 从本地配置文件里面取值，然后存到hashmap中去，再从hashmap中取出来，并且组装成新的json
	  */	
	  ini();
	  String name=CONFIG_MAP.get("name");
	  //System.out.println(name);
	  detailInoJs.addProperty("name",name);
	  INFO_MAP.put("name", name);
	  System.out.println("detailInoJs:"+detailInoJs);
	  
	  /**********
	   * 将拼接好的json传送到本地数据库
	   */
	  TestMysql.Mysql();
	  /**********
	   * 将拼接好的json传送到我搭建的服务器，并进行解析，写好服务器端代码，对获取的json重新进行拼接
	   */
	  
	  HttpClient client = new HttpClient();
	  String jsonStr = detailInoJs.toString();
	  System.out.println("jsonStr is:"+jsonStr);
	
	  /*try {
		client.connect("112.13.92.148", 6060, "/testInterface", jsonStr);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}*/
	  /********
	   * 起10个线程，分别计算
	   * 
	   */
	  
	  /*****
	   * 
	   * 
	   * 提交自己的代码到git
	   */
	}
}

