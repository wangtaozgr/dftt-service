package com.atao.dftt.quartz;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.atao.dftt.model.JkdttUser;
import com.atao.dftt.service.AsynService;
import com.atao.dftt.service.JkdttUserWyService;

//@Component
public class JkdttReadJob {
	private static Logger logger = LoggerFactory.getLogger(JkdttReadJob.class);

	@Resource
	private JkdttUserWyService jkdttUserWyService;
	@Resource
	private AsynService asynService;

	@Scheduled(cron = "0 3/13 11-23 * * ?")
	public void readJkdttNews() throws Exception {
		logger.info("jkdtt:开始阅读新闻金币任务");
		List<JkdttUser> users = jkdttUserWyService.getUsedUser();
		List<Future<Integer>> futures = new ArrayList<Future<Integer>>();
		for (JkdttUser user : users) {
			Future<Integer> future = asynService.readJkdttNews(user);
			futures.add(future);
		}
		for (Future<Integer> future : futures) {
			future.get();
		}
		logger.info("jkdtt:结束阅读新闻金币任务");
	}

	/**
	 * 每小时打一次卡
	 */

	@Scheduled(cron = "0 3 6-23 * * ?")
	public void daka() {
		List<JkdttUser> users = jkdttUserWyService.getUsedUser();
		for (JkdttUser user : users) {
			jkdttUserWyService.daka(user);
		}
	}

	@Scheduled(cron = "0 5 9 * * ?")
	public void cointx() {
		List<JkdttUser> users = jkdttUserWyService.getUsedUser();
		for (JkdttUser user : users) {
			if (user.getAutoTx())
				jkdttUserWyService.cointx(user);
		}
	}

}