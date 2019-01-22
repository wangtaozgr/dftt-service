package com.atao.dftt.quartz;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.atao.dftt.model.ZqttUser;
import com.atao.dftt.service.ZqttCoinRecordWyService;
import com.atao.dftt.service.ZqttUserWyService;
import com.atao.dftt.thread.ZqttReadCoinThread;

@Component
public class ZqttReadJob {
	private static Logger logger = LoggerFactory.getLogger(ZqttReadJob.class);

	@Resource
	private ZqttUserWyService zqttUserWyService;
	@Resource
	private ZqttCoinRecordWyService zqttCoinRecordWyService;

	@Scheduled(cron = "0 11,22,33,44,55 6-9,11-23 * * ?")
	public void readZqttNews() {
		List<ZqttUser> users = zqttUserWyService.getUsedUser();
		for (ZqttUser user : users) {
			Date endTime = new Date(new Date().getTime() + 10 * 60 * 1000l);
			ZqttReadCoinThread thread = new ZqttReadCoinThread(endTime, user, zqttUserWyService, 0);
			thread.start();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	//@Scheduled(cron = "0 11,22,33,44,55 10-23 * * ?")
	public void readZqttVideo() {
		List<ZqttUser> users = zqttUserWyService.getUsedUser();
		for (ZqttUser user : users) {
			Date endTime = new Date(new Date().getTime() + 10 * 60 * 1000l);
			ZqttReadCoinThread thread = new ZqttReadCoinThread(endTime, user, zqttUserWyService, 1);
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
	//@Scheduled(cron = "0 4 6-23 * * ?")
	public void daka() {
		List<ZqttUser> users = zqttUserWyService.getUsedUser();
		for (ZqttUser user : users) {
			zqttUserWyService.daka(user);
		}

	}

	/**
	 * 查询我的金币
	 */
	@Scheduled(cron = "0 0 12,18 * * ?")
	public void queryMyCoin() {
		zqttCoinRecordWyService.updateAllCoin();
	}

	@Scheduled(cron = "0 8 8 * * ?")
	public void cointx() {
		List<ZqttUser> users = zqttUserWyService.getUsedUser();
		for (ZqttUser user : users) {
			if (user.getAutoTx())
				zqttUserWyService.cointx(user);
		}
	}

}