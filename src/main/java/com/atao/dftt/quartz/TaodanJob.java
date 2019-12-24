package com.atao.dftt.quartz;

import java.util.List;

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
		for (TaodanUser user : users) {
			taodanTaskWyService.update10000OrderStatus(user.getUsername());
			int num = taodanTaskWyService.updateConfirmOrderStatus(user.getUsername());
			logger.info("taodan-{}:确认收货成功数量,count={}", user.getUsername(), num);
		}
		logger.info("taodan:结束确认收货任务");
	}

	@Scheduled(cron = "0 15 10,17,21 * * ?")
	public void downloadAllCommentImage() throws Exception {
		logger.info("taodan:开始下载评价图片和上传淘单图片任务");
		List<TaodanUser> users = taodanUserWyService.getUsedUser();
		for (TaodanUser user : users) {
			int num = taodanTaskWyService.downloadAllCommentImage(user.getUsername());
			logger.info("taodan-{}:下载评价图片任务数量,count={}", user.getUsername(), num);
			num = taodanTaskWyService.uploadTaodanImg(user.getUsername());
			logger.info("taodan-{}:上传淘单图片任务数量,count={}", user.getUsername(), num);
		}
	}

	//@Scheduled(cron = "0 0 22 * * 1-4")
	public void txje() throws Exception {
		List<TaodanUser> users = taodanUserWyService.getUsedUser();
		for (TaodanUser user : users) {
			logger.info("taodan-{}:开始提现任务", user.getUsername());
			String msg = taodanTaskWyService.withdraw(user.getUsername());
			logger.info("taodan-{}:结束提现任务,msg={}", user.getUsername(), msg);
		}
	}

	@Scheduled(cron = "0 30 17 * * 1-5")
	public void txje02() throws Exception {
		List<TaodanUser> users = taodanUserWyService.getUsedUser();
		for (TaodanUser user : users) {
			logger.info("taodan-{}:开始提现任务", user.getUsername());
			String msg = taodanTaskWyService.withdraw(user.getUsername());
			logger.info("taodan-{}:结束提现任务,msg={}", user.getUsername(), msg);
		}
	}
}