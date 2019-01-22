package com.atao.dftt.thread;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atao.dftt.model.HsttUser;
import com.atao.dftt.service.HsttUserWyService;
import com.atao.dftt.service.HsttUserWyService;

public class HsttReadCoinThread extends Thread {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private HsttUserWyService hsttUserWyService;
	private Date endTime;
	private HsttUser user;
	private int type;

	public HsttReadCoinThread() {

	}

	public HsttReadCoinThread(Date endTime, HsttUser user, HsttUserWyService hsttUserWyService, int type) {
		super();
		this.endTime = endTime;
		this.user = user;
		this.hsttUserWyService = hsttUserWyService;
		this.type = type;
	}

	@Override
	public void run() {
		try {
			if (type == 0) {
				hsttUserWyService.readNewsCoin(user, endTime);
			} else if (type == 1) {
				hsttUserWyService.readVideoCoin(user, endTime);
			} else if (type == 2) {
				hsttUserWyService.readHotWord(user);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
