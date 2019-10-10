package com.atao.dftt.quartz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.atao.dftt.model.HsttUser;
import com.atao.dftt.service.AsynService;
import com.atao.dftt.service.HsttCoinRecordWyService;
import com.atao.dftt.service.HsttUserWyService;

//@Component
public class HsttReadJob {
	private static Logger logger = LoggerFactory.getLogger(HsttReadJob.class);

	@Resource
	private HsttUserWyService hsttUserWyService;
	@Resource
	private HsttCoinRecordWyService hsttCoinRecordWyService;
	@Resource
	private AsynService asynService;

	/*@Scheduled(cron = "0 11,22,33,44,55 6-9,11-23 * * ?")
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
	}*/
	
	@Scheduled(cron = "0 1/11 6-9,11-23 * * ?")
	public void readMayittNews() throws Exception {
		logger.info("hstt:开始阅读新闻金币任务");
		List<HsttUser> users = hsttUserWyService.getUsedUser();
		List<Future<Integer>> futures = new ArrayList<Future<Integer>>();
		for (HsttUser user : users) {
			Future<Integer> future = asynService.readHsttNews(user);
			futures.add(future);
		}
		for (Future<Integer> future : futures) {
			future.get();
		}
		logger.info("hstt:结束阅读新闻金币任务");
	}

	//@Scheduled(cron = "0 11,22,33,44,55 10-23 * * ?")
	/*public void readHsttVideo() {
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
	}*/

	@Scheduled(cron = "0 0/20 12,15,18 * * ?")
	public void readHsttHotWord() throws Exception {
		logger.info("hstt:开始搜索任务");
		List<HsttUser> users = hsttUserWyService.getUsedUser();
		List<Future<Integer>> futures = new ArrayList<Future<Integer>>();
		for (HsttUser user : users) {
			Future<Integer> future = asynService.searchTask(user);
			futures.add(future);
		}
		for (Future<Integer> future : futures) {
			future.get();
		}
		logger.info("hstt:结束搜索任务");
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

	@Scheduled(cron = "0 06 9 * * ?")
	public void cointx() {
		List<HsttUser> users = hsttUserWyService.getUsedUser();
		for (HsttUser user : users) {
			if (user.getAutoTx())
				hsttUserWyService.cointx(user);
		}
	}

}