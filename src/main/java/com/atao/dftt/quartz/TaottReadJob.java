package com.atao.dftt.quartz;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.atao.dftt.model.TaoToutiaoUser;
import com.atao.dftt.service.AsynService;
import com.atao.dftt.service.TaoToutiaoUserWyService;
import com.atao.dftt.service.TaottCoinRecordWyService;

@Component
public class TaottReadJob {
	private static Logger logger = LoggerFactory.getLogger(TaottReadJob.class);

	@Resource
	private TaoToutiaoUserWyService taoToutiaoUserWyService;
	@Resource
	private TaottCoinRecordWyService taottCoinRecordWyService;
	@Resource
	private AsynService asynService;

	@Scheduled(cron = "0 14,25,36,47,58 6-8,11-23 * * ?")
	public void readTaottNews() throws Exception {
		logger.info("taott:开始阅读新闻金币任务");
		List<TaoToutiaoUser> users = taoToutiaoUserWyService.getUsedUser();
		List<Future<Integer>> futures = new ArrayList<Future<Integer>>();
		for (TaoToutiaoUser user : users) {
			Future<Integer> future = asynService.readTaottNews(user);
			futures.add(future);
		}
		for (Future<Integer> future : futures) {
			future.get();
		}
		logger.info("taott:结束阅读新闻金币任务");
	}

	/**
	 * 每半小时打一次卡
	 */
	@Scheduled(cron = "0 0/30 6-23 * * ?")
	public void daka() {
		List<TaoToutiaoUser> users = taoToutiaoUserWyService.getUsedUser();
		for (TaoToutiaoUser user : users) {
			taoToutiaoUserWyService.daka(user);
		}

	}

	/**
	 * 每天完成一次任务
	 */
	@Scheduled(cron = "0 5 17,18 * * ?")
	public void checkTask() {
		List<TaoToutiaoUser> users = taoToutiaoUserWyService.getUsedUser();
		for (TaoToutiaoUser user : users) {
			taoToutiaoUserWyService.checkTask(user);
		}
	}

	/**
	 * 查询我的金币
	 */
	@Scheduled(cron = "0 0 12,18 * * ?")
	public void queryMyCoin() {
		taottCoinRecordWyService.updateAllCoin();
	}

	@Scheduled(cron = "0 5 8 * * ?")
	public void cointx() {
		List<TaoToutiaoUser> users = taoToutiaoUserWyService.getUsedUser();
		for (TaoToutiaoUser user : users) {
			if (user.getAutoTx())
				taoToutiaoUserWyService.cointx(user);
		}
	}
}