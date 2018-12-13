package com.atao.dftt.thread;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atao.dftt.model.Wltt;
import com.atao.dftt.service.WlttWyService;

public class WlttReadVideoThread extends Thread {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private WlttWyService wlttWyService;
	private Date endTime;
	private Wltt user;

	public WlttReadVideoThread() {

	}

	public WlttReadVideoThread(Date endTime, Wltt user, WlttWyService wlttWyService) {
		super();
		this.endTime = endTime;
		this.user = user;
		this.wlttWyService = wlttWyService;
	}

	@Override
	public void run() {
		try {
			wlttWyService.readVideoCoin(user, endTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
