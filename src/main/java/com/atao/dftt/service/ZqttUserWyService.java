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
import com.atao.dftt.http.ZqttHttp;
import com.atao.dftt.mapper.ZqttUserMapper;
import com.atao.dftt.model.CoinTxRecord;
import com.atao.dftt.model.ZqttCoinRecord;
import com.atao.dftt.model.ZqttUser;
import com.atao.dftt.util.ZqttUtils;
import com.atao.util.DateUtils;

import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

/**
 *
 * @author twang
 */
@Service
public class ZqttUserWyService extends BaseService<ZqttUser> {

	@Resource
	private ZqttUserMapper zqttUserMapper;
	@Resource
	private ZqttCoinRecordWyService zqttCoinRecordWyService;
	@Resource
	private CoinTxRecordWyService coinTxRecordWyService;

	@Override
	public BaseMapper<ZqttUser> getMapper() {
		return zqttUserMapper;
	}

	public List<ZqttUser> getUsedUser() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		Date today = c.getTime();
		ZqttUser dftt = new ZqttUser();
		dftt.setUsed(true);
		List<ZqttUser> users = super.queryList(dftt, null);
		for (ZqttUser user : users) {
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

	public ZqttUser queryUserByUsername(String username) {
		ZqttUser p = new ZqttUser();
		p.setUsername(username);
		return super.queryOne(p, null);
	}

	public void readNewsCoin(ZqttUser user, Date endTime) throws Exception {
		ZqttHttp http = ZqttHttp.getInstance(user);
		http.start();
		http.signature();
		super.save(http.user);
		String today = DateUtils.formatDate(new Date(), "yyyyMMdd");
		JSONArray appStart = ZqttUtils.AppStart(user);
		http.eventList.addAll(appStart);
		Thread.sleep(new Random().nextInt(1000 + new Random().nextInt(1000)));
		if (user.getQdTime() == null || !today.equals(DateUtils.formatDate(user.getQdTime(), "yyyyMMdd"))) {
			// 开始签到
			JSONObject myClick = ZqttUtils.AppClick(user, "com.weishang.wxrd.activity.MainActivity",
					"com.weishang.wxrd.activity.MainActivity######", null, "我的", "android.widget.RelativeLayout");
			http.eventList.add(myClick);
			JSONObject my = ZqttUtils.enterTab(user,
					"com.sensorsdata.analytics.android.sdk.SensorsDataAPI$10##run##SensorsDataAPI.java##2098", "我的");
			http.eventList.add(my);
			Thread.sleep(new Random().nextInt(2000));
			JSONObject taskClick = ZqttUtils.AppClick(user, "com.weishang.wxrd.activity.MainActivity",
					"com.weishang.wxrd.activity.MainActivity######", "ll_container", "任务中心-签到、做任务领青豆",
					"com.weishang.wxrd.widget.DivideRelativeLayout");
			http.eventList.add(taskClick);
			JSONObject task = ZqttUtils.AppViewScreen(user, "com.weishang.wxrd.activity.MoreActivity",
					"com.weishang.wxrd.activity.MoreActivity######");
			http.eventList.add(task);
			boolean suc = http.qiandao();
			// if (suc) {//失败也不再签到
			user.setQdTime(new Date());
			super.updateBySelect(user);
			// }
			JSONObject indexClick = ZqttUtils.AppClick(user,
					"com.weishang.wxrd.activity.MainActivity|com.weishang.wxrd.ui.MainFragment",
					"com.weishang.wxrd.activity.MainActivity######", "tv_home_tab", "首页", "TextView");
			http.eventList.add(indexClick);
			JSONObject index = ZqttUtils.enterTab(user,
					"com.sensorsdata.analytics.android.sdk.SensorsDataAPI$10##run##SensorsDataAPI.java##2098", "首页");
			http.eventList.add(index);
		}
		// 发送请求
		http.sendData();
		int readedNum = 0;
		long readNum = user.getReadNum();
		int time = 30 * 1000 + new Random().nextInt(15000);
		while (readNum < user.getLimitReadNum() && new Date().getTime() < endTime.getTime() && readedNum < 10) {
			JSONObject indexFresh = ZqttUtils.AppClick(user,
					"com.weishang.wxrd.activity.MainActivity|com.weishang.wxrd.ui.MainFragment",
					"com.weishang.wxrd.activity.MainActivity######", "tv_home_tab", "刷新", "TextView");
			http.eventList.add(indexFresh);
			JSONObject enterTab = ZqttUtils.enterTab(user, "首页",
					"com.sensorsdata.analytics.android.sdk.SensorsDataAPI$10##run##SensorsDataAPI.java##2098");
			http.eventList.add(enterTab);
			JSONArray newsList = http.newsList();
			if (newsList.size() < 1) {
				logger.info(user.getUsername() + ":没有查到新闻列表!");
				continue;
			}
			for (int i = 0; i < newsList.size(); i++) {
				JSONObject news = newsList.getJSONObject(i);
				if (news.getIntValue("article_type") == 0) {
					String newsId = news.getString("id");
					String title = news.getString("title");
					String account_name = news.getString("account_name");
					String read_num = news.getString("read_num");
					String catid = news.getString("catid");
					JSONObject gotoNews = http.gotoNews(newsId, catid);

					JSONObject appViewScreen06 = ZqttUtils.AppViewScreen(user,
							"com.weishang.wxrd.activity.MainActivity", "com.weishang.wxrd.activity.MainActivity######");
					http.eventList.add(appViewScreen06);
					time = 30 * 1000 + new Random().nextInt(5000);
					Thread.sleep(time);

					JSONArray readNews = ZqttUtils.readNews(user, newsId, title, account_name, read_num);
					http.eventList.addAll(readNews);
					http.sendData();
					time = new Random().nextInt(2000);
					if (new Date().getTime() >= endTime.getTime()) {
						JSONObject appEnd = ZqttUtils.appEnd(user);
						http.eventList.add(appEnd);
						return;
					}
					JSONObject result = http.readNews(newsId);
					if (result.getBooleanValue("success")) {
						readNum++;
						readedNum++;
						logger.info("zqtt-{}:阅读新闻金币成功  已读次数={}", user.getUsername(), readNum);
						user.setReadNum(readNum);
						user.setReadTime(new Date());
						this.updateBySelect(user);
						if (gotoNews.getBooleanValue("success")) {
							String rid = gotoNews.getString("rid");
							http.leaveNews(newsId, rid);
							http.article_collect(newsId);
							if (i % 2 == 0)
								http.user_readtime(60 + new Random().nextInt(5));
						}
						if (readNum >= user.getLimitReadNum() || readedNum >= 10
								|| new Date().getTime() > endTime.getTime()) {
							JSONObject appEnd = ZqttUtils.appEnd(user);
							http.eventList.add(appEnd);
							return;
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
						logger.error("zqtt-{}:阅读新闻金币报错或已达到上限", user.getUsername());
						JSONObject appEnd = ZqttUtils.appEnd(user);
						http.eventList.add(appEnd);
						return;
					}
				}
			}
		}
	}

	public void readVideoCoin(ZqttUser user, Date endTime) throws Exception {
		ZqttHttp http = ZqttHttp.getInstance(user);
		int readedNum = 0;
		long readNum = user.getVReadNum();
		int time = 30 * 1000 + new Random().nextInt(15000);
		JSONObject videoAppClick = ZqttUtils.AppClick(user,
				"com.weishang.wxrd.activity.MainActivity|com.weishang.wxrd.ui.MainFragment",
				"com.weishang.wxrd.activity.MainActivity######", "tv_find_tab", "视频", "TextView");
		http.eventList.add(videoAppClick);
		JSONObject enterTab = ZqttUtils.enterTab(user,
				"com.sensorsdata.analytics.android.sdk.SensorsDataAPI$10##run##SensorsDataAPI.java##2098", "视频");
		http.eventList.add(enterTab);
		JSONObject appViewScreen01 = ZqttUtils.AppViewScreen(user,
				"com.weishang.wxrd.activity.MainActivity|com.weishang.wxrd.ui.HomeFragment",
				"com.weishang.wxrd.activity.MainActivity######");
		http.eventList.add(appViewScreen01);

		while (readNum < user.getVLimitReadNum() && new Date().getTime() < endTime.getTime() && readedNum < 10) {
			JSONArray newsList = http.videoList();
			if (newsList.size() < 1) {
				logger.info(user.getUsername() + ":没有查到视频列表!");
				continue;
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
					http.sendData();
					time = new Random().nextInt(2000);
					JSONObject result = http.readVideo(newsId);
					if (result == null)
						return;
					if (result.getBooleanValue("success")) {
						readNum++;
						readedNum++;
						logger.info("zqtt-{}:阅读视频金币成功  已读次数={}", user.getUsername(), readNum);
						user.setVReadNum(readNum);
						user.setVReadTime(new Date());
						this.updateBySelect(user);
						if (readNum >= user.getVLimitReadNum())
							break;
						if (readedNum >= 10)
							break;
						if (new Date().getTime() > endTime.getTime())
							break;
					} else if (result.getIntValue("error_code") == 200001) {
						continue;
					} else if (result.getIntValue("error_code") == 10017) {
						continue;
					} else {
						readNum = user.getVLimitReadNum();
						user.setVReadNum(readNum);
						user.setVReadTime(new Date());
						this.updateBySelect(user);
						logger.error("zqtt-{}:阅读视频金币报错或已达到上限", user.getUsername());
						break;
					}
				}
			}
		}
	}

	public void daka(ZqttUser user) {
		ZqttHttp http = ZqttHttp.getInstance(user);
		boolean dk = http.daka();
		if (!dk)
			http.daka();
	}

	public void checkTask(ZqttUser user) {
		ZqttHttp http = ZqttHttp.getInstance(user);
	}

	public JSONObject cointx(ZqttUser user) {
		JSONObject result = new JSONObject(true);
		ZqttHttp http = ZqttHttp.getInstance(user);
		ZqttCoinRecord mayittCoinRecord = zqttCoinRecordWyService.queryTodayMyCoin(user.getUsername());
		if ("wx".equals(user.getTxType())) {
			JSONArray txList = http.cointxList("wxpay");
			for (int i = txList.size(); i > 0; i--) {
				JSONObject coin = txList.getJSONObject(i - 1);
				double money = coin.getDoubleValue("money");
				if (money > 5 && mayittCoinRecord.getBalance().doubleValue() >= money) {
					boolean success = http.cointxWx(Double.valueOf(money).intValue());
					if (success) {
						CoinTxRecord record = new CoinTxRecord("zqtt", user.getUsername(), money, user.getTxType(),
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
						CoinTxRecord record = new CoinTxRecord("zqtt", user.getUsername(), money, user.getTxType(),
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
	public Weekend<ZqttUser> genSqlExample(ZqttUser t) {
		Weekend<ZqttUser> w = super.genSqlExample(t);
		WeekendCriteria<ZqttUser, Object> c = w.weekendCriteria();
		if (StringUtils.isNotBlank(t.getUsername())) {
			c.andEqualTo(ZqttUser::getUsername, t.getUsername());
		}
		if (t.getUsed() != null) {
			c.andEqualTo(ZqttUser::getUsed, t.getUsed());
		}
		w.and(c);
		return w;
	}
}
