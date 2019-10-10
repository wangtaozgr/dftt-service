package com.atao.dftt.service;

import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.atao.dftt.model.DfToutiaoUser;
import com.atao.dftt.model.HsttUser;
import com.atao.dftt.model.JkdttUser;
import com.atao.dftt.model.MayittUser;
import com.atao.dftt.model.TaoToutiaoUser;
import com.atao.dftt.model.Wltt;
import com.atao.dftt.model.ZqttUser;

@Service
public class AsynService {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Resource
	private DfToutiaoUserWyService dfToutiaoUserWyService;
	@Resource
	private WlttWyService wlttWyService;
	@Resource
	private TaoToutiaoUserWyService taoToutiaoUserWyService;
	@Resource
	private JkdttUserWyService jkdttUserWyService;
	@Resource
	private MayittUserWyService mayittUserWyService;
	@Resource
	private ZqttUserWyService zqttUserWyService;
	@Resource
	private HsttUserWyService hsttUserWyService;
	@Resource
	private TaodanTaskWyService taodanTaskWyService;

	@Async
	public Future<Integer> readDfttNews(DfToutiaoUser user) {
		logger.info("dftt-{}:开始阅读新闻金币任务", user.getUsername());
		Integer num = 0;
		try {
			num = dfToutiaoUserWyService.readNewsCoin(user);
		} catch (Exception e) {
		}
		logger.info("dftt-{}:结束阅读新闻金币任务,本次共阅读成功次数={}", user.getUsername(), num);
		return new AsyncResult<Integer>(num);
	}

	@Async
	public Future<Integer> readDfttVideo(DfToutiaoUser user) {
		logger.info("dftt-{}:开始阅读视频金币任务", user.getUsername());
		Integer num = 0;
		try {
			num = dfToutiaoUserWyService.readVideoCoin(user);
		} catch (Exception e) {
		}
		logger.info("dftt-{}:结束阅读视频金币任务,本次共阅读成功次数={}", user.getUsername(), num);
		return new AsyncResult<Integer>(num);
	}

	@Async
	public Future<Integer> readWlttNews(Wltt user) {
		logger.info("wltt-{}:开始阅读新闻金币任务", user.getUsername());
		Integer num = 0;
		try {
			num = wlttWyService.readNewsCoin(user);
		} catch (Exception e) {
		}
		logger.info("wltt-{}:结束阅读新闻金币任务,本次共阅读成功次数={}", user.getUsername(), num);
		return new AsyncResult<Integer>(num);
	}

	@Async
	public Future<Integer> searchTask(Wltt user) {
		logger.info("wltt-{}:开始搜索任务", user.getUsername());
		Integer num = 0;
		try {
			num = wlttWyService.searchTask(user);
		} catch (Exception e) {
		}
		logger.info("wltt-{}:结束搜索任务,本次共阅读成功次数={}", user.getUsername(), num);
		return new AsyncResult<Integer>(num);
	}

	@Async
	public Future<Integer> readTaottNews(TaoToutiaoUser user) {
		logger.info("taott-{}:开始阅读新闻金币任务", user.getUsername());
		Integer num = 0;
		try {
			num = taoToutiaoUserWyService.readNewsCoin(user);
		} catch (Exception e) {
		}
		logger.info("taott-{}:结束阅读新闻金币任务,本次共阅读成功次数={}", user.getUsername(), num);
		return new AsyncResult<Integer>(num);
	}

	@Async
	public Future<Integer> readJkdttNews(JkdttUser user) {
		logger.info("jkdtt-{}:开始阅读新闻金币任务", user.getUsername());
		Integer num = 0;
		try {
			num = jkdttUserWyService.readNewsCoin(user);
		} catch (Exception e) {
		}
		logger.info("jkdtt-{}:结束阅读新闻金币任务,本次共阅读成功次数={}", user.getUsername(), num);
		return new AsyncResult<Integer>(num);
	}

	@Async
	public Future<Integer> readMayittNews(MayittUser user) {
		logger.info("mayitt-{}:开始阅读新闻金币任务", user.getUsername());
		Integer num = 0;
		try {
			num = mayittUserWyService.readNewsCoin(user);
		} catch (Exception e) {
		}
		logger.info("mayitt-{}:结束阅读新闻金币任务,本次共阅读成功次数={}", user.getUsername(), num);
		return new AsyncResult<Integer>(num);
	}

	@Async
	public Future<Integer> readMayittVideo(MayittUser user) {
		logger.info("mayitt-{}:开始阅读视频金币任务", user.getUsername());
		Integer num = 0;
		try {
			num = mayittUserWyService.readVideoCoin(user);
		} catch (Exception e) {
		}
		logger.info("mayitt-{}:结束阅读视频金币任务,本次共阅读成功次数={}", user.getUsername(), num);
		return new AsyncResult<Integer>(num);
	}

	@Async
	public Future<Integer> readMayittAdTask(MayittUser user) {
		logger.info("mayitt-{}:开始阅读广告金币任务", user.getUsername());
		Integer num = 0;
		try {
			num = mayittUserWyService.readAdTask(user);
		} catch (Exception e) {
		}
		logger.info("mayitt-{}:结束阅读广告金币任务,本次共阅读成功次数={}", user.getUsername(), num);
		return new AsyncResult<Integer>(num);
	}

	@Async
	public Future<Integer> readMayittRwTask(MayittUser user) {
		logger.info("mayitt-{}:开始阅读热文金币任务", user.getUsername());
		Integer num = 0;
		try {
			num = mayittUserWyService.readRwTask(user);
		} catch (Exception e) {
		}
		logger.info("mayitt-{}:结束阅读热文金币任务,本次共阅读成功次数={}", user.getUsername(), num);
		return new AsyncResult<Integer>(num);
	}

	@Async
	public Future<Integer> readMayittMoreTask(MayittUser user) {
		logger.info("mayitt-{}:开始阅读更多金币任务", user.getUsername());
		Integer num = 0;
		try {
			num = mayittUserWyService.readMoreTask(user);
		} catch (Exception e) {
		}
		logger.info("mayitt-{}:结束阅读更多金币任务,本次共阅读成功次数={}", user.getUsername(), num);
		return new AsyncResult<Integer>(num);
	}

	@Async
	public Future<Integer> readMayittHongbaoTask(MayittUser user) {
		logger.info("mayitt-{}:开始阅读红包金币任务", user.getUsername());
		Integer num = 0;
		try {
			num = mayittUserWyService.readHongbao(user);
		} catch (Exception e) {
		}
		logger.info("mayitt-{}:结束阅读红包金币任务,本次共阅读成功次数={}", user.getUsername(), num);
		return new AsyncResult<Integer>(num);
	}

	////////////////////////// 花生头条/////////////////////////
	@Async
	public Future<Integer> readHsttNews(HsttUser user) {
		logger.info("hstt-{}:开始阅读新闻金币任务", user.getUsername());
		Integer num = 0;
		try {
			num = hsttUserWyService.readNewsCoin(user);
		} catch (Exception e) {
		}
		logger.info("hstt-{}:结束阅读新闻金币任务,本次共阅读成功次数={}", user.getUsername(), num);
		return new AsyncResult<Integer>(num);
	}

	@Async
	public Future<Integer> searchTask(HsttUser user) {
		logger.info("hstt-{}:开始搜索任务", user.getUsername());
		Integer num = 0;
		try {
			num = hsttUserWyService.readHotWord(user);
		} catch (Exception e) {
		}
		logger.info("hstt-{}:结束搜索任务,本次共阅读成功次数={}", user.getUsername(), num);
		return new AsyncResult<Integer>(num);
	}

	////////////////////////// 中青看点/////////////////////////
	@Async
	public Future<Integer> readZqttNews(ZqttUser user) {
		logger.info("zqtt-{}:开始阅读新闻金币任务", user.getUsername());
		Integer num = 0;
		try {
			num = zqttUserWyService.readNewsCoin(user);
		} catch (Exception e) {
		}
		logger.info("zqtt-{}:结束阅读新闻金币任务,本次共阅读成功次数={}", user.getUsername(), num);
		return new AsyncResult<Integer>(num);
	}

	@Async
	public Future<Integer> readZqttKkzTask(ZqttUser user) {
		logger.info("zqtt-{}:开始阅读看看赚金币任务", user.getUsername());
		Integer num = 0;
		try {
			num = zqttUserWyService.readMoreTask(user);
		} catch (Exception e) {
		}
		logger.info("zqtt-{}:结束阅读看看赚金币任务,本次共阅读成功次数={}", user.getUsername(), num);
		return new AsyncResult<Integer>(num);
	}

	@Async
	public Future<Integer> searchTask(ZqttUser user) {
		logger.info("zqtt-{}:开始阅读搜索任务金币任务", user.getUsername());
		Integer num = 0;
		try {
			num = zqttUserWyService.searchTask(user);
		} catch (Exception e) {
		}
		logger.info("zqtt-{}:结束阅读搜索任务金币任务,本次共阅读成功次数={}", user.getUsername(), num);
		return new AsyncResult<Integer>(num);
	}

	@Async
	public Future<Integer> updateConfirmOrderStatus(String username) {
		logger.info("taodan-{}:开始确认收货任务", username);
		Integer num = 0;
		try {
			taodanTaskWyService.update10000OrderStatus(username);
			num = taodanTaskWyService.updateConfirmOrderStatus(username);
		} catch (Exception e) {
		}
		logger.info("taodan-{}:结束确认收货任务", username);
		return new AsyncResult<Integer>(num);
	}

	@Async
	public Future<Integer> downloadAllCommentImage(String username) {
		logger.info("taodan-{}:开始下载评价图片和上传淘单图片任务", username);
		Integer num = 0;
		try {
			num = taodanTaskWyService.downloadAllCommentImage(username);
			logger.info("taodan-{}:下载评价图片任务数量,count={}", username, num);
			num = taodanTaskWyService.uploadTaodanImg(username);
			logger.info("taodan-{}:上传淘单图片任务数量,count={}", username, num);
		} catch (Exception e) {
		}
		logger.info("taodan-{}:结束下载评价图片和上传淘单图片任务", username);
		return new AsyncResult<Integer>(num);
	}

	@Async
	public Future<String> withdraw(String username) {
		logger.info("taodan-{}:开始提现任务", username);
		String msg = "";
		try {
			msg = taodanTaskWyService.withdraw(username);
		} catch (Exception e) {
		}
		logger.info("taodan-{}:结束提现任务,msg={}", username, msg);
		return new AsyncResult<String>(msg);
	}

}
