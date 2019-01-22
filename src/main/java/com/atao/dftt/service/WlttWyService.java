package com.atao.dftt.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

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
		if(dk==null) return;
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
				if (coin.getBooleanValue("can_buy") && price > 1) {//微信只提1元以上的，每次都要手动提取，麻烦
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

	public void readNewsCoin(Wltt user, Date endTime) throws Exception {
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
		while (readNum < user.getLimitReadNum() && new Date().getTime() < endTime.getTime() && readedNum < 3) {
			JSONArray newsList = wlttHttp.newsList();
			if (newsList.size() < 1) {
				logger.error("wltt-{}:没有查到新闻列表!", user.getUsername());
				continue;
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
				// wlttHttp.ad();// 发送广告
				event_list = new JSONArray();
				event_list.add(wlttHttp.retriveEventFromList(newsList, i + 3));
				event_list.add(wlttHttp.retriveEventFromList(newsList, i + 4));
				wlttHttp.collectEventLog(event_list);// 发送新闻列表日志
				Thread.sleep(2000);
				JSONObject news = newsList.getJSONObject(i);
				if ("post".equals(news.getString("action_type"))) {
					String itemId = news.getLongValue("item_id") + "";
					String postId = news.getLongValue("post_id") + "";
					JSONArray relatedReadNewsList = wlttHttp.getRelatedRead(itemId, postId);
					wlttHttp.upTask(itemId);
					Thread.sleep(2000);
					// wlttHttp.ad();// 发送广告
					event_list = wlttHttp.getNewsDetailEventList(relatedReadNewsList, i);
					if (relatedReadNewsList.size() > 0) {
						event_list.add(wlttHttp.convertNesToEvent(relatedReadNewsList.getJSONObject(0), "1.1.1"));
					}
					wlttHttp.collectEventLog(event_list);// 发送新闻详细页面日志
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
						if (readedNum >= 3)
							break;
						if (readNum > user.getLimitReadNum())
							break;
						if (new Date().getTime() > endTime.getTime())
							break;
					} else if (suc == 3) {
						readNum = user.getLimitReadNum();
						user.setReadNum(readNum);
						user.setReadTime(new Date());
						this.updateBySelect(user);
						logger.error("wltt-{}:阅读新闻金币报错或已达到上限", user.getUsername());
						break;
					} else if (suc == 0) {
						logger.info("wltt-{}:第一次获取任务id", user.getUsername());
					}
					Thread.sleep(2000);
					event_list = wlttHttp.getBackNewsListEventList(relatedReadNewsList, i);
					wlttHttp.collectEventLog(event_list);// 发送新闻详细页面日志
					Thread.sleep(2000);
				}
			}
		}
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
		w.and(c);
		return w;
	}
}
