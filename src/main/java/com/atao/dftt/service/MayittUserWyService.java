package com.atao.dftt.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atao.base.mapper.BaseMapper;
import com.atao.base.service.BaseService;
import com.atao.base.util.StringUtils;
import com.atao.dftt.http.MayittHttp;
import com.atao.dftt.mapper.MayittUserMapper;
import com.atao.dftt.model.CoinTxRecord;
import com.atao.dftt.model.MayittCoinRecord;
import com.atao.dftt.model.MayittUser;
import com.atao.dftt.util.MayittUtils;
import com.atao.util.DateUtils;

import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

/**
 *
 * @author twang
 */
@Service
public class MayittUserWyService extends BaseService<MayittUser> {

	@Resource
	private MayittUserMapper mayittUserMapper;
	@Resource
	private MayittCoinRecordWyService mayittCoinRecordWyService;
	@Resource
	private CoinTxRecordWyService coinTxRecordWyService;

	@Override
	public BaseMapper<MayittUser> getMapper() {
		return mayittUserMapper;
	}

	public List<MayittUser> getUsedUser() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		Date today = c.getTime();
		MayittUser dftt = new MayittUser();
		dftt.setUsed(true);
		List<MayittUser> users = super.queryList(dftt, null);
		for (MayittUser user : users) {
			if (user.getReadTime() == null || user.getReadTime().getTime() < today.getTime()) {
				user.setReadNum(0l);
				user.setReadTime(new Date());
				super.updateBySelect(user);
			}
			if (user.getVReadTime() == null || user.getVReadTime().getTime() < today.getTime()) {
				user.setVReadNum(0l);
				user.setVReadTime(new Date());
				super.updateBySelect(user);

			}
		}
		return users;
	}

	public MayittUser queryUserByUsername(String username) {
		MayittUser p = new MayittUser();
		p.setUsername(username);
		return super.queryOne(p, null);
	}

	public int readNewsCoin(MayittUser user) throws Exception {
		MayittHttp http = MayittHttp.getInstance(user);
		String today = DateUtils.formatDate(new Date(), "yyyyMMdd");
		if (user.getQdTime() == null || !today.equals(DateUtils.formatDate(user.getQdTime(), "yyyyMMdd"))) {
			JSONArray appStart = MayittUtils.AppStart(user);
			Thread.sleep(new Random().nextInt(1000 + new Random().nextInt(1000)));
			http.eventList.addAll(appStart);
			// 开始签到
			JSONObject taskClick = MayittUtils.AppClick(user,
					"com.weishang.wxrd.activity.MainActivity|com.weishang.wxrd.ui.MainFragment",
					"com.weishang.wxrd.activity.MainActivity######", "tv_task_tab", "任务", "TextView");
			JSONObject task = MayittUtils.enterTab(user,
					"com.sensorsdata.analytics.android.sdk.SensorsDataAPI$10##run##SensorsDataAPI.java##1979", "任务");
			http.eventList.add(taskClick);
			http.eventList.add(task);
			boolean suc = http.qiandao();
			if (suc) {
				user.setQdTime(new Date());
				super.updateBySelect(user);
			}
			JSONObject index = MayittUtils.enterTab(user,
					"com.sensorsdata.analytics.android.sdk.SensorsDataAPI$10##run##SensorsDataAPI.java##1979", "首页");
			http.eventList.add(index);
			// 发送请求
			http.sendData();
		}
		int readedNum = 0;
		long readNum = user.getReadNum();
		int time = 30 * 1000 + new Random().nextInt(15000);
		while (readNum < user.getLimitReadNum()) {
			JSONObject indexFresh = MayittUtils.AppClick(user,
					"com.weishang.wxrd.activity.MainActivity|com.weishang.wxrd.ui.MainFragment",
					"com.weishang.wxrd.activity.MainActivity######", "tv_home_tab", "刷新", "TextView");
			http.eventList.add(indexFresh);
			JSONObject enterTab = MayittUtils.enterTab(user, "首页",
					"com.sensorsdata.analytics.android.sdk.SensorsDataAPI$10##run##SensorsDataAPI.java##1979");
			http.eventList.add(enterTab);
			http.eventList.add(MayittUtils.adShow(user));

			JSONArray newsList = http.newsList();
			if (newsList.size() < 1) {
				logger.info(user.getUsername() + ":没有查到新闻列表!");
				return readedNum;
			}
			for (int i = 0; i < newsList.size(); i++) {
				JSONObject news = newsList.getJSONObject(i);
				if (news.getIntValue("article_type") == 0) {
					time = 30 * 1000 + new Random().nextInt(5000);
					Thread.sleep(time);
					String newsId = news.getString("id");
					String title = news.getString("title");
					String account_name = news.getString("account_name");
					String read_num = news.getString("read_num");
					JSONArray readNews = MayittUtils.readNews(user, newsId, title, account_name, read_num);
					http.eventList.addAll(readNews);
					http.sendData();
					time = new Random().nextInt(2000);
					JSONObject result = http.readNews(newsId);
					if (result == null) {
						return readedNum;
					}
					if (result.getBooleanValue("success")) {
						readNum++;
						readedNum++;
						logger.info("mayitt-{}:阅读新闻金币成功  已读次数={}", user.getUsername(), readNum);
						user.setReadNum(readNum);
						user.setReadTime(new Date());
						this.updateBySelect(user);
						if (readNum >= user.getLimitReadNum()) {
							return readedNum;
						}
					} else if (result.getIntValue("error_code") == 200001) {
						continue;
					} else if (result.getIntValue("error_code") == 10017) {
						continue;
					} else {
						readNum = user.getLimitReadNum();
						user.setReadNum(readNum);
						user.setReadTime(new Date());
						this.updateBySelect(user);
						logger.error("mayitt-{}:阅读新闻金币报错或已达到上限", user.getUsername());
						return readedNum;
					}
				}
			}
		}
		return readedNum;
	}

	public int readVideoCoin(MayittUser user) throws Exception {
		MayittHttp http = MayittHttp.getInstance(user);
		int readedNum = 0;
		long readNum = user.getVReadNum();
		int time = 30 * 1000 + new Random().nextInt(15000);
		JSONObject videoAppClick = MayittUtils.AppClick(user,
				"com.weishang.wxrd.activity.MainActivity|com.weishang.wxrd.ui.MainFragment",
				"com.weishang.wxrd.activity.MainActivity######", "tv_find_tab", "视频", "TextView");
		http.eventList.add(videoAppClick);
		JSONObject enterTab = MayittUtils.enterTab(user,
				"com.sensorsdata.analytics.android.sdk.SensorsDataAPI$10##run##SensorsDataAPI.java##1979", "视频");
		http.eventList.add(enterTab);
		JSONObject appViewScreen01 = MayittUtils.AppViewScreen(user,
				"com.weishang.wxrd.activity.MainActivity|com.weishang.wxrd.ui.HomeFragment",
				"com.weishang.wxrd.activity.MainActivity######");
		http.eventList.add(appViewScreen01);

		while (readNum < user.getVLimitReadNum()) {
			JSONArray newsList = http.videoList();
			if (newsList.size() < 1) {
				logger.info(user.getUsername() + ":没有查到视频列表!");
				return readedNum;
			}
			for (int i = 0; i < newsList.size(); i++) {
				JSONObject news = newsList.getJSONObject(i);
				if (news.getIntValue("article_type") == 0) {
					time = 30 * 1000 + new Random().nextInt(5000);
					Thread.sleep(time);
					String newsId = news.getString("id");
					String title = news.getString("title");
					String account_name = news.getString("account_name");
					String video_time = news.getString("video_time");
					String read_num = news.getString("read_num");
					JSONArray readNews = MayittUtils.readVideo(user, newsId, title, account_name, read_num, video_time);
					http.eventList.addAll(readNews);
					http.sendData();
					time = new Random().nextInt(2000);
					JSONObject result = http.readVideo(newsId);
					if (result == null) {
						return readedNum;
					}
					if (result.getBooleanValue("success")) {
						readNum++;
						readedNum++;
						logger.info("mayitt-{}:阅读视频金币成功  已读次数={}", user.getUsername(), readNum);
						user.setVReadNum(readNum);
						user.setVReadTime(new Date());
						this.updateBySelect(user);
						if (readNum >= user.getVLimitReadNum()) {
							return readedNum;
						}
					} else if (result.getIntValue("error_code") == 200001) {
						continue;
					} else if (result.getIntValue("error_code") == 10017) {
						continue;
					} else {
						readNum = user.getVLimitReadNum();
						user.setVReadNum(readNum);
						user.setVReadTime(new Date());
						this.updateBySelect(user);
						logger.error("mayitt-{}:阅读视频金币报错或已达到上限", user.getUsername());
						return readedNum;
					}
				}
			}
		}
		return readedNum;
	}

	/**
	 * 广告任务,每天40次
	 * 
	 * @param user
	 * @param endTime
	 * @throws Exception
	 */
	public int readAdTask(MayittUser user) throws Exception {
		MayittHttp http = MayittHttp.getInstance(user);
		JSONObject taskAppClick = MayittUtils.AppClick(user,
				"com.weishang.wxrd.activity.MainActivity|com.weishang.wxrd.ui.MainFragment",
				"com.weishang.wxrd.activity.MainActivity######", "tv_find_tab", "任务", "TextView");
		http.eventList.add(taskAppClick);
		JSONObject enterTab = MayittUtils.enterTab(user,
				"com.sensorsdata.analytics.android.sdk.SensorsDataAPI$10##run##SensorsDataAPI.java##1979", "任务");
		http.eventList.add(enterTab);
		JSONObject appViewScreen01 = MayittUtils.AppViewScreen(user,
				"com.weishang.wxrd.activity.MainActivity|com.weishang.wxrd.ui.WebViewFragment",
				"com.weishang.wxrd.activity.MainActivity######");
		http.eventList.add(appViewScreen01);
		JSONObject appViewScreen02 = MayittUtils.AppViewScreen(user, "com.weishang.wxrd.activity.MoreActivity",
				"com.weishang.wxrd.activity.MoreActivity######");
		http.eventList.add(appViewScreen02);
		JSONObject appViewScreen03 = MayittUtils.AppViewScreen(user,
				"com.weishang.wxrd.activity.MoreActivity|com.weishang.wxrd.ui.WebViewFragment",
				"com.weishang.wxrd.activity.MoreActivity######");
		http.eventList.add(appViewScreen03);
		http.eventList.add(appViewScreen03);// 两次
		int time = 30 * 1000;
		JSONObject taskApiData = http.taskApi(); 
		JSONArray info = taskApiData.getJSONArray("info");
		JSONObject ad = null;
		for (int i = 0; i < info.size(); i++) {
			if ("task_ad_score_num".equals(info.getJSONObject(i).getString("name"))) {
				ad = info.getJSONObject(i);
			}
		}
		if (ad == null) {
			logger.error("mayitt-{}:没有找到广告任务的id", user.getUsername());
			return 0;
		}
		String ad_id = ad.getString("id");
		int readedNum = ad.getIntValue("task_ad_num");
		int max_num = ad.getIntValue("max_num");
		while (readedNum < max_num) {
			time = 200 * 1000 + new Random().nextInt(5000);
			Thread.sleep(time);
			if (http.adTaskData == null) {
				logger.error("mayitt-{}:{}", user.getUsername(), "没有查到广告数据，放弃广告任务");
				return readedNum;
			}
			http.startAdTask();
			JSONArray readAdTask = MayittUtils.readAdTask(user, http.adTaskData);
			http.eventList.addAll(readAdTask);
			http.sendData();
			time = 30 * 1000;
			Thread.sleep(time);
			JSONObject result = http.readAdCoin(ad_id);
			if (result.getBooleanValue("success")) {
				readedNum++;
				logger.info("mayitt-{}:阅读广告任务金币成功  已读次数={}", user.getUsername(), readedNum);
			} else {
				logger.error("mayitt-{}:阅读广告任务金币报错", user.getUsername());
				return readedNum;
			}
		}
		return readedNum;
	}

	/**
	 * 看热文任务
	 * 
	 * @param user
	 * @throws Exception
	 */
	public int readRwTask(MayittUser user) throws Exception {
		MayittHttp http = MayittHttp.getInstance(user);
		JSONObject taskAppClick = MayittUtils.AppClick(user,
				"com.weishang.wxrd.activity.MainActivity|com.weishang.wxrd.ui.MainFragment",
				"com.weishang.wxrd.activity.MainActivity######", "tv_find_tab", "任务", "TextView");
		http.eventList.add(taskAppClick);
		JSONObject enterTab = MayittUtils.enterTab(user,
				"com.sensorsdata.analytics.android.sdk.SensorsDataAPI$10##run##SensorsDataAPI.java##1979", "任务");
		http.eventList.add(enterTab);
		JSONObject appViewScreen01 = MayittUtils.AppViewScreen(user,
				"com.weishang.wxrd.activity.MainActivity|com.weishang.wxrd.ui.WebViewFragment",
				"com.weishang.wxrd.activity.MainActivity######");
		http.eventList.add(appViewScreen01);
		JSONObject appViewScreen02 = MayittUtils.AppViewScreen(user, "com.weishang.wxrd.activity.MoreActivity",
				"com.weishang.wxrd.activity.MoreActivity######");
		http.eventList.add(appViewScreen02);
		JSONObject appViewScreen03 = MayittUtils.AppViewScreen(user,
				"com.weishang.wxrd.activity.MoreActivity|com.weishang.wxrd.ui.WebViewFragment",
				"com.weishang.wxrd.activity.MoreActivity######");
		http.eventList.add(appViewScreen03);
		int time = 30 * 1000;
		http.sendData();
		JSONObject taskApiData = http.taskApi();
		JSONArray info = taskApiData.getJSONArray("info");
		JSONObject rw = null;
		for (int i = 0; i < info.size(); i++) {
			if ("task_hot_score_num".equals(info.getJSONObject(i).getString("name"))) {
				rw = info.getJSONObject(i);
			}
		}
		if (rw == null) {
			logger.error("mayitt-{}:没有找到看热文任务的id", user.getUsername());
			return 0;
		}
		String ad_id = rw.getString("id");
		int readedNum = rw.getIntValue("task_ad_num");
		int max_num = rw.getIntValue("max_num");
		while (readedNum < max_num) {
			time = new Random().nextInt(5000);
			Thread.sleep(time);
			JSONObject appViewScreen04 = MayittUtils.AppViewScreen(user,
					"com.weishang.wxrd.activity.MoreActivity|com.weishang.wxrd.ui.WebViewFragment",
					"com.weishang.wxrd.activity.MoreActivity######");
			http.eventList.add(appViewScreen04);
			JSONObject result = http.taskAdStatus(ad_id, "1");
			if (result == null)
				return readedNum;
			if (result.getBooleanValue("success")) {
				JSONObject kkTasks = result.getJSONObject("items");
				int read_num = kkTasks.getIntValue("read_num");
				int see_num = kkTasks.getIntValue("see_num");
				while (read_num < see_num) {// 前4次
					JSONObject appViewScreen05 = MayittUtils.AppViewScreen(user,
							"com.weishang.wxrd.activity.MoreActivity|com.weishang.wxrd.ui.WebAdFragment",
							"com.weishang.wxrd.activity.MoreActivity######");
					http.eventList.add(appViewScreen05);
					http.sendData();
					time = 30 * 1000 + new Random().nextInt(3000);
					Thread.sleep(time);
					JSONObject readTask = http.taskAdStatus(ad_id, "0");
					if (readTask.getBooleanValue("success")) {
						read_num++;
						logger.info("mayitt-{}:阅读看热文第{}次成功", user.getUsername(), read_num);
					} else {
						logger.error("mayitt-{}:阅读看热文第{}次失败 ,跳出", user.getUsername(), read_num);
						return readedNum;
					}
				}
				JSONObject finishTask = http.readRwCoin(ad_id);
				if (finishTask.getBooleanValue("success")) {
					readedNum++;
					logger.info("mayitt-{}:阅读看热文金币成功  已读次数={}", user.getUsername(), readedNum);
				} else {
					logger.error("mayitt-{}:阅读看热文金币报错", user.getUsername());
					return readedNum;
				}
			} else {
				logger.error("mayitt-{}:阅读看热文金币报错", user.getUsername());
				return readedNum;
			}
		}
		return readedNum;
	}

	/**
	 * 看其它任务
	 * 
	 * @param user
	 * @throws Exception
	 */
	public int readMoreTask(MayittUser user) throws Exception {
		MayittHttp http = MayittHttp.getInstance(user);
		JSONObject taskAppClick = MayittUtils.AppClick(user,
				"com.weishang.wxrd.activity.MainActivity|com.weishang.wxrd.ui.MainFragment",
				"com.weishang.wxrd.activity.MainActivity######", "tv_find_tab", "任务", "TextView");
		http.eventList.add(taskAppClick);
		JSONObject enterTab = MayittUtils.enterTab(user,
				"com.sensorsdata.analytics.android.sdk.SensorsDataAPI$10##run##SensorsDataAPI.java##1979", "任务");
		http.eventList.add(enterTab);
		JSONObject appViewScreen01 = MayittUtils.AppViewScreen(user,
				"com.weishang.wxrd.activity.MainActivity|com.weishang.wxrd.ui.WebViewFragment",
				"com.weishang.wxrd.activity.MainActivity######");
		http.eventList.add(appViewScreen01);
		JSONObject appViewScreen02 = MayittUtils.AppViewScreen(user, "com.weishang.wxrd.activity.MoreActivity",
				"com.weishang.wxrd.activity.MoreActivity######");
		http.eventList.add(appViewScreen02);
		JSONObject appViewScreen03 = MayittUtils.AppViewScreen(user,
				"com.weishang.wxrd.activity.MoreActivity|com.weishang.wxrd.ui.WebViewFragment",
				"com.weishang.wxrd.activity.MoreActivity######");
		http.eventList.add(appViewScreen03);
		int time = 30 * 1000;
		http.sendData();
		JSONObject taskApiData = http.taskApi();
		JSONArray info = taskApiData.getJSONArray("info");
		JSONObject more = null;
		JSONObject appViewScreen04 = MayittUtils.AppViewScreen(user,
				"com.weishang.wxrd.activity.MoreActivity|com.weishang.wxrd.ui.WebViewFragment",
				"com.weishang.wxrd.activity.MoreActivity######");
		http.eventList.add(appViewScreen04);
		for (int i = 0; i < info.size(); i++) {
			if (info.getJSONObject(i).getIntValue("status") != 2
					&& info.getJSONObject(i).getString("name").startsWith("task_browse_")) {
				more = info.getJSONObject(i);
				String task_id = more.getString("name").replace("task_browse_", "");
				logger.info("mayitt-{}:开始阅读其它任务 task_id={}", user.getUsername(), task_id);
				JSONObject start = http.adlickstart(task_id);
				if (start.getBooleanValue("success")) {
					JSONObject appViewScreen05 = MayittUtils.AppViewScreen(user,
							"com.weishang.wxrd.activity.MoreActivity|com.weishang.wxrd.ui.WebAdFragment",
							"com.weishang.wxrd.activity.MoreActivity######");
					http.eventList.add(appViewScreen05);
					http.sendData();
					JSONArray rule = start.getJSONObject("items").getJSONArray("rule");
					int read_num = start.getJSONObject("items").getIntValue("read_num");
					int see_num = start.getJSONObject("items").getIntValue("see_num");
					if (rule == null) {
						time = 60 * 1000 + new Random().nextInt(10000);
						Thread.sleep(time);
						JSONObject adlickend = http.adlickend(task_id);
						if (adlickend.getBooleanValue("success")) {
							logger.info("mayitt-{}:阅读其它任务金币成功 task_id={}", user.getUsername(), task_id);
						}
					} else if (see_num > 0) {
						while (read_num < see_num) {
							time = 10 * 1000 + new Random().nextInt(3000);
							Thread.sleep(time);
							JSONObject bannerstatus = http.bannerstatus(task_id);
							if (bannerstatus.getBooleanValue("success")) {
								read_num++;
								logger.info("mayitt-{}:阅读其它任务第{}次成功", user.getUsername(), read_num);
							} else {
								logger.error("mayitt-{}:阅读其它任务第{}次失败 ,跳出", user.getUsername(), read_num);
								return 0;
							}
						}
						JSONObject adlickend = http.adlickend(task_id);
						if (adlickend.getBooleanValue("success")) {
							logger.info("mayitt-{}:阅读其它任务金币成功 task_id={}", user.getUsername(), task_id);
						}
					}
				}
			}
		}
		return 1;
	}

	public int readHongbao(MayittUser user) throws Exception {
		MayittHttp http = MayittHttp.getInstance(user);
		JSONObject taskAppClick = MayittUtils.AppClick(user,
				"com.weishang.wxrd.activity.MainActivity|com.weishang.wxrd.ui.MainFragment",
				"com.weishang.wxrd.activity.MainActivity######", "tv_find_tab", "任务", "TextView");
		http.eventList.add(taskAppClick);
		JSONObject enterTab = MayittUtils.enterTab(user,
				"com.sensorsdata.analytics.android.sdk.SensorsDataAPI$10##run##SensorsDataAPI.java##1979", "任务");
		http.eventList.add(enterTab);
		JSONObject appViewScreen01 = MayittUtils.AppViewScreen(user,
				"com.weishang.wxrd.activity.MainActivity|com.weishang.wxrd.ui.WebViewFragment",
				"com.weishang.wxrd.activity.MainActivity######");
		http.eventList.add(appViewScreen01);
		JSONObject appViewScreen02 = MayittUtils.AppViewScreen(user, "com.weishang.wxrd.activity.MoreActivity",
				"com.weishang.wxrd.activity.MoreActivity######");
		http.eventList.add(appViewScreen02);
		JSONObject appViewScreen03 = MayittUtils.AppViewScreen(user,
				"com.weishang.wxrd.activity.MoreActivity|com.weishang.wxrd.ui.WebViewFragment",
				"com.weishang.wxrd.activity.MoreActivity######");
		http.eventList.add(appViewScreen03);
		int time = 30 * 1000;
		http.sendData();
		JSONObject taskApiData = http.taskApi();
		JSONArray info = taskApiData.getJSONArray("info");
		JSONObject more = null;
		JSONObject appViewScreen04 = MayittUtils.AppViewScreen(user,
				"com.weishang.wxrd.activity.MoreActivity|com.weishang.wxrd.ui.WebViewFragment",
				"com.weishang.wxrd.activity.MoreActivity######");
		http.eventList.add(appViewScreen04);
		for (int i = 0; i < info.size(); i++) {
			if (info.getJSONObject(i).getIntValue("status") != 2
					&& info.getJSONObject(i).getString("name").startsWith("task_browse_")) {
				more = info.getJSONObject(i);
				String task_id = more.getString("name").replace("task_browse_", "");
				logger.info("mayitt-{}:开始阅读红包任务 task_id={}", user.getUsername(), task_id);
				JSONObject start = http.adlickstart(task_id);
				if (start.getBooleanValue("success")) {
					JSONObject appViewScreen05 = MayittUtils.AppViewScreen(user,
							"com.weishang.wxrd.activity.MoreActivity|com.weishang.wxrd.ui.WebAdFragment",
							"com.weishang.wxrd.activity.MoreActivity######");
					http.eventList.add(appViewScreen05);
					http.sendData();
					JSONArray rule = start.getJSONObject("items").getJSONArray("rule");
					int read_num = start.getJSONObject("items").getIntValue("read_num");
					int see_num = start.getJSONObject("items").getIntValue("see_num");
					if (rule == null) {
						time = 60 * 1000 + new Random().nextInt(10000);
						Thread.sleep(time);
						JSONObject adlickend = http.adlickend(task_id);
						if (adlickend.getBooleanValue("success")) {
							logger.info("mayitt-{}:阅读红包任务金币成功 task_id={}", user.getUsername(), task_id);
						}
					} else if (see_num > 0) {
						while (read_num < see_num) {
							time = 10 * 1000 + new Random().nextInt(3000);
							Thread.sleep(time);
							JSONObject bannerstatus = http.bannerstatus(task_id);
							if (bannerstatus.getBooleanValue("success")) {
								read_num++;
								logger.info("mayitt-{}:阅读红包任务第{}次成功", user.getUsername(), read_num);
							} else {
								logger.error("mayitt-{}:阅读红包任务第{}次失败 ,跳出", user.getUsername(), read_num);
								return 0;
							}
						}
						JSONObject adlickend = http.adlickend(task_id);
						if (adlickend.getBooleanValue("success")) {
							logger.info("mayitt-{}:阅读其它任务金币成功 task_id={}", user.getUsername(), task_id);
						}
					}
				}
			}
		}
		return 1;
	}

	public void daka(MayittUser user) {
		MayittHttp http = MayittHttp.getInstance(user);
		boolean dk = http.daka();
		if (!dk)
			http.daka();
	}

	public void checkTask(MayittUser user) {
		MayittHttp http = MayittHttp.getInstance(user);
	}

	public JSONObject cointx(MayittUser user) {
		JSONObject result = new JSONObject(true);
		MayittHttp http = MayittHttp.getInstance(user);
		mayittCoinRecordWyService.updateCoin(user);
		MayittCoinRecord mayittCoinRecord = mayittCoinRecordWyService.queryTodayMyCoin(user.getUsername());
		if ("wx".equals(user.getTxType())) {
			JSONArray txList = http.cointxList("wxpay");
			for (int i = txList.size(); i > 0; i--) {
				JSONObject coin = txList.getJSONObject(i - 1);
				double money = coin.getDoubleValue("money");
				if (money > 5 && mayittCoinRecord.getBalance().doubleValue() >= money) {
					boolean success = http.cointxWx(Double.valueOf(money).intValue());
					if (success) {
						CoinTxRecord record = new CoinTxRecord("mayitt", user.getUsername(), money, user.getTxType(),
								user.getTxUser(), new Date());
						coinTxRecordWyService.insert(record);
						result.put("status", true);
						result.put("msg", money + "元提现成功.");
						return result;
					}
				}
			}
		} else if ("ali".equals(user.getTxType())) {
			JSONArray txList = http.cointxList("alipay");
			for (int i = txList.size(); i > 0; i--) {
				JSONObject coin = txList.getJSONObject(i - 1);
				double money = coin.getDoubleValue("money");
				if (money > 5 && mayittCoinRecord.getBalance().doubleValue() >= money) {
					boolean success = http.cointxAli(Double.valueOf(money).intValue());
					if (success) {
						CoinTxRecord record = new CoinTxRecord("mayitt", user.getUsername(), money, user.getTxType(),
								user.getTxUser(), new Date());
						coinTxRecordWyService.insert(record);
						result.put("status", true);
						result.put("msg", money + "元提现成功.");
						return result;
					}
				}
			}
		}
		result.put("status", false);
		result.put("msg", "没有可以提现的金额.");
		return result;
	}

	@Override
	public Weekend<MayittUser> genSqlExample(MayittUser t) {
		Weekend<MayittUser> w = super.genSqlExample(t);
		WeekendCriteria<MayittUser, Object> c = w.weekendCriteria();
		if (StringUtils.isNotBlank(t.getUsername())) {
			c.andEqualTo(MayittUser::getUsername, t.getUsername());
		}
		if (t.getUsed() != null) {
			c.andEqualTo(MayittUser::getUsed, t.getUsed());
		}
		
		return w;
	}
}
