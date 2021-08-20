package com.controller.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class TcpHeart implements Runnable {
	
SimpleDateFormat time = Utils.time;
	@Override
	public void run() {
		time.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		while (true) {
			try {
				
				boolean status = false;
				try {
					status = InetAddress.getByName(Utils.serverAdress).isReachable(3000);
				} catch (UnknownHostException e2) {
					System.out.println("服务器地址不可达,请检查网络状态........");
					e2.printStackTrace();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				if (!Utils.shutdown && status && (null != Utils.ctxTemp)) {
					System.out.println(time.format(new java.util.Date()) + "  准备发送心跳PING...........");
					try {
						Utils.sendMessage( ":PING", Utils.ctxTemp);
					} catch (Exception e) {
						System.out.println(time.format(new java.util.Date())+" ping 发送失败.........");

					}
				} else if (Utils.shutdown && status && (null == Utils.ctxTemp)) {
					System.out.println("准备重新建立TCP链接.........");
					TcpClient tcpClient = new TcpClient(Utils.serverAdress, Utils.port);
					Thread th1 = new Thread(tcpClient);
					th1.start();
					Thread.sleep(2000);
					continue;
				}
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
