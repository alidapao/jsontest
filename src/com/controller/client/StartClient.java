package com.controller.client;

public class StartClient {


	public static void main(String[] args) {
		if(args.length >0){
			if(args[0].equals("v")){
				System.out.print("1.0");	
			}else{
				System.out.print("illegal parameter");	
			}
			return;
		}
		Utils.iniClient();
		System.out.println("初始化信息:serverAdress/port");
		System.out.println(Utils.serverAdress+"/"+Utils.port);
		TcpClient tcpClient = new TcpClient(Utils.serverAdress, Utils.port);
		TcpHeart tcpHeart = new TcpHeart();
		Thread th1 = new Thread(tcpClient);
		Thread th2 = new Thread(tcpHeart);
		th1.start();
		th2.start();
	}
}
