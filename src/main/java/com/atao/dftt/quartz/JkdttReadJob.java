package com.atao.dftt.quartz;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.atao.dftt.model.JkdttUser;
import com.atao.dftt.service.JkdttUserWyService;
import com.atao.dftt.thread.JkdttReadCoinThread;

@Component
public class JkdttReadJob {
	@Resource
	private JkdttUserWyService jkdttUserWyService;

	@Scheduled(cron = "0 12,23,34,45,56 8,11-23 * * ?")
	public void readNews() {
		List<JkdttUser> users = jkdttUserWyService.getUsedUser();
		for (JkdttUser user : users) {
			Date endTime = new Date(new Date().getTime() + 5 * 60 * 1000l);
			JkdttReadCoinThread thread = new JkdttReadCoinThread(endTime, user, jkdttUserWyService);
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