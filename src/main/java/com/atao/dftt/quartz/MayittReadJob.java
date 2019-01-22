package com.atao.dftt.quartz;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.atao.dftt.model.MayittUser;
import com.atao.dftt.service.MayittCoinRecordWyService;
import com.atao.dftt.service.MayittUserWyService;
import com.atao.dftt.thread.MayittReadCoinThread;
import com.atao.dftt.thread.MayittReadTaskCoinThread;

@Component
public class MayittReadJob {
	private static Logger logger = LoggerFactory.getLogger(MayittReadJob.class);

	@Resource
	private MayittUserWyService mayittUserWyService;
	@Resource
	private MayittCoinRecordWyService mayittCoinRecordWyService;

	@Scheduled(cron = "0 11,22,33,44,55 6-9,11-23 * * ?")
	public void readMayittNews() {
		List<MayittUser> users = mayittUserWyService.getUsedUser();
		for (MayittUser user : users) {
			Date endTime = new Date(new Date().getTime() + 10 * 60 * 1000l);
			MayittReadCoinThread thread = new MayittReadCoinThread(endTime, user, mayittUserWyService, 0);
			thread.start();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Scheduled(cron = "0 11,22,33,44,55 10-23 * * ?")
	public void readMayittVideo() {
		List<MayittUser> users = mayittUserWyService.getUsedUser();
		for (MayittUser user : users) {
			Date endTime = new Date(new Date().getTime() + 10 * 60 * 1000l);
			MayittReadCoinThread thread = new MayittReadCoinThread(endTime, user, mayittUserWyService, 1);
			thread.start();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 每小时打一次卡
	 */
	@Scheduled(cron = "0 4 6-23 * * ?")
	public void daka() {
		List<MayittUser> users = mayittUserWyService.getUsedUser();
		for (MayittUser user : users) {
			mayittUserWyService.daka(user);
		}

	}

	/**
	 * 查询我的金币
	 */
	@Scheduled(cron = "0 0 12,18 * * ?")
	public void queryMyCoin() {
		mayittCoinRecordWyService.updateAllCoin();
	}

	@Scheduled(cron = "0 0 11,17,20 * * ?")
	public void readAdTask() {
		List<MayittUser> users = mayittUserWyService.getUsedUser();
		for (MayittUser user : users) {
			MayittReadTaskCoinThread thread = new MayittReadTaskCoinThread(user, mayittUserWyService, 0);
			thread.start();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Scheduled(cron = "0 0 14,20 * * ?")
	public void readRwTask() {
		List<MayittUser> users = mayittUserWyService.getUsedUser();
		for (MayittUser user : users) {
			MayittReadTaskCoinThread thread = new MayittReadTaskCoinThread(user, mayittUserWyService, 1);
			thread.start();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Scheduled(cron = "0 0 17,20 * * ?")
	public void readMoreTask() {
		List<MayittUser> users = mayittUserWyService.getUsedUser();
		for (MayittUser user : users) {
			MayittReadTaskCoinThread thread = new MayittReadTaskCoinThread(user, mayittUserWyService, 2);
			thread.start();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Scheduled(cron = "0 10 9 * * ?")
	public void cointx() {
		List<MayittUser> users = mayittUserWyService.getUsedUser();
		for (MayittUser user : users) {
			if (user.getAutoTx())
				mayittUserWyService.cointx(user);
		}
	}
	
}