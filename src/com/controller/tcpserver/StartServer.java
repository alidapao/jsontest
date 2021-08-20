package com.controller.tcpserver;

import com.controller.httpserver.HttpServer;

public class StartServer {
	public static void main(String[] args) {
		TcpServer tcpServer = new TcpServer(9000);
		HttpServer httpServer = new HttpServer(8000);
		Thread th1 = new Thread(tcpServer);
		Thread th2 = new Thread(httpServer);
		th1.start();
		th2.start();
		System.out.println("TCP&http server 启动成功");
	}
}
