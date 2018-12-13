package com.atao.dftt.service;

import com.atao.dftt.model.TaoToutiaoUser;
import com.atao.dftt.util.TaottUtils;
import com.atao.util.DateUtils;

import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import com.atao.dftt.http.TaottHttp;
import com.atao.dftt.mapper.TaoToutiaoUserMapper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atao.base.mapper.BaseMapper;
import com.atao.base.service.BaseService;
import com.atao.base.util.StringUtils;

import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

/**
 *
 * @author twang
 */
@Service
public class TaoToutiaoUserWyService extends BaseService<TaoToutiaoUser> {

	@Resource
	private TaoToutiaoUserMapper taoToutiaoUserMapper;

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
		}
		return users;
	}

	public void readNewsCoin(TaoToutiaoUser user, Date endTime) throws Exception {
		TaottHttp http = TaottHttp.getInstance(user);
		String today = DateUtils.formatDate(new Date(), "yyyyMMdd");
		if (user.getQdTime() == null || !today.equals(DateUtils.formatDate(user.getQdTime(), "yyyyMMdd"))) {
			JSONArray appStart = TaottUtils.AppStart(user);
			JSONObject task = TaottUtils.AppClick(user, "首页", "任务大厅");
			Thread.sleep(new Random().nextInt(1000 + new Random().nextInt(1000)));
			boolean common = http.commonV3();
			if (!common)
				return;
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
		}
		int readedNum = 0;
		long readNum = user.getReadNum();
		int time = 30 * 1000 + new Random().nextInt(15000);
		while (readNum < user.getLimitReadNum() && new Date().getTime() < endTime.getTime() && readedNum < 10) {
			JSONObject rwObsain = TaottUtils.AppNewsObtain(user, "下拉", "热文");
			http.eventList.add(rwObsain);
			JSONArray newsList = http.newsList();
			if (newsList.size() < 1) {
				logger.info(user.getUsername() + ":没有查到新闻列表!");
				continue;
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
				if (result == null)
					return;
				if (result.getIntValue("code") == 0) {
					readNum = result.getJSONObject("result").getIntValue("totalTimesGot");
					readedNum++;
					logger.info("taott-{}:阅读新闻金币成功  已读次数={}", user.getUsername(), readNum);
					user.setReadNum(readNum);
					user.setReadTime(new Date());
					this.updateBySelect(user);
					if (readNum >= user.getLimitReadNum())
						break;
					if (readedNum >= 10)
						break;
					if (new Date().getTime() > endTime.getTime())
						break;
				} else if (result.getIntValue("code") == 20012) {// 用户登陆过期
					JSONObject loginInfo = http.login();
					if (loginInfo.getIntValue("code") == 0) {
						logger.info("taott-{}:用户登陆已失效，重新登陆成功，更新用户信息", user.getUsername());
						String ticket = loginInfo.getJSONObject("result").getString("ticket");
						user.setTicket(ticket);
						super.updateBySelect(user);
						http.user = user;
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
					break;
				}
			}
		}
	}

	public void daka(TaoToutiaoUser user) {
		TaottHttp http = TaottHttp.getInstance(user);
		boolean dk = http.daka();
	}

	public void checkTask(TaoToutiaoUser user) {
		TaottHttp http = TaottHttp.getInstance(user);
		http.checkTask();
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
