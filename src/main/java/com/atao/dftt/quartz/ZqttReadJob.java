package com.atao.dftt.quartz;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.atao.dftt.model.ZqttUser;
import com.atao.dftt.service.AsynService;
import com.atao.dftt.service.ZqttCoinRecordWyService;
import com.atao.dftt.service.ZqttUserWyService;

@Component
public class ZqttReadJob {
	private static Logger logger = LoggerFactory.getLogger(ZqttReadJob.class);

	@Resource
	private ZqttUserWyService zqttUserWyService;
	@Resource
	private ZqttCoinRecordWyService zqttCoinRecordWyService;
	@Resource
	private AsynService asynService;
	
	@Scheduled(cron = "0 2/13 6-9,11-23 * * ?")
	public void readZqttNews() throws Exception {
		logger.info("zqtt:开始阅读新闻金币任务");
		List<ZqttUser> users = zqttUserWyService.getUsedUser();
		List<Future<Integer>> futures = new ArrayList<Future<Integer>>();
		for (ZqttUser user : users) {
			Future<Integer> future = asynService.readZqttNews(user);
			futures.add(future);
		}
		for (Future<Integer> future : futures) {
			future.get();
		}
		logger.info("zqtt:结束阅读新闻金币任务");
	}
	
	@Scheduled(cron = "0 0,10,20,30,40,50 13,17 * * ?")
	public void readZqttKkzTask() throws Exception {
		logger.info("zqtt:开始阅读看看赚金币任务");
		List<ZqttUser> users = zqttUserWyService.getUsedUser();
		List<Future<Integer>> futures = new ArrayList<Future<Integer>>();
		for (ZqttUser user : users) {
			Future<Integer> future = asynService.readZqttKkzTask(user);
			futures.add(future);
		}
		for (Future<Integer> future : futures) {
			future.get();
		}
		logger.info("zqtt:结束阅读看看赚金币任务");
	}
	
	@Scheduled(cron = "0 0,10,20,30,40,50 15,20 * * ?")
	public void searchTask() throws Exception {
		logger.info("zqtt:开始阅读搜索任务金币任务");
		List<ZqttUser> users = zqttUserWyService.getUsedUser();
		List<Future<Integer>> futures = new ArrayList<Future<Integer>>();
		for (ZqttUser user : users) {
			Future<Integer> future = asynService.searchTask(user);
			futures.add(future);
		}
		for (Future<Integer> future : futures) {
			future.get();
		}
		logger.info("zqtt:结束阅读搜索任务金币任务");
	}
	
	/**
	 * 每小时打一次卡
	 */
	@Scheduled(cron = "0 4 6-23 * * ?")
	public void daka() {
		List<ZqttUser> users = zqttUserWyService.getUsedUser();
		for (ZqttUser user : users) {
			zqttUserWyService.daka(user);
		}
	}

	/**
	 * 查询我的金币
	 */
	@Scheduled(cron = "0 0 8,12,18 * * ?")
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