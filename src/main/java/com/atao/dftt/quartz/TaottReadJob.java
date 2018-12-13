package com.atao.dftt.quartz;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.atao.dftt.model.TaoToutiaoUser;
import com.atao.dftt.service.TaoToutiaoUserWyService;
import com.atao.dftt.service.TaottCoinRecordWyService;
import com.atao.dftt.thread.TaottReadCoinThread;

@Component
public class TaottReadJob {
	private static Logger logger = LoggerFactory.getLogger(TaottReadJob.class);

	@Resource
	private TaoToutiaoUserWyService taoToutiaoUserWyService;
	@Resource
	private TaottCoinRecordWyService taottCoinRecordWyService;

	@Scheduled(cron = "0 0/11 11-23 * * ?")
	public void readDfttNews() {
		List<TaoToutiaoUser> users = taoToutiaoUserWyService.getUsedUser();
		for (TaoToutiaoUser user : users) {
			Date endTime = new Date(new Date().getTime() + 10 * 60 * 1000l);
			TaottReadCoinThread thread = new TaottReadCoinThread(endTime, user, taoToutiaoUserWyService);
			thread.start();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 每小时打一次卡
	 */
	@Scheduled(cron = "0 3 * * * ?")
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
			if (!user.getFirstDay())
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
}