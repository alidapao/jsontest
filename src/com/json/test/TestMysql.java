package com.json.test;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

public class TestMysql {
	//public static void main(String[] args){
	//static可以供其他类调用
		public static void Mysql(){
			Connection conn;
			PreparedStatement stmt;
			String driver = "com.mysql.jdbc.Driver";//连接MySql数据库
			String url = "jdbc:mysql://localhost:3306/mysql";
			String user = "root";//数据库用户名
			String password = "123456";//数据库密码
			String sql = "insert into jsontestsql(IPAddress,HostName,MacAddress,SSID,TxRate,name) values (?,?,?,?,?,?)";//数据库操作语句（插入）
			
			try {
				Class.forName(driver);//向DriverManager注册自己
				conn = (Connection) DriverManager.getConnection(url, user, password);//与数据库建立连接
				stmt = (PreparedStatement) conn.prepareStatement(sql);//用来执行SQL语句查询，对sql语句进行预编译处理
				stmt.setString(1,JsonTest.INFO_MAP.get("IPAddress"));
				stmt.setString(2,JsonTest.INFO_MAP.get("HostName"));
				stmt.setString(3,JsonTest.INFO_MAP.get("MacAddress"));
				stmt.setString(4,JsonTest.INFO_MAP.get("SSID"));
				stmt.setString(5,JsonTest.INFO_MAP.get("TxRate"));
				stmt.setString(6,JsonTest.INFO_MAP.get("name"));
				
				stmt.executeUpdate();
				
			} catch (ClassNotFoundException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (SQLException e) { //执行与数据库建立连接需要抛出SQL异常
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	 

}
