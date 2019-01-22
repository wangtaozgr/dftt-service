package com.atao.dftt.thread;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atao.dftt.model.ZqttUser;
import com.atao.dftt.service.ZqttUserWyService;

public class ZqttReadCoinThread extends Thread {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private ZqttUserWyService zqttUserWyService;
	private Date endTime;
	private ZqttUser user;
	private int type;

	public ZqttReadCoinThread() {

	}

	public ZqttReadCoinThread(Date endTime, ZqttUser user, ZqttUserWyService zqttUserWyService, int type) {
		super();
		this.endTime = endTime;
		this.user = user;
		this.zqttUserWyService = zqttUserWyService;
		this.type = type;
	}

	@Override
	public void run() {
		try {
			if (type == 0) {
				zqttUserWyService.readNewsCoin(user, endTime);
			} else {
				zqttUserWyService.readVideoCoin(user, endTime);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
