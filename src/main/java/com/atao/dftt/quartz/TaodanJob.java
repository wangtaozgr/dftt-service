package com.atao.dftt.quartz;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.atao.dftt.model.TaodanUser;
import com.atao.dftt.service.AsynService;
import com.atao.dftt.service.TaodanTaskWyService;
import com.atao.dftt.service.TaodanUserWyService;

@Component
public class TaodanJob {
	private static Logger logger = LoggerFactory.getLogger(TaodanJob.class);
	@Resource
	private TaodanTaskWyService taodanTaskWyService;
	@Resource
	private TaodanUserWyService taodanUserWyService;
	@Resource
	private AsynService asynService;

	@Scheduled(cron = "0 05 10,17,21 * * ?")
	public void updateConfirmOrderStatus() throws Exception {
		logger.info("taodan:开始确认收货任务");
		List<TaodanUser> users = taodanUserWyService.getUsedUser();
		List<Future<Integer>> futures = new ArrayList<Future<Integer>>();
		for (TaodanUser user : users) {
			Future<Integer> future = asynService.updateConfirmOrderStatus(user.getUsername());
			futures.add(future);
		}
		for (Future<Integer> future : futures) {
			future.get();
		}
		logger.info("taodan:结束确认收货任务");
	}

	@Scheduled(cron = "0 25 10,17,21 * * ?")
	public void downloadAllCommentImage() throws Exception {
		logger.info("taodan:开始下载评价图片和上传淘单图片任务");
		List<TaodanUser> users = taodanUserWyService.getUsedUser();
		List<Future<Integer>> futures = new ArrayList<Future<Integer>>();
		for (TaodanUser user : users) {
			Future<Integer> future = asynService.downloadAllCommentImage(user.getUsername());
			futures.add(future);
		}
		for (Future<Integer> future : futures) {
			future.get();
		}
	}
	
	@Scheduled(cron = "0 0 22 * * 1-4")
	public void txje() throws Exception {
		logger.info("taodan:开始提现任务");
		List<TaodanUser> users = taodanUserWyService.getUsedUser();
		List<Future<String>> futures = new ArrayList<Future<String>>();
		for (TaodanUser user : users) {
			Future<String> future = asynService.withdraw(user.getUsername());
			futures.add(future);
		}
		for (Future<String> future : futures) {
			future.get();
		}
	}
	
	@Scheduled(cron = "0 30 17 * * 5")
	public void txje02() throws Exception {
		logger.info("taodan:开始提现任务");
		List<TaodanUser> users = taodanUserWyService.getUsedUser();
		List<Future<String>> futures = new ArrayList<Future<String>>();
		for (TaodanUser user : users) {
			Future<String> future = asynService.withdraw(user.getUsername());
			futures.add(future);
		}
		for (Future<String> future : futures) {
			future.get();
		}
	}
}