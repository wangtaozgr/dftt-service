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
import com.atao.dftt.service.DdhpTaskWyService;

//@Component
public class DdhpJob {
	private static Logger logger = LoggerFactory.getLogger(DdhpJob.class);

	@Resource
	private DdhpTaskWyService ddhpTaskWyService;
	@Resource
	private AsynService asynService;

	@Scheduled(cron = "0 0/1 8-23 * * ?")
	public void readDfttNews() throws Exception {
		logger.info("ddhp:开始查询任务");
		
		logger.info("ddhp:结束查询任务");
	}
}