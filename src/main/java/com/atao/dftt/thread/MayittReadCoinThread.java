package com.atao.dftt.thread;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atao.dftt.model.MayittUser;
import com.atao.dftt.service.MayittUserWyService;

public class MayittReadCoinThread extends Thread {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private MayittUserWyService mayittUserWyService;
	private Date endTime;
	private MayittUser user;
	private int type;

	public MayittReadCoinThread() {

	}

	public MayittReadCoinThread(Date endTime, MayittUser user, MayittUserWyService mayittUserWyService, int type) {
		super();
		this.endTime = endTime;
		this.user = user;
		this.mayittUserWyService = mayittUserWyService;
		this.type = type;
	}

	@Override
	public void run() {
		try {
			if (type == 0) {
				mayittUserWyService.readNewsCoin(user, endTime);
			} else {
				mayittUserWyService.readVideoCoin(user, endTime);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
