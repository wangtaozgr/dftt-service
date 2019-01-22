package com.atao.dftt.quartz;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.atao.dftt.model.Wltt;
import com.atao.dftt.service.WlttCoinRecordWyService;
import com.atao.dftt.service.WlttWyService;
import com.atao.dftt.thread.WlttReadCoinThread;
import com.atao.dftt.thread.WlttReadVideoThread;

@Component
public class WlttReadJob {
	@Resource
	private WlttWyService wlttWyService;
	@Resource
	private WlttCoinRecordWyService wlttCoinRecordWyService;

	@Scheduled(cron = "0 06,12,18,24,30,36,42,48,54 6-8,11-23 * * ?")
	public void readNews() {
		List<Wltt> users = wlttWyService.getUsedUser();
		for (Wltt user : users) {
			Date endTime = new Date(new Date().getTime() + 5 * 60 * 1000l);
			WlttReadCoinThread thread = new WlttReadCoinThread(endTime, user, wlttWyService);
			thread.start();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void readVideo() {
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
	}

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