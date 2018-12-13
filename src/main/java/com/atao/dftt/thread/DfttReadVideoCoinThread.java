package com.atao.dftt.thread;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atao.dftt.model.DfToutiaoUser;
import com.atao.dftt.service.DfToutiaoUserWyService;

public class DfttReadVideoCoinThread extends Thread {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private DfToutiaoUserWyService dfToutiaoUserWyService;
	private Date endTime;
	private DfToutiaoUser user;

	public DfttReadVideoCoinThread() {

	}

	public DfttReadVideoCoinThread(Date endTime, DfToutiaoUser user, DfToutiaoUserWyService dfToutiaoUserWyService) {
		super();
		this.endTime = endTime;
		this.user = user;
		this.dfToutiaoUserWyService = dfToutiaoUserWyService;
	}

	@Override
	public void run() {
		try {
			dfToutiaoUserWyService.readVideoCoin(user, endTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
