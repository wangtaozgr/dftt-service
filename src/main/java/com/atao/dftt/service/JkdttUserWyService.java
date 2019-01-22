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
import com.atao.dftt.http.JkdttHttp;
import com.atao.dftt.mapper.JkdttUserMapper;
import com.atao.dftt.model.CoinTxRecord;
import com.atao.dftt.model.JkdttCoinRecord;
import com.atao.dftt.model.JkdttUser;
import com.atao.dftt.util.JkdttUtils;
import com.atao.util.DateUtils;
import com.atao.util.StringUtils;

import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

/**
 *
 * @author twang
 */
@Service
public class JkdttUserWyService extends BaseService<JkdttUser> {

	@Resource
	private JkdttUserMapper jkdttUserMapper;
	@Resource
	private JkdttCoinRecordWyService jkdttCoinRecordWyService;
	@Resource
	private CoinTxRecordWyService coinTxRecordWyService;

	@Override
	public BaseMapper<JkdttUser> getMapper() {
		return jkdttUserMapper;
	}

	public List<JkdttUser> getUsedUser() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		Date today = c.getTime();
		JkdttUser dftt = new JkdttUser();
		dftt.setUsed(true);
		List<JkdttUser> users = super.queryList(dftt, null);
		for (JkdttUser user : users) {
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

	public JkdttUser queryUserByUsername(String username) {
		JkdttUser p = new JkdttUser();
		p.setUsername(username);
		return super.queryOne(p, null);
	}

	public void readNewsCoin(JkdttUser user, Date endTime) throws Exception {
		JkdttHttp http = JkdttHttp.getInstance(user);
		String today = DateUtils.formatDate(new Date(), "yyyyMMdd");

		if (user.getQdTime() == null || !today.equals(DateUtils.formatDate(user.getQdTime(), "yyyyMMdd"))) {
			/*
			 * JSONObject loginInfo = http.login(); if (loginInfo != null) { String skey =
			 * loginInfo.getJSONObject("datas").getString("skey"); String bdjssdkId =
			 * loginInfo.getJSONObject("datas").getString("bdjssdkId"); http.bdjssdkId =
			 * bdjssdkId; String spRequestTokenKey = JkdttUtils.getSpRequestTokenKey(skey,
			 * user.getOpenId()); user.setX(spRequestTokenKey); super.updateBySelect(user);
			 * http.user = user; http.page = 1; }
			 */
			// 开始签到
			boolean suc = http.qiandao();
			if (suc) {
				user.setQdTime(new Date());
				super.updateBySelect(user);
			}
		}
		int readedNum = 0;
		long readNum = user.getReadNum();
		long vReadNum = user.getVReadNum();
		int time = 30 * 1000 + new Random().nextInt(15000);
		while ((readNum < user.getLimitReadNum() || vReadNum < user.getVLimitReadNum())
				&& new Date().getTime() < endTime.getTime() && readedNum < 10) {
			JSONArray newsList = http.newsList();
			if (newsList.size() < 1) {
				logger.info(user.getUsername() + ":没有查到新闻列表!");
				continue;
			}
			for (int i = 0; i < newsList.size(); i++) {
				time = 30 * 1000 + new Random().nextInt(15000);
				JSONObject news = newsList.getJSONObject(i);
				if ("article".equals(news.getString("item_type"))) {
					if (readNum >= user.getLimitReadNum())
						continue;
					String newsId = news.getString("art_id");
					String open_url = news.getString("open_url");
					http.newsDetail(open_url);
					http.commentListLog(newsId);
					// 新闻详细里的百度广告日志
					http.reportAd("bd_jssdk", "JSSDK", "", "百度", http.bdjssdkId, "", "", "webview", "");
					http.reportAd("bd_jssdk", "JSSDK", "", "百度", http.bdjssdkId, "", "", "webview", "");
					http.reportAd("bd_jssdk", "JSSDK", "", "百度", http.bdjssdkId, "", "", "webview", "");
					http.reportAd("bd_jssdk", "JSSDK", "", "百度", http.bdjssdkId, "", "", "webview", "");
					http.reportAd("bd_jssdk", "JSSDK", "", "百度", http.bdjssdkId, "", "", "webview", "");
					http.reportAd("bd_jssdk", "JSSDK", "", "百度", http.bdjssdkId, "", "", "webview", "");
					http.reportAd("bd_jssdk", "JSSDK", "", "百度", http.bdjssdkId, "", "", "webview", "");
					Thread.sleep(time);
					JSONObject result = http.readNews(newsId);
					if (result == null)
						return;
					if ("ok".equals(result.getString("ret"))) {
						JSONObject readNewsCount = http.readNewsLog(newsId);
						if ("ok".equals(readNewsCount.getString("ret"))) {
							readNum = 150 - readNewsCount.getJSONObject("datas").getIntValue("artcount");
							vReadNum = 50 - readNewsCount.getJSONObject("datas").getIntValue("videocount");
						}
						readedNum++;
						logger.info("jkdtt-{}:阅读新闻金币成功  已读次数={}", user.getUsername(), readNum);
						user.setReadNum(readNum);
						user.setVReadNum(vReadNum);
						user.setReadTime(new Date());
						this.updateBySelect(user);
						if (readNum >= user.getLimitReadNum())
							break;
						if (readedNum >= 10)
							break;
						if (new Date().getTime() > endTime.getTime())
							break;
					} else if ("R-ART-1004".equals(result.getString("rtn_code"))) {
						logger.error("jkdtt-{}:读取新闻异常 content={}", user.getUsername(), "验证失败,请重启APP");
						JSONObject loginInfo = http.login();
						if (loginInfo != null) {
							String skey = loginInfo.getJSONObject("datas").getString("skey");
							String bdjssdkId = loginInfo.getJSONObject("datas").getString("bdjssdkId");
							if (StringUtils.isBlank(skey)) {
								logger.error("jkdtt-{}:用户帐号异常，可能被封了", user.getUsername());
								user.setUsed(false);
								super.updateBySelect(user);
								return;
							}
							http.bdjssdkId = bdjssdkId;
							String spRequestTokenKey = JkdttUtils.getSpRequestTokenKey(skey, user.getOpenId());
							if (StringUtils.isBlank(spRequestTokenKey)) {
								logger.error("jkdtt-{}:用户帐号异常，可能被封了", user.getUsername());
								user.setUsed(false);
								super.updateBySelect(user);
								return;
							}
							user.setX(spRequestTokenKey);
							super.updateBySelect(user);
							http = http.refreshUser(user);
						}
						return;
					} else if ("R-ART-1000".equals(result.getString("rtn_code"))) {
						logger.error("jkdtt-{}:新闻异常 content={}", user.getUsername(), result);
						continue;
					} else {
						readNum = user.getLimitReadNum();
						user.setReadNum(readNum);
						user.setReadTime(new Date());
						this.updateBySelect(user);
						logger.error("jkdtt-{}:阅读新闻金币报错或已达到上限", user.getUsername());
						break;
					}
				} else if ("advert".equals(news.getString("item_type"))) {
					http.reportAd(news);
				} else if ("video".equals(news.getString("item_type"))) {
					if (vReadNum >= user.getVLimitReadNum())
						continue;
					String newsId = news.getString("art_id");
					JSONObject videoDetail = http.videoDetail(news);
					JSONArray videoAds = videoDetail.getJSONArray("relate");
					for (int v = 0; v < videoAds.size(); v++) {
						if ("advert".equals(news.getString("item_type"))) {
							http.reportAd(news);
						}
					}
					Thread.sleep(time);
					JSONObject artinfo = videoDetail.getJSONObject("artinfo");
					JSONObject result = http.readVideo(newsId, artinfo.getString("securitykey"));
					if (result == null)
						return;
					if ("ok".equals(result.getString("ret"))) {
						JSONObject readNewsCount = http.readNewsLog(newsId);
						if ("ok".equals(readNewsCount.getString("ret"))) {
							readNum = 150 - readNewsCount.getJSONObject("datas").getIntValue("artcount");
							vReadNum = 50 - readNewsCount.getJSONObject("datas").getIntValue("videocount");
						}
						readedNum++;
						logger.info("jkdtt-{}:阅读视频金币成功  已读次数={}", user.getUsername(), readNum);
						user.setReadNum(readNum);
						user.setVReadNum(vReadNum);
						user.setVReadTime(new Date());
						this.updateBySelect(user);
						if (vReadNum >= user.getVLimitReadNum())
							break;
						if (readedNum >= 10)
							break;
						if (new Date().getTime() > endTime.getTime())
							break;
					}
				}

			}
		}
	}

	public void daka(JkdttUser user) {
		JkdttHttp http = JkdttHttp.getInstance(user);
		boolean dk = http.daka();
	}

	/**
	 * 大于十元就提现
	 * 
	 * @param user
	 * @return
	 */
	public JSONObject cointx(JkdttUser user) {
		JSONObject result = new JSONObject(true);
		JkdttHttp http = JkdttHttp.getInstance(user);
		if ("wx".equals(user.getTxType())) {
			String coinDay = DateUtils.formatDate(new Date(), "yyyyMMdd");
			jkdttCoinRecordWyService.updateCoin(user);
			JkdttCoinRecord t = new JkdttCoinRecord();
			t.setCoinDay(coinDay);
			t.setUsername(user.getUsername());
			JkdttCoinRecord jkdttCoinRecord = jkdttCoinRecordWyService.queryOne(t, null);
			if (jkdttCoinRecord.getBalance() >= 10) {
				double price = 10d;
				boolean txed = http.cointx();
				if (txed) {
					CoinTxRecord record = new CoinTxRecord("jkdtt", user.getUsername(), price, user.getTxType(),
							user.getTxUser(), new Date());
					coinTxRecordWyService.insert(record);
					result.put("status", true);
					result.put("msg", price + "元提现成功.");
					return result;
				}
			}
		}
		result.put("status", false);
		result.put("msg", "没有可以提现的金额.");
		return result;
	}

	@Override
	public Weekend<JkdttUser> genSqlExample(JkdttUser t) {
		Weekend<JkdttUser> w = super.genSqlExample(t);
		WeekendCriteria<JkdttUser, Object> c = w.weekendCriteria();
		if (StringUtils.isNotBlank(t.getUsername())) {
			c.andEqualTo(JkdttUser::getUsername, t.getUsername());
		}
		if (t.getUsed() != null) {
			c.andEqualTo(JkdttUser::getUsed, t.getUsed());
		}
		w.and(c);
		return w;
	}

}
