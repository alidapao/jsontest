package com.controller.client;

public class CheckWifiTask implements Runnable {
	
	private String tempStr;
	
	CheckWifiTask(String tempStr){
		this.tempStr = tempStr;
		
	}
	@Override
	public void run() {
		int count = 0;
		// TODO Auto-generated method stub
		while (true) {
			count++;
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (Utils.ctxTemp != null) {
				Utils.sendMessage(tempStr, Utils.ctxTemp);
				break;
			}
			if (count > 3) {
				Utils.sendMessage("{}", Utils.ctxTemp);
				break;
			}
		}
	}

}
