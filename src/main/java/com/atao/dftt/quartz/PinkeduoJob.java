package com.atao.dftt.quartz;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.atao.dftt.model.PinkeduoUser;
import com.atao.dftt.service.AsynService;
import com.atao.dftt.service.PinkeduoTaskWyService;
import com.atao.dftt.service.PinkeduoUserWyService;

@Component
public class PinkeduoJob {
	private static Logger logger = LoggerFactory.getLogger(PinkeduoJob.class);
	@Resource
	private PinkeduoTaskWyService pinkeduoTaskWyService;
	@Resource
	private PinkeduoUserWyService pinkeduoUserWyService;
	@Resource
	private AsynService asynService;

	// @Scheduled(cron = "0 15 10,17,21 * * ?")
	public void updateConfirmOrderStatus() throws Exception {
		logger.info("pingkeduo:开始确认收货任务");
		List<PinkeduoUser> users = pinkeduoUserWyService.getUsedUser();
		for (PinkeduoUser user : users) {
		}
		logger.info("pingkeduo:结束确认收货任务");
	}

}