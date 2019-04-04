package com.atao.dftt.quartz;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.atao.dftt.model.DfToutiaoUser;
import com.atao.dftt.service.AsynService;
import com.atao.dftt.service.DfToutiaoUserWyService;

//@Component
public class DfttReadJob {
	private static Logger logger = LoggerFactory.getLogger(DfttReadJob.class);

	@Resource
	private DfToutiaoUserWyService dfToutiaoUserWyService;
	@Resource
	private AsynService asynService;

	@Scheduled(cron = "0 13,24,35,46,57 6-8,11-23 * * ?")
	public void readDfttNews() throws Exception {
		logger.info("dftt:开始阅读新闻金币任务");
		List<DfToutiaoUser> users = dfToutiaoUserWyService.getUsedUser();
		List<Future<Integer>> futures = new ArrayList<Future<Integer>>();
		for (DfToutiaoUser user : users) {
			Future<Integer> future = asynService.readDfttNews(user);
			futures.add(future);
		}
		for (Future<Integer> future : futures) {
			future.get();
		}
		logger.info("dftt:结束阅读新闻金币任务");
	}

	@Scheduled(cron = "0 0/16 8,11-23 * * ?")
	public void readDfttVideo() throws Exception {
		logger.info("dftt:开始阅读视频金币任务");
		List<DfToutiaoUser> users = dfToutiaoUserWyService.getUsedUser();
		List<Future<Integer>> futures = new ArrayList<Future<Integer>>();
		for (DfToutiaoUser user : users) {
			Future<Integer> future = asynService.readDfttVideo(user);
			futures.add(future);
		}
		for (Future<Integer> future : futures) {
			future.get();
		}
		logger.info("dftt:结束阅读视频金币任务");
	}

	@Scheduled(cron = "0 0 18 * * ?")
	public void readPushNews() {
		dfToutiaoUserWyService.readPushNews();
	}
}