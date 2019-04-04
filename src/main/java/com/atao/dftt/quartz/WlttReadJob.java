package com.atao.dftt.quartz;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.atao.dftt.model.Wltt;
import com.atao.dftt.service.AsynService;
import com.atao.dftt.service.WlttCoinRecordWyService;
import com.atao.dftt.service.WlttWyService;

@Component
public class WlttReadJob {
	private static Logger logger = LoggerFactory.getLogger(WlttReadJob.class);
	@Resource
	private WlttWyService wlttWyService;
	@Resource
	private WlttCoinRecordWyService wlttCoinRecordWyService;
	@Resource
	private AsynService asynService;

	@Scheduled(cron = "0 1/2 6-8,11-23 * * ?")
	public void readWlttNews() throws Exception {
		logger.info("wltt:开始阅读新闻金币任务");
		List<Wltt> users = wlttWyService.getUsedUser();
		List<Future<Integer>> futures = new ArrayList<Future<Integer>>();
		for (Wltt user : users) {
			Future<Integer> future = asynService.readWlttNews(user);
			futures.add(future);
		}
		for (Future<Integer> future : futures) {
			future.get();
		}
		logger.info("wltt:结束阅读新闻金币任务");
	}
	
	@Scheduled(cron = "0 10,20,30,40,50 13,15,18,21,23 * * ?")
	public void searchTask() throws Exception {
		logger.info("wltt:开始搜索任务");
		List<Wltt> users = wlttWyService.getUsedUser();
		List<Future<Integer>> futures = new ArrayList<Future<Integer>>();
		for (Wltt user : users) {
			Future<Integer> future = asynService.searchTask(user);
			futures.add(future);
		}
		for (Future<Integer> future : futures) {
			future.get();
		}
		logger.info("wltt:结束搜索任务");
	}

	/*public void readVideo() {
		List<Wltt> users = wlttWyService.getUsedUser();
		for (Wltt user : users) {
			Date endTime = new Date(new Date().getTime() + 10 * 60 * 1000l);
			WlttReadVideoThread thread = new WlttReadVideoThread(endTime, user, wlttWyService);
			thread.start();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}*/

	/**
	 * 每小时打一次卡
	 */
	@Scheduled(cron = "0 5 6-23 * * ?")
	public void daka() {
		List<Wltt> users = wlttWyService.getUsedUser();
		for (Wltt user : users) {
			wlttWyService.daka(user);
		}
	}

	/**
	 * 每天完成一次任务
	 */
	@Scheduled(cron = "0 5 12,18,23 * * ?")
	public void queryMyCoin() {
		wlttCoinRecordWyService.updateAllCoin();
	}

	@Scheduled(cron = "11 0 6 * * ?")
	public void cointx() {
		List<Wltt> users = wlttWyService.getUsedUser();
		for (Wltt user : users) {
			if (user.getAutoTx())
				wlttWyService.cointx(user);
		}
	}
}