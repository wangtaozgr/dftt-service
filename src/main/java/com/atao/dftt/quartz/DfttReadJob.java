package com.atao.dftt.quartz;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.atao.dftt.model.DfToutiaoUser;
import com.atao.dftt.service.DfToutiaoUserWyService;
import com.atao.dftt.thread.DfttReadCoinThread;
import com.atao.dftt.thread.DfttReadVideoCoinThread;

@Component
public class DfttReadJob {
	@Resource
	private DfToutiaoUserWyService dfToutiaoUserWyService;

	@Scheduled(cron = "0 0/15 11-23 * * ?")
	public void readDfttNews() {
		List<DfToutiaoUser> users = dfToutiaoUserWyService.getUsedUser();
		for (DfToutiaoUser user : users) {
			Date endTime = new Date(new Date().getTime() + 10 * 60 * 1000l);
			DfttReadCoinThread thread = new DfttReadCoinThread(endTime, user, dfToutiaoUserWyService);
			thread.start();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	
	@Scheduled(cron = "0 0/16 11-23 * * ?")
	public void readDfttVideo() {
		List<DfToutiaoUser> users = dfToutiaoUserWyService.getUsedUser();
		for (DfToutiaoUser user : users) {
			Date endTime = new Date(new Date().getTime() + 10 * 60 * 1000l);
			DfttReadVideoCoinThread thread = new DfttReadVideoCoinThread(endTime, user, dfToutiaoUserWyService);
			thread.start();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	@Scheduled(cron = "0 0 18 * * ?")
	public void readPushNews() {
		dfToutiaoUserWyService.readPushNews();
	}
}