package com.atao.dftt.quartz;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.atao.dftt.model.HsttUser;
import com.atao.dftt.service.HsttCoinRecordWyService;
import com.atao.dftt.service.HsttUserWyService;
import com.atao.dftt.thread.HsttReadCoinThread;

@Component
public class HsttReadJob {
	private static Logger logger = LoggerFactory.getLogger(HsttReadJob.class);

	@Resource
	private HsttUserWyService hsttUserWyService;
	@Resource
	private HsttCoinRecordWyService hsttCoinRecordWyService;

	@Scheduled(cron = "0 11,22,33,44,55 6-9,11-23 * * ?")
	public void readHsttNews() {
		List<HsttUser> users = hsttUserWyService.getUsedUser();
		for (HsttUser user : users) {
			Date endTime = new Date(new Date().getTime() + 20 * 60 * 1000l);
			HsttReadCoinThread thread = new HsttReadCoinThread(endTime, user, hsttUserWyService, 0);
			thread.start();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	//@Scheduled(cron = "0 11,22,33,44,55 10-23 * * ?")
	public void readHsttVideo() {
		List<HsttUser> users = hsttUserWyService.getUsedUser();
		for (HsttUser user : users) {
			Date endTime = new Date(new Date().getTime() + 10 * 60 * 1000l);
			HsttReadCoinThread thread = new HsttReadCoinThread(endTime, user, hsttUserWyService, 1);
			thread.start();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Scheduled(cron = "0 0 12,15,18 * * ?")
	public void readHotword() {
		List<HsttUser> users = hsttUserWyService.getUsedUser();
		for (HsttUser user : users) {
			Date endTime = new Date(new Date().getTime() + 10 * 60 * 1000l);
			HsttReadCoinThread thread = new HsttReadCoinThread(endTime, user, hsttUserWyService, 2);
			thread.start();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Scheduled(cron = "0 0 19 * * ?")
	public void searchreward() throws Exception {
		List<HsttUser> users = hsttUserWyService.getUsedUser();
		for (HsttUser user : users) {
			hsttUserWyService.searchreward(user);
		}
	}

	/**
	 * 每小时打一次卡
	 */
	@Scheduled(cron = "0 4 6-23 * * ?")
	public void daka() {
		List<HsttUser> users = hsttUserWyService.getUsedUser();
		for (HsttUser user : users) {
			hsttUserWyService.daka(user);
		}

	}

	/**
	 * 查询我的金币
	 */
	@Scheduled(cron = "0 0 12,18 * * ?")
	public void queryMyCoin() {
		hsttCoinRecordWyService.updateAllCoin();
	}

	@Scheduled(cron = "0 10 9 * * ?")
	public void cointx() {
		List<HsttUser> users = hsttUserWyService.getUsedUser();
		for (HsttUser user : users) {
			if (user.getAutoTx())
				hsttUserWyService.cointx(user);
		}
	}

}