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
import com.atao.dftt.http.TaottHttp;
import com.atao.dftt.mapper.TaoToutiaoUserMapper;
import com.atao.dftt.model.CoinTxRecord;
import com.atao.dftt.model.TaoToutiaoUser;
import com.atao.dftt.util.TaottUtils;
import com.atao.util.DateUtils;

import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

/**
 *
 * @author twang
 */
@Service
public class TaoToutiaoUserWyService extends BaseService<TaoToutiaoUser> {

	@Resource
	private TaoToutiaoUserMapper taoToutiaoUserMapper;
	@Resource
	private CoinTxRecordWyService coinTxRecordWyService;

	@Override
	public BaseMapper<TaoToutiaoUser> getMapper() {
		return taoToutiaoUserMapper;
	}

	public List<TaoToutiaoUser> getUsedUser() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		Date today = c.getTime();
		TaoToutiaoUser dftt = new TaoToutiaoUser();
		dftt.setUsed(true);
		List<TaoToutiaoUser> users = super.queryList(dftt, null);
		for (TaoToutiaoUser user : users) {
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

	public TaoToutiaoUser queryUserByUsername(String username) {
		TaoToutiaoUser p = new TaoToutiaoUser();
		p.setUsername(username);
		return super.queryOne(p, null);
	}

	public int readNewsCoin(TaoToutiaoUser user) throws Exception {
		TaottHttp http = TaottHttp.getInstance(user);
		String today = DateUtils.formatDate(new Date(), "yyyyMMdd");
		if (user.getQdTime() == null || !today.equals(DateUtils.formatDate(user.getQdTime(), "yyyyMMdd"))) {
			JSONArray appStart = TaottUtils.AppStart(user);
			JSONObject task = TaottUtils.AppClick(user, "首页", "任务大厅");
			Thread.sleep(new Random().nextInt(1000 + new Random().nextInt(1000)));
			boolean common = http.commonV3();
			if (!common) {
				return 0;
			}
			// 开始签到
			boolean suc = http.qiandao();
			if (suc) {
				http.finishTask("29");// 完成签到任务
				user.setQdTime(new Date());
				super.updateBySelect(user);
			}
			Thread.sleep(new Random().nextInt(1000 + new Random().nextInt(1000)));
			JSONObject index = TaottUtils.AppClick(user, "首页", "新闻赚钱页");
			Thread.sleep(new Random().nextInt(300));
			JSONObject view = TaottUtils.AppPageView(user, "视频feed页", null);
			Thread.sleep(1000 + new Random().nextInt(1000));
			JSONObject rw = TaottUtils.AppNewsObtain(user, "切换", "热文");
			http.eventList.addAll(appStart);
			http.eventList.add(task);
			http.eventList.add(index);
			http.eventList.add(view);
			http.eventList.add(rw);
			// 发送请求
			http.sendData();
			http.times = 0;
			http.pubTime = 0l;
		}
		int readedNum = 0;
		long readNum = user.getReadNum();
		int time = 30 * 1000 + new Random().nextInt(15000);
		while (readNum < user.getLimitReadNum()) {
			JSONObject rwObsain = TaottUtils.AppNewsObtain(user, "下拉", "热文");
			http.eventList.add(rwObsain);
			JSONArray newsList = http.newsList();
			if (newsList.size() < 1) {
				logger.info(user.getUsername() + ":没有查到新闻列表!");
				return readedNum;
			}
			for (int i = 0; i < newsList.size(); i++) {
				time = 20 * 1000 + new Random().nextInt(15000);
				Thread.sleep(time);
				JSONObject news = newsList.getJSONObject(i);
				String newsId = news.getString("id");
				String page_url = news.getString("reportUrl");
				JSONArray readNews = TaottUtils.readNews(user, newsId, page_url);
				http.eventList.addAll(readNews);
				// 发送请求
				http.sendData();
				JSONObject result = http.readNews(newsId);
				if (result == null) {
					return readedNum;
				}
				if (result.getIntValue("code") == 0) {
					readNum = result.getJSONObject("result").getIntValue("totalTimesGot");
					readedNum++;
					logger.info("taott-{}:阅读新闻金币成功  已读次数={}", user.getUsername(), readNum);
					user.setReadNum(readNum);
					user.setReadTime(new Date());
					this.updateBySelect(user);
					if (readNum >= user.getLimitReadNum()) {
						return readedNum;
					}
				} else if (result.getIntValue("code") == 20012) {// 用户登陆过期
					JSONObject loginInfo = http.login();
					if (loginInfo.getIntValue("code") == 0) {
						logger.info("taott-{}:用户登陆已失效，重新登陆成功，更新用户信息", user.getUsername());
						String ticket = loginInfo.getJSONObject("result").getString("ticket");
						user.setTicket(ticket);
						super.updateBySelect(user);
						http = http.refreshUser(user);
					} else {
						readNum = user.getLimitReadNum();
						user.setReadNum(readNum);
						user.setReadTime(new Date());
						this.updateBySelect(user);
						logger.error("taott-{}:用户登陆已失效，登陆失败，设置成最大阅读数", user.getUsername());
					}
				} else {
					readNum = user.getLimitReadNum();
					user.setReadNum(readNum);
					user.setReadTime(new Date());
					this.updateBySelect(user);
					logger.error("taott-{}:阅读新闻金币报错或已达到上限", user.getUsername());
					return readedNum;
				}
			}
		}
		return readedNum;
	}

	public void daka(TaoToutiaoUser user) {
		TaottHttp http = TaottHttp.getInstance(user);
		boolean dk = http.daka();
		if (!dk) {
			JSONObject loginInfo = http.login();
			if (loginInfo.getIntValue("code") == 0) {
				logger.info("taott-{}:用户登陆已失效，重新登陆成功，更新用户信息", user.getUsername());
				String ticket = loginInfo.getJSONObject("result").getString("ticket");
				user.setTicket(ticket);
				super.updateBySelect(user);
				http = http.refreshUser(user);
			}
		}

	}

	public void checkTask(TaoToutiaoUser user) {
		TaottHttp http = TaottHttp.getInstance(user);
		http.checkTask();
	}

	public JSONObject cointx(TaoToutiaoUser user) {
		JSONObject result = new JSONObject(true);
		TaottHttp http = TaottHttp.getInstance(user);
		if ("wx".equals(user.getTxType())) {
			JSONArray txList = http.cointxList("wxpay");
			for (int i = 0; i < txList.size(); i++) {
				JSONObject coin = txList.getJSONObject(i);
				int productId = coin.getIntValue("id");
				JSONObject txDetail = http.cointxDetail(productId);
				boolean txed = txDetail.getBooleanValue("taskFinished");
				if (txed) {
					boolean success = http.cointx(productId);
					if (success) {
						double price = coin.getDoubleValue("price");
						CoinTxRecord record = new CoinTxRecord("taott", user.getUsername(), price, user.getTxType(),
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
			for (int i = 0; i < txList.size(); i++) {
				JSONObject coin = txList.getJSONObject(i);
				int productId = coin.getIntValue("id");
				JSONObject txDetail = http.cointxDetail(productId);
				boolean txed = txDetail.getBooleanValue("taskFinished");
				if (txed) {
					boolean success = http.cointxAli(productId);
					if (success) {
						double price = coin.getDoubleValue("price");
						CoinTxRecord record = new CoinTxRecord("taott", user.getUsername(), price, user.getTxType(),
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

	@Override
	public Weekend<TaoToutiaoUser> genSqlExample(TaoToutiaoUser t) {
		Weekend<TaoToutiaoUser> w = super.genSqlExample(t);
		WeekendCriteria<TaoToutiaoUser, Object> c = w.weekendCriteria();
		if (StringUtils.isNotBlank(t.getUsername())) {
			c.andEqualTo(TaoToutiaoUser::getUsername, t.getUsername());
		}
		if (t.getUsed() != null) {
			c.andEqualTo(TaoToutiaoUser::getUsed, t.getUsed());
		}
		w.and(c);
		return w;
	}
}
