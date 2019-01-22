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
import com.atao.dftt.http.HsttHttp;
import com.atao.dftt.mapper.HsttUserMapper;
import com.atao.dftt.model.HsttUser;
import com.atao.dftt.model.JkdttCoinRecord;
import com.atao.dftt.util.HsttUtils;
import com.atao.util.DateUtils;
import com.atao.util.StringUtils;

import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

/**
 *
 * @author twang
 */
@Service
public class HsttUserWyService extends BaseService<HsttUser> {

	@Resource
	private HsttUserMapper hsttUserMapper;
	@Resource
	private HsttCoinRecordWyService hsttCoinRecordWyService;
	private static int readMaxNum = 20;

	@Override
	public BaseMapper<HsttUser> getMapper() {
		return hsttUserMapper;
	}

	public List<HsttUser> getUsedUser() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		Date today = c.getTime();
		HsttUser dftt = new HsttUser();
		dftt.setUsed(true);
		List<HsttUser> users = super.queryList(dftt, null);
		for (HsttUser user : users) {
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

	public HsttUser queryUserByUsername(String username) {
		HsttUser p = new HsttUser();
		p.setUsername(username);
		return super.queryOne(p, null);
	}

	public void readNewsCoin(HsttUser user, Date endTime) throws Exception {
		HsttHttp http = HsttHttp.getInstance(user);
		http.getApp();
		String today = DateUtils.formatDate(new Date(), "yyyyMMdd");
		if (user.getQdTime() == null || !today.equals(DateUtils.formatDate(user.getQdTime(), "yyyyMMdd"))) {
			// 开始签到
			boolean suc = http.qiandao();
			if (suc) {
				http.readHotWordNum = 0;
				http.times = 0;
				http.timeswatch = 0;
				http.s = 1;
				http.readBaozNum = 0;
				user.setQdTime(new Date());
				super.updateBySelect(user);
			}
		}

		// 上报启动日志
		int runtimes = 20 * 60 * 1000 + new Random().nextInt(15000);
		JSONArray logDatas = HsttUtils.startApp(runtimes);
		String m = HsttUtils.getMobRunM(user);
		long appStartTime = System.currentTimeMillis();
		http.moblog4(m);
		http.mobCdata(logDatas);
		http.userBehavior("", "article_type");
		http.userBehavior("", "article_type");
		http.slide("sliding_channel");
		http.adsV3();
		JSONArray newsList = http.newsList("1");

		int readedNum = 0;
		long readNum = user.getReadNum();
		int time = 30 * 1000 + new Random().nextInt(15000);
		while (readNum < user.getLimitReadNum() && new Date().getTime() < endTime.getTime() && readedNum < readMaxNum) {
			http.userBehavior("", "article_channel");
			http.slide("drop_down_refresh");
			http.adsV3();
			newsList = http.newsList("1");
			http.slide("drop_down_refresh");
			http.adsV3();
			http.newsList("2");
			if (newsList.size() < 1) {
				logger.info(user.getUsername() + ":没有查到新闻列表!");
				return;
			}
			// 上报无线网日志
			logDatas = HsttUtils.wifiScan();
			http.mobCdata(logDatas);
			Date startReadTime = null;
			Date endReadTime = null;
			for (int i = 0; i < newsList.size(); i++) {
				Thread.sleep(2000);
				JSONObject news = newsList.getJSONObject(i);
				String newsId = news.getString("id");
				if ("推荐".equals(news.getString("category")))
					continue;
				http.userBehavior("", "article");
				startReadTime = new Date();
				JSONObject timer = http.timercount(http.times);
				http.times = timer.getIntValue("times");
				// logger.info("hstt-{}:进入新闻的阅读时间times={}", user.getUsername(), http.times);
				for (int t = 0; t < 5; t++) {
					time = 5 * 1000 + new Random().nextInt(2000);
					Thread.sleep(time);
					String sensor = HsttUtils.getSensor();
					http.userBehavior(sensor, "article_stop_in");
				}
				if (new Date().getTime() >= endTime.getTime()) {
					long appEndTime = System.currentTimeMillis();
					long appTime = (appEndTime - appStartTime) / 1000;
					m = HsttUtils.getMobExtM(user, appTime);
					http.moblog4(m);
					return;
				}
				JSONObject result = http.readNews(newsId);
				http.userBehavior("", "reading_30_seconds");
				if (result.getIntValue("status") == 100) {
					readedNum++;
					readNum = result.getIntValue("readcount");
					logger.info("hstt-{}:阅读新闻金币成功  已读次数={}", user.getUsername(), readNum);
					user.setReadNum(readNum);
					user.setReadTime(new Date());
					this.updateBySelect(user);
					time = 5 * 1000 + new Random().nextInt(2000);
					Thread.sleep(time);
					http.times = time / 3;
					endReadTime = new Date();
					long secs = (endReadTime.getTime() - startReadTime.getTime()) / 1000;
					// logger.info("hstt-{}:secs={}", user.getUsername(), secs);
					http.artcileread(newsId, secs);
					http.timercount2(http.times);

					if (http.readBaozNum < 20 && readNum % 4 == 0) {
						http.userBehavior("", "fudai");
						JSONObject box = http.openBox();
						if (box.getIntValue("status") == 100) {
							http.readBaozNum = box.getIntValue("times");
						} else if (box.getString("msg").contains("今日已领取20个小宝藏")) {
							logger.info("hstt-{}: content={}", user.getUsername(), box.getString("msg"));
							http.readBaozNum = 20;
						} else {
							logger.error("hstt-{}: content={}", user.getUsername(), box);
						}
					}
					// logger.info("hstt-{}:离开新闻的阅读时间times={}", user.getUsername(), http.times);
					if (readNum >= user.getLimitReadNum() || readedNum >= readMaxNum
							|| new Date().getTime() > endTime.getTime()) {
						long appEndTime = System.currentTimeMillis();
						long appTime = (appEndTime - appStartTime) / 1000;
						m = HsttUtils.getMobExtM(user, appTime);
						http.moblog4(m);
						return;
					}
				} else {
					logger.error("hstt-{}:阅读新闻金币失败  result={}", user.getUsername(), result);
					long appEndTime = System.currentTimeMillis();
					long appTime = (appEndTime - appStartTime) / 1000;
					m = HsttUtils.getMobExtM(user, appTime);
					http.moblog4(m);
					return;
				}

			}
		}
	}

	public void readVideoCoin(HsttUser user, Date endTime) throws Exception {
		HsttHttp http = HsttHttp.getInstance(user);
		http.getApp();
		// 上报启动日志
		int runtimes = 20 * 60 * 1000 + new Random().nextInt(15000);
		JSONArray logDatas = HsttUtils.startApp(runtimes);
		String m = HsttUtils.getMobRunM(user);
		long appStartTime = System.currentTimeMillis();
		http.moblog4(m);
		http.mobCdata(logDatas);
		http.userBehavior("", "article_type");
		http.userBehavior("", "article_type");
		http.slide("sliding_channel");
		http.adsV3();
		JSONArray newsList = http.newsList("1");

		int readedNum = 0;
		long readNum = user.getVReadNum();
		int time = 30 * 1000 + new Random().nextInt(15000);
		while (readNum < user.getVLimitReadNum() && new Date().getTime() < endTime.getTime()
				&& readedNum < readMaxNum) {
			http.userBehavior("", "video_channel");
			http.videoAds();
			newsList = http.videoList(1001);
			http.videoAds();
			http.videoList(1016);
			if (newsList.size() < 1) {
				logger.info(user.getUsername() + ":没有查到视频列表!");
				continue;
			}
			Date startReadTime = null;
			Date endReadTime = null;
			for (int i = 0; i < newsList.size(); i++) {
				Thread.sleep(2000);
				JSONObject news = newsList.getJSONObject(i);
				if (!"video".equals(news.getString("type")))
					continue;
				news = JSONObject.parseObject(news.getString("data"));
				String newsId = news.getString("id");
				http.userBehavior("", "video");
				startReadTime = new Date();
				JSONObject timer = http.timercountwatch(http.timeswatch);
				http.timeswatch = timer.getIntValue("times");
				logger.info("hstt-{}:进入视频的阅读时间times={}", user.getUsername(), http.timeswatch);
				time = 30 * 1000;
				Thread.sleep(time-http.timeswatch*3);
				if (new Date().getTime() >= endTime.getTime()) {
					long appEndTime = System.currentTimeMillis();
					long appTime = (appEndTime - appStartTime) / 1000;
					m = HsttUtils.getMobExtM(user, appTime);
					http.moblog4(m);
					return;
				}
				JSONObject result = http.readVideo(newsId);
				// http.userBehavior("", "reading_30_seconds");
				if (result.getIntValue("status") == 100) {
					readedNum++;
					readNum = result.getIntValue("readcount");
					logger.info("hstt-{}:阅读视频金币成功  已读次数={}", user.getUsername(), readNum);
					user.setVReadNum(readNum);
					user.setVReadTime(new Date());
					this.updateBySelect(user);
					time = 5 * 1000 + new Random().nextInt(2000);
					Thread.sleep(time);
					http.timeswatch = time / 3;
					endReadTime = new Date();
					long secs = (endReadTime.getTime() - startReadTime.getTime()) / 1000;
					logger.info("hstt-{}:secs={}", user.getUsername(), secs);
					http.timercount2watch(http.timeswatch);
					http.timercount2watch(http.timeswatch + new Random().nextInt(100));
					logger.info("hstt-{}:离开视频的阅读时间times={}", user.getUsername(), http.timeswatch);
					if (readNum >= user.getVLimitReadNum() || readedNum >= readMaxNum
							|| new Date().getTime() > endTime.getTime()) {
						long appEndTime = System.currentTimeMillis();
						long appTime = (appEndTime - appStartTime) / 1000;
						m = HsttUtils.getMobExtM(user, appTime);
						http.moblog4(m);
						return;
					}
				} else {
					logger.error("hstt-{}:阅读视频金币失败  result={}", user.getUsername(), result);
					long appEndTime = System.currentTimeMillis();
					long appTime = (appEndTime - appStartTime) / 1000;
					m = HsttUtils.getMobExtM(user, appTime);
					http.moblog4(m);
					return;
				}

			}
		}
	}

	public void readHotWord(HsttUser user) throws Exception {
		int time = 2 * 1000;
		HsttHttp http = HsttHttp.getInstance(user);
		http.getApp();
		http.userBehavior("", "task_center");
		Thread.sleep(time);
		http.userBehavior("", "task_center_task");
		JSONObject hotwords = http.hotwordV2();
		if (hotwords == null)
			return;
		while (http.readHotWordNum < 30) {
			JSONArray keywords = hotwords.getJSONArray("keywords");
			if (keywords != null && keywords.size() > 0) {
				http.userBehavior("", "red_search");
				time = 20 * 1000 + new Random().nextInt(2000);
				Thread.sleep(time);
				JSONObject search = http.searchV2();
				if (search.getIntValue("status") == 100) {
					http.readHotWordNum = search.getIntValue("search_count");
					logger.info("hstt-{}:阅读热门单词金币成功  已读次数={}", user.getUsername(), http.readHotWordNum);
				}
			}
		}

	}

	public void searchreward(HsttUser user) throws Exception {
		HsttHttp http = HsttHttp.getInstance(user);
		http.searchrewardV3(1);
		http.userBehavior("", "search_reward");
		int time = 2 * 1000 + new Random().nextInt(2000);
		Thread.sleep(time);
		http.searchrewardV3(2);
		http.userBehavior("", "search_reward");
		time = 2 * 1000 + new Random().nextInt(2000);
		Thread.sleep(time);
		http.searchrewardV3(3);
		http.userBehavior("", "search_reward");
	}

	public void daka(HsttUser user) {
		HsttHttp http = HsttHttp.getInstance(user);
		http.userBehavior("", "top_right_corner");
		boolean dk = http.daka();
		http.userBehavior("", "home_whole_point");
	}

	/**
	 * 大于十元就提现
	 * 
	 * @param user
	 * @return
	 */
	public JSONObject cointx(HsttUser user) {
		JSONObject result = new JSONObject(true);
		HsttHttp http = HsttHttp.getInstance(user);
		if ("wx".equals(user.getTxType())) {
			String coinDay = DateUtils.formatDate(new Date(), "yyyyMMdd");
			hsttCoinRecordWyService.updateCoin(user);
			JkdttCoinRecord t = new JkdttCoinRecord();
			t.setCoinDay(coinDay);
			t.setUsername(user.getUsername());

		}
		result.put("status", false);
		result.put("msg", "没有可以提现的金额.");
		return result;
	}

	@Override
	public Weekend<HsttUser> genSqlExample(HsttUser t) {
		Weekend<HsttUser> w = super.genSqlExample(t);
		WeekendCriteria<HsttUser, Object> c = w.weekendCriteria();
		if (StringUtils.isNotBlank(t.getUsername())) {
			c.andEqualTo(HsttUser::getUsername, t.getUsername());
		}
		if (t.getUsed() != null) {
			c.andEqualTo(HsttUser::getUsed, t.getUsed());
		}
		w.and(c);
		return w;
	}

}
