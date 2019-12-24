package com.atao.dftt.quartz;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.atao.dftt.model.ZhuankeUser;
import com.atao.dftt.service.AsynService;
import com.atao.dftt.service.ZhuankeTaskWyService;
import com.atao.dftt.service.ZhuankeUserWyService;

@Component
public class ZhuankeJob {
	private static Logger logger = LoggerFactory.getLogger(ZhuankeJob.class);
	@Resource
	private ZhuankeTaskWyService zhuankeTaskWyService;
	@Resource
	private ZhuankeUserWyService zhuankeUserWyService;
	@Resource
	private AsynService asynService;

	@Scheduled(cron = "0 25 10,17,21 * * ?")
	public void updateConfirmOrderStatus() throws Exception {
		List<ZhuankeUser> users = zhuankeUserWyService.getUsedUser();
		for (ZhuankeUser user : users) {
			zhuankeTaskWyService.updateTxConfirmOrderStatus(user.getUsername());
			int num = zhuankeTaskWyService.updateConfirmOrderStatus(user.getUsername());
			logger.info("zhuanke-{}:|确认收货成功.num={}", user.getUsername(), num);
		}
	}

	@Scheduled(cron = "0 35 10,17,21 * * ?")
	public void downloadAllCommentImage() throws Exception {
		logger.info("zhuanke:开始下载评价图片和上传淘单图片任务");
		List<ZhuankeUser> users = zhuankeUserWyService.getUsedUser();
		for (ZhuankeUser user : users) {
			int num = zhuankeTaskWyService.downloadAllCommentImage(user.getUsername());
			logger.info("zhuanke-{}:下载评价图片任务数量,count={}", user.getUsername(), num);
			num = zhuankeTaskWyService.uploadZhuankeImg(user.getUsername());
			logger.info("zhuanke-{}:上传赚客图片任务数量,count={}", user.getUsername(), num);
		}
	}
	
	@Scheduled(cron = "0 10 17 * * *")
	public void txje02() throws Exception {
		List<ZhuankeUser> users = zhuankeUserWyService.getUsedUser();
		for (ZhuankeUser user : users) {
			logger.info("zhuanke-{}:开始提现任务", user.getUsername());
			String msg = zhuankeTaskWyService.withdraw(user.getUsername());
			logger.info("zhuanke-{}:结束提现任务,msg={}", user.getUsername(), msg);
		}
	}
}