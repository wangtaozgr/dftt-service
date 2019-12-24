package com.atao.dftt.quartz;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.atao.dftt.model.MayittUser;
import com.atao.dftt.service.AsynService;
import com.atao.dftt.service.MayittCoinRecordWyService;
import com.atao.dftt.service.MayittUserWyService;

//@Component
public class MayittReadJob {
	private static Logger logger = LoggerFactory.getLogger(MayittReadJob.class);

	@Resource
	private MayittUserWyService mayittUserWyService;
	@Resource
	private MayittCoinRecordWyService mayittCoinRecordWyService;
	@Resource
	private AsynService asynService;

	@Scheduled(cron = "0 11,22,33,44,55 6-9,11-23 * * ?")
	public void readMayittNews() throws Exception {
		logger.info("mayitt:开始阅读新闻金币任务");
		List<MayittUser> users = mayittUserWyService.getUsedUser();
		List<Future<Integer>> futures = new ArrayList<Future<Integer>>();
		for (MayittUser user : users) {
			Future<Integer> future = asynService.readMayittNews(user);
			futures.add(future);
		}
		for (Future<Integer> future : futures) {
			future.get();
		}
		logger.info("mayitt:结束阅读新闻金币任务");
	}

	@Scheduled(cron = "0 11,22,33,44,55 10-23 * * ?")
	public void readMayittVideo() throws Exception {
		logger.info("mayitt:开始阅读视频金币任务");
		List<MayittUser> users = mayittUserWyService.getUsedUser();
		List<Future<Integer>> futures = new ArrayList<Future<Integer>>();
		for (MayittUser user : users) {
			Future<Integer> future = asynService.readMayittVideo(user);
			futures.add(future);
		}
		for (Future<Integer> future : futures) {
			future.get();
		}
		logger.info("mayitt:结束阅读视频金币任务");
	}

	/*@Scheduled(cron = "0 0,10,20,30,40,50 13 * * ?")
	public void readAdTask() throws Exception {
		logger.info("mayitt:开始阅读广告金币任务");
		List<MayittUser> users = mayittUserWyService.getUsedUser();
		List<Future<Integer>> futures = new ArrayList<Future<Integer>>();
		for (MayittUser user : users) {
			Future<Integer> future = asynService.readMayittAdTask(user);
			futures.add(future);
		}
		for (Future<Integer> future : futures) {
			future.get();
		}
		logger.info("mayitt:结束阅读广告金币任务");
	}*/

	@Scheduled(cron = "0 0,10,20,30,40,50 16 * * ?")
	public void readRwTask() throws Exception {
		logger.info("mayitt:开始阅读热文金币任务");
		List<MayittUser> users = mayittUserWyService.getUsedUser();
		List<Future<Integer>> futures = new ArrayList<Future<Integer>>();
		for (MayittUser user : users) {
			Future<Integer> future = asynService.readMayittRwTask(user);
			futures.add(future);
		}
		for (Future<Integer> future : futures) {
			future.get();
		}
		logger.info("mayitt:结束阅读热文金币任务");
	}

	@Scheduled(cron = "0 0,10,20,30,40,50 19 * * ?")
	public void readMoreTask() throws Exception {
		logger.info("mayitt:开始阅读更多金币任务");
		List<MayittUser> users = mayittUserWyService.getUsedUser();
		List<Future<Integer>> futures = new ArrayList<Future<Integer>>();
		for (MayittUser user : users) {
			Future<Integer> future = asynService.readMayittMoreTask(user);
			futures.add(future);
		}
		for (Future<Integer> future : futures) {
			future.get();
		}
		logger.info("mayitt:结束阅读更多金币任务");
	}

	/*@Scheduled(cron = "0 0,10,20,30,40,50 22 * * ?")
	public void readMayittHongbaoTask() throws Exception {
		logger.info("mayitt:开始阅读红包金币任务");
		List<MayittUser> users = mayittUserWyService.getUsedUser();
		List<Future<Integer>> futures = new ArrayList<Future<Integer>>();
		for (MayittUser user : users) {
			Future<Integer> future = asynService.readMayittHongbaoTask(user);
			futures.add(future);
		}
		for (Future<Integer> future : futures) {
			future.get();
		}
		logger.info("mayitt:结束阅读红包金币任务");
	}*/

	/**
	 * 查询我的金币
	 */
	@Scheduled(cron = "0 0 12,18 * * ?")
	public void queryMyCoin() {
		mayittCoinRecordWyService.updateAllCoin();
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

	@Scheduled(cron = "0 10 9 * * ?")
	public void cointx() {
		List<MayittUser> users = mayittUserWyService.getUsedUser();
		for (MayittUser user : users) {
			if (user.getAutoTx())
				mayittUserWyService.cointx(user);
		}
	}

}