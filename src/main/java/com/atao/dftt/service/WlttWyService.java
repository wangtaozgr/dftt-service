package com.atao.dftt.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atao.base.mapper.BaseMapper;
import com.atao.base.service.BaseService;
import com.atao.base.util.StringUtils;
import com.atao.dftt.http.WlttHttp;
import com.atao.dftt.mapper.WlttMapper;
import com.atao.dftt.model.CoinTxRecord;
import com.atao.dftt.model.Wltt;
import com.atao.dftt.util.JkdttUtils;
import com.atao.dftt.util.WlttUtils;
import com.atao.util.DateUtils;

import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

/**
 *
 * @author twang
 */
@Service
public class WlttWyService extends BaseService<Wltt> {

	@Resource
	private WlttMapper wlttMapper;
	@Resource
	private CoinTxRecordWyService coinTxRecordWyService;

	@Override
	public BaseMapper<Wltt> getMapper() {
		return wlttMapper;
	}

	@Async
	public void executeAsyncTask(Integer n) {
		System.out.println(Thread.currentThread().getName() + "异步任务执行：" + n);
		try {
			Thread.sleep(5000);
			throw new IllegalArgumentException("eeeee" + n);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + "异步任务执行 end：" + n);
	}

	@Async
	public Future<String> returnAsyn(Integer n) {
		System.out.println(Thread.currentThread().getName() + "异步任务执行：" + n);
		try {
			Thread.sleep((3 - n) * 10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + "异步任务执行 end：" + n);
		return new AsyncResult<>(String.format("这个是第{%s}个异步调用的证书", n));
	}

	public List<Wltt> queryUsedUsers() {
		Wltt p = new Wltt();
		p.setUsed(true);
		return super.queryList(p, null);
	}

	public Wltt queryUserByUsername(String username) {
		Wltt p = new Wltt();
		p.setUsername(username);
		return super.queryOne(p, null);
	}

	public List<Wltt> getUsedUser() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		Date today = c.getTime();
		Wltt dftt = new Wltt();
		dftt.setUsed(true);
		List<Wltt> users = super.queryList(dftt, null);
		for (Wltt user : users) {
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

	public void daka(Wltt wltt) {
		WlttHttp wlttHttp = WlttHttp.getInstance(wltt);
		JSONObject dk = wlttHttp.daka();
		if (dk == null)
			return;
		if (dk.getIntValue("status") == 1004) {
			JSONObject userInfo = wlttHttp.login();
			String authToken = WlttUtils.getAuthToken(wltt.getDevice(), userInfo.getString("acctk"));
			wltt.setAuthToken(authToken);
			super.updateBySelect(wltt);
			wlttHttp = wlttHttp.refreshUser(wltt);
			wlttHttp.daka();
		}
	}

	public JSONObject cointx(Wltt user) {
		JSONObject result = new JSONObject(true);
		WlttHttp http = WlttHttp.getInstance(user);
		if ("wx".equals(user.getTxType())) {
			JSONArray txList = http.cointxList("wxpay");
			for (int i = txList.size(); i > 0; i--) {
				JSONObject coin = txList.getJSONObject(i - 1);
				double price = coin.getDoubleValue("face_price") / 100;
				if (coin.getBooleanValue("can_buy") && price > 1) {// 微信只提1元以上的，每次都要手动提取，麻烦
					int productId = coin.getIntValue("id");
					boolean success = http.cointx(productId);
					if (success) {
						CoinTxRecord record = new CoinTxRecord("wltt", user.getUsername(), price, user.getTxType(),
								user.getTxUser(), new Date());
						coinTxRecordWyService.insert(record);
						result.put("status", true);
						result.put("msg", price + "元提现成功.");
						return result;
					}
				}
			}
		} else if ("ali".equals(user.getTxType())) {
			JSONArray txList = http.cointxList("alipay");
			for (int i = txList.size(); i > 0; i--) {
				JSONObject coin = txList.getJSONObject(i - 1);
				if (coin.getBooleanValue("can_buy")) {
					int productId = coin.getIntValue("id");
					boolean success = http.cointx(productId);
					if (success) {
						double price = coin.getDoubleValue("face_price") / 100;
						CoinTxRecord record = new CoinTxRecord("wltt", user.getUsername(), price, user.getTxType(),
								user.getTxUser(), new Date());
						coinTxRecordWyService.insert(record);
						result.put("status", true);
						result.put("msg", price + "元提现成功.");
						return result;
					}
				}
			}
		}
		result.put("status", false);
		result.put("msg", "没有可以提现的金额.");
		return result;
	}

	/**
	 * 读取新闻 0:失败 1：成功 3:已到上限
	 * 
	 * @param wltt
	 * @param itemId
	 * @param postId
	 * @return
	 */
	public int readOneNews(Wltt wltt, String itemId, String postId) {
		WlttHttp wlttHttp = WlttHttp.getInstance(wltt);
		if (StringUtils.isBlank(wlttHttp.taskId)) {
			JSONObject result = wlttHttp.firstGetTask(itemId);
			if (result.getIntValue("status") == 1000) {
				return 0;
			} else if (result.getIntValue("status") == 1004) {// 重新登陆
				JSONObject userInfo = wlttHttp.login();
				String authToken = WlttUtils.getAuthToken(wltt.getDevice(), userInfo.getString("acctk"));
				wltt.setAuthToken(authToken);
				super.updateBySelect(wltt);
				wlttHttp = wlttHttp.refreshUser(wltt);

			} else {
				return 3;
			}
		} else {
			wlttHttp.upTask(postId);
			JSONObject object = wlttHttp.readNews(itemId, wlttHttp.taskId, wlttHttp.timestamp);
			if (object.getIntValue("status") == 1000) {
				JSONObject data = object.getJSONObject("data").getJSONObject("next_task");
				if (data != null) {
					wlttHttp.taskId = data.getString("task_id");
					wlttHttp.timestamp = data.getString("timestamp");
				}
				return 1;
			} else if (object.getIntValue("status") == 1002) {
				JSONObject result = wlttHttp.firstGetTask(itemId);
				if (result.getIntValue("status") == 1000) {
					return 0;
				} else if (result.getIntValue("status") == 1004) {// 重新登陆
					JSONObject userInfo = wlttHttp.login();
					String authToken = WlttUtils.getAuthToken(wltt.getDevice(), userInfo.getString("acctk"));
					wltt.setAuthToken(authToken);
					super.updateBySelect(wltt);
					wlttHttp = wlttHttp.refreshUser(wltt);
				} else {
					return 3;
				}
			} else if (object.getIntValue("status") == 1004) {// 重新登陆
				JSONObject userInfo = wlttHttp.login();
				String authToken = WlttUtils.getAuthToken(wltt.getDevice(), userInfo.getString("acctk"));
				wltt.setAuthToken(authToken);
				super.updateBySelect(wltt);
				wlttHttp = wlttHttp.refreshUser(wltt);
			}
		}
		return 0;
	}

	public int readNewsCoin(Wltt user) throws Exception {
		WlttHttp wlttHttp = WlttHttp.getInstance(user);
		String today = DateUtils.formatDate(new Date(), "yyyyMMdd");
		if (user.getQdTime() == null || !today.equals(DateUtils.formatDate(user.getQdTime(), "yyyyMMdd"))) {
			// 开始签到
			boolean qd = wlttHttp.qiandao();
			if (qd) {
				user.setQdTime(new Date());
				super.updateBySelect(user);
			}
		}
		int readedNum = 0;
		long readNum = user.getReadNum();
		int time = 30 * 1000 + new Random().nextInt(15000);
		while (readNum < user.getLimitReadNum() && readedNum < 3) {
			JSONArray newsList = wlttHttp.newsList();
			if (newsList.size() < 1) {
				logger.error("wltt-{}:没有查到新闻列表!", user.getUsername());
				return readedNum;
			}
			JSONArray event_list = new JSONArray();
			JSONObject click = wlttHttp.createEvent("click", "-601", "", "", 28, 11);
			event_list.add(click);
			event_list.add(wlttHttp.retriveEventFromList(newsList, 1));
			event_list.add(wlttHttp.retriveEventFromList(newsList, 2));
			wlttHttp.collectEventLog(event_list);// 发送新闻列表刷新日志
			for (int i = 0; i < newsList.size(); i++) {// 先找礼物
				JSONObject news = newsList.getJSONObject(i);
				if ("giftGold".equals(news.getString("action_type"))) {
					String giftId = news.getJSONObject("gift").getString("id");
					JSONObject object = wlttHttp.readGift(giftId);
					logger.info("wltt-{}:阅读礼物结果={}", user.getUsername(),
							object.getIntValue("status") == 1000 ? "成功" : "失败");
					if (object.getIntValue("status") == 1004) {// 重新登陆
						JSONObject userInfo = wlttHttp.login();
						String authToken = WlttUtils.getAuthToken(user.getDevice(), userInfo.getString("acctk"));
						user.setAuthToken(authToken);
						super.updateBySelect(user);
						wlttHttp = wlttHttp.refreshUser(user);
					}
				}
			}

			for (int i = 0; i < newsList.size(); i++) {
				Thread.sleep(2000);
				event_list = new JSONArray();
				event_list.add(wlttHttp.retriveEventFromList(newsList, i + 3));
				event_list.add(wlttHttp.retriveEventFromList(newsList, i + 4));
				wlttHttp.collectEventLog(event_list);// 发送新闻列表日志
				Thread.sleep(2000);
				JSONObject news = newsList.getJSONObject(i);
				if ("post".equals(news.getString("action_type"))) {
					String itemId = news.getLongValue("item_id") + "";
					String postId = news.getLongValue("post_id") + "";
					wlttHttp.upTask(itemId);
					JSONArray relatedReadNewsList = wlttHttp.getRelatedRead(itemId, postId);
					if (relatedReadNewsList.size() < 1) {
						logger.error("wltt-{}:没有找到新闻详细页面相关阅读，无法发送日志 itemId={}", user.getUsername(), itemId);
						continue;
					}
					Thread.sleep(2000);
					// wlttHttp.ad();// 发送广告
					if (relatedReadNewsList.size() > 0) {
						event_list = wlttHttp.getNewsDetailEventList(relatedReadNewsList, i);
						event_list.add(wlttHttp.convertNesToEvent(relatedReadNewsList.getJSONObject(0), "1.1.1"));
						wlttHttp.collectEventLog(event_list);// 发送新闻详细页面日志
					}
					time = 20 * 1000 + new Random().nextInt(15000);
					Thread.sleep(time);
					int suc = readOneNews(user, itemId, postId);
					if (suc == 1) {
						readNum++;
						readedNum++;
						user.setReadNum(readNum);
						user.setReadTime(new Date());
						this.updateBySelect(user);
						logger.info("wltt-{}:阅读新闻金币成功  已读次数={}", user.getUsername(), readNum);
						if (readNum > user.getLimitReadNum() || readedNum >= 3) {
							return readedNum;
						}
					} else if (suc == 3) {
						readNum = user.getLimitReadNum();
						user.setReadNum(readNum);
						user.setReadTime(new Date());
						this.updateBySelect(user);
						logger.error("wltt-{}:阅读新闻金币报错或已达到上限", user.getUsername());
						return readedNum;
					} else if (suc == 0) {
						logger.info("wltt-{}:第一次获取任务id", user.getUsername());
					}
					Thread.sleep(2000);
					event_list = wlttHttp.getBackNewsListEventList(newsList, i);
					wlttHttp.collectEventLog(event_list);// 发送新闻详细页面日志
					Thread.sleep(2000);
				}
			}
		}
		return readedNum;
	}

	public int readOneVideo(Wltt wltt, String videoId) {
		WlttHttp wlttHttp = WlttHttp.getInstance(wltt);
		if (StringUtils.isBlank(wlttHttp.vTaskId)) {
			boolean s = wlttHttp.firstGetVideoTask(videoId);
			if (!s)
				return 3;
		} else {
			JSONObject object = wlttHttp.readVideos(videoId, wlttHttp.vTaskId, wlttHttp.vTimestamp);
			if (object.getIntValue("status") == 1000) {
				JSONObject data = object.getJSONObject("data").getJSONObject("next_task");
				if (data != null) {
					wlttHttp.vTaskId = data.getString("task_id");
					wlttHttp.vTimestamp = data.getString("timestamp");
				}
				return 1;
			} else if (object.getIntValue("status") == 1002) {
				boolean s = wlttHttp.firstGetVideoTask(videoId);
				if (!s)
					return 3;
			}
		}
		return 0;
	}

	public void readVideoCoin(Wltt user, Date endTime) throws Exception {
		WlttHttp wlttHttp = WlttHttp.getInstance(user);
		int readedNum = 0;
		long readNum = user.getVReadNum();
		int time = 30 * 1000 + new Random().nextInt(15000);
		while (readNum < user.getVLimitReadNum() && new Date().getTime() < endTime.getTime() && readedNum < 3) {
			JSONArray videoList = wlttHttp.videoList();
			if (videoList.size() < 1) {
				logger.error("wltt-{}:没有查到视频列表!", user.getUsername());
				continue;
			}
			for (int i = 0; i < videoList.size(); i++) {
				time = 30 * 1000 + new Random().nextInt(15000);
				Thread.sleep(time);
				JSONObject video = videoList.getJSONObject(i);
				String itemId = video.getString("id");
				String videoId = wlttHttp.queryVideoId(itemId);
				int suc = readOneVideo(user, videoId);
				if (suc == 1) {
					readNum++;
					readedNum++;
					user.setVReadNum(readNum);
					user.setVReadTime(new Date());
					this.updateBySelect(user);
					logger.info("wltt-{}:阅读视频金币成功  已读次数={}", user.getUsername(), readNum);
					if (readedNum >= 3)
						break;
					if (readNum > user.getVLimitReadNum())
						break;
					if (new Date().getTime() > endTime.getTime())
						break;
				} else if (suc == 3) {
					readNum = user.getVLimitReadNum();
					user.setVReadNum(readNum);
					user.setVReadTime(new Date());
					this.updateBySelect(user);
					logger.error("wltt-{}:阅读视频金币报错或已达到上限", user.getUsername());
					break;
				} else if (suc == 0) {
					logger.info("wltt-{}:第一次获取视频任务id", user.getUsername());
				}
			}
		}
	}

	public int searchTask(Wltt user) throws Exception {
		WlttHttp http = WlttHttp.getInstance(user);
		JSONArray event_list = new JSONArray();
		JSONObject click = http.createEvent("click", "-601", "", "", 28, 11);
		event_list.add(click);
		int time = 30 * 1000 + new Random().nextInt(15000);
		JSONObject keywordsJson = http.keywords();
		JSONArray search_task_rewards = keywordsJson.getJSONObject("data").getJSONArray("search_task_rewards");
		JSONArray hot_keywords = keywordsJson.getJSONObject("data").getJSONArray("hot_keywords");
		int totalSearchNum = 0;
		for (int i = 0; i < search_task_rewards.size(); i++) {
			if (search_task_rewards.getJSONObject(i).getIntValue("search_num") > totalSearchNum)
				totalSearchNum = search_task_rewards.getJSONObject(i).getIntValue("search_num");
		}
		int readedNum = keywordsJson.getJSONObject("data").getIntValue("today_search_num");
		if (keywordsJson != null) {
			readedNum = keywordsJson.getJSONObject("data").getIntValue("today_search_num");
			while (readedNum < totalSearchNum) {
				JSONObject page_view = http.createEvent("page_view", "-3", "", "", 28, 2);
				event_list.add(page_view);
				time = 2 * 1000 + new Random().nextInt(1000);
				Thread.sleep(time);
				JSONObject exit = http.createEvent("exit", "-3", "", "", 28, "{\"use_time_ms\":" + time + "}", 2);
				event_list.add(exit);
				Thread.sleep(new Random().nextInt(500));
				page_view = http.createEvent("page_view", "-31", "", "", 28, "{\"source\":1,\"t_s\":0}", 2);
				event_list.add(page_view);
				String[] keywords = JkdttUtils.adtitles;
				String keyword = keywords[new Random().nextInt(keywords.length)];
				String keyword_id = "";
				boolean suc = http.searchStart(keyword, keyword_id);
				if (suc) {
					time = 10 * 1000 + new Random().nextInt(5000);
					Thread.sleep(time);
					suc = http.searchEnd(keyword, keyword_id);
					Thread.sleep(2000);
					keywordsJson = http.keywords();
					readedNum = keywordsJson.getJSONObject("data").getIntValue("today_search_num");
					logger.info("wltt-{}:搜索结果|suc={},readedNum={}", user.getUsername(), suc, readedNum);
					search_task_rewards = keywordsJson.getJSONObject("data").getJSONArray("search_task_rewards");
					for (int i = 0; i < search_task_rewards.size(); i++) {
						if (search_task_rewards.getJSONObject(i).getBooleanValue("can_receive")) {
							int search_num = search_task_rewards.getJSONObject(i).getIntValue("search_num");
							http.finishSearchTask(search_num);
						}
					}
					exit = http.createEvent("exit", "-31", "", "", 28, "{\"use_time_ms\":" + time + "}", 2);
					event_list.add(exit);
					http.collectEventLog(event_list);
				} else {
					return readedNum;
				}
			}
		}
		return readedNum;
	}
	
	/*public int searchTask(Wltt user) throws Exception {
		WlttHttp http = WlttHttp.getInstance(user);
		JSONArray event_list = new JSONArray();
		JSONObject click = http.createEvent("click", "-601", "", "", 28, 11);
		event_list.add(click);
		int time = 30 * 1000 + new Random().nextInt(15000);
		JSONObject keywordsJson = http.keywords();
		JSONArray search_task_rewards = keywordsJson.getJSONObject("data").getJSONArray("search_task_rewards");
		JSONArray hot_keywords = keywordsJson.getJSONObject("data").getJSONArray("hot_keywords");
		int totalSearchNum = 0;
		for (int i = 0; i < search_task_rewards.size(); i++) {
			if (search_task_rewards.getJSONObject(i).getIntValue("search_num") > totalSearchNum)
				totalSearchNum = search_task_rewards.getJSONObject(i).getIntValue("search_num");
		}
		int readedNum = keywordsJson.getIntValue("today_search_num");
		int num = 0;
		if (keywordsJson != null) {
			readedNum = keywordsJson.getJSONObject("data").getIntValue("today_search_num");
			while (readedNum < totalSearchNum) {
				JSONObject page_view = http.createEvent("page_view", "-3", "", "", 28, 2);
				event_list.add(page_view);
				time = 2 * 1000 + new Random().nextInt(1000);
				Thread.sleep(time);
				JSONObject exit = http.createEvent("exit", "-3", "", "", 28, "{\"use_time_ms\":" + time + "}", 2);
				event_list.add(exit);
				Thread.sleep(new Random().nextInt(500));
				page_view = http.createEvent("page_view", "-31", "", "", 28, "{\"source\":1,\"t_s\":0}", 2);
				event_list.add(page_view);
				if (hot_keywords != null && hot_keywords.size() > 0) {
					if (num < hot_keywords.size()) {
						String keyword = hot_keywords.getJSONObject(num).getString("keyword");
						String keyword_id = hot_keywords.getJSONObject(num).getString("id");
						boolean suc = http.searchStart(keyword, keyword_id);
						num++;
						if (suc) {
							time = 10 * 1000 + new Random().nextInt(5000);
							Thread.sleep(time);
							suc = http.searchEnd(keyword, keyword_id);
							Thread.sleep(2000);
							readedNum++;
							logger.info("wltt-{}:搜索结果|suc={},readedNum={}", user.getUsername(), suc, readedNum);
							exit = http.createEvent("exit", "-31", "", "", 28, "{\"use_time_ms\":" + time + "}", 2);
							event_list.add(exit);
							http.collectEventLog(event_list);
						} else {
							return readedNum;
						}
					} else {
						keywordsJson = http.keywords();
						hot_keywords = keywordsJson.getJSONObject("data").getJSONArray("hot_keywords");
						num = 0;
						if (hot_keywords == null || hot_keywords.size() == 0) {
							return readedNum;
						}
					}
				} else {
					return readedNum;
				}
			}
			keywordsJson = http.keywords();
			search_task_rewards = keywordsJson.getJSONObject("data").getJSONArray("search_task_rewards");
			for (int i = 0; i < search_task_rewards.size(); i++) {
				if (search_task_rewards.getJSONObject(i).getBooleanValue("can_receive")) {
					int search_num = search_task_rewards.getJSONObject(0).getIntValue("search_num");
					http.finishSearchTask(search_num);
				}
			}

		}
		return readedNum;
	}*/

	@Override
	public Weekend<Wltt> genSqlExample(Wltt t) {
		Weekend<Wltt> w = super.genSqlExample(t);
		WeekendCriteria<Wltt, Object> c = w.weekendCriteria();
		if (StringUtils.isNotBlank(t.getUsername())) {
			c.andEqualTo(Wltt::getUsername, t.getUsername());
		}
		if (t.getUsed() != null) {
			c.andEqualTo(Wltt::getUsed, t.getUsed());
		}
		return w;
	}
}
