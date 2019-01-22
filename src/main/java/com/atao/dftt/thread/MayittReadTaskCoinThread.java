package com.atao.dftt.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atao.dftt.model.MayittUser;
import com.atao.dftt.service.MayittUserWyService;

public class MayittReadTaskCoinThread extends Thread {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private MayittUserWyService mayittUserWyService;
	private MayittUser user;
	private int type;

	public MayittReadTaskCoinThread() {

	}

	public MayittReadTaskCoinThread(MayittUser user, MayittUserWyService mayittUserWyService, int type) {
		super();
		this.user = user;
		this.mayittUserWyService = mayittUserWyService;
		this.type = type;
	}

	@Override
	public void run() {
		try {
			if (type == 0) {
				mayittUserWyService.readAdTask(user);
			} else if (type == 1) {
				mayittUserWyService.readRwTask(user);
				mayittUserWyService.readHongbao(user);
			} else {
				mayittUserWyService.readMoreTask(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
