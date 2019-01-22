package com.atao.dftt.thread;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atao.dftt.model.JkdttUser;
import com.atao.dftt.service.JkdttUserWyService;

public class JkdttReadCoinThread extends Thread {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private JkdttUserWyService jkdttUserWyService;
	private Date endTime;
	private JkdttUser user;

	public JkdttReadCoinThread() {

	}

	public JkdttReadCoinThread(Date endTime, JkdttUser user, JkdttUserWyService jkdttUserWyService) {
		super();
		this.endTime = endTime;
		this.user = user;
		this.jkdttUserWyService = jkdttUserWyService;
	}

	@Override
	public void run() {
		try {
			jkdttUserWyService.readNewsCoin(user, endTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
