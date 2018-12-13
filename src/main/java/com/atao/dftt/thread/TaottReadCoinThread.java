package com.atao.dftt.thread;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atao.dftt.model.DfToutiaoUser;
import com.atao.dftt.model.TaoToutiaoUser;
import com.atao.dftt.service.DfToutiaoUserWyService;
import com.atao.dftt.service.TaoToutiaoUserWyService;

public class TaottReadCoinThread extends Thread {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private TaoToutiaoUserWyService taoToutiaoUserWyService;
	private Date endTime;
	private TaoToutiaoUser user;

	public TaottReadCoinThread() {

	}

	public TaottReadCoinThread(Date endTime, TaoToutiaoUser user, TaoToutiaoUserWyService taoToutiaoUserWyService) {
		super();
		this.endTime = endTime;
		this.user = user;
		this.taoToutiaoUserWyService = taoToutiaoUserWyService;
	}

	@Override
	public void run() {
		try {
			taoToutiaoUserWyService.readNewsCoin(user, endTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
