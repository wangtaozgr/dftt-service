package com.atao.dftt.service;

import java.lang.reflect.InvocationTargetException;
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
import com.atao.base.util.BeanUtils;
import com.atao.base.util.StringUtils;
import com.atao.dftt.http.DtffHttp;
import com.atao.dftt.mapper.DfToutiaoUserMapper;
import com.atao.dftt.model.DfToutiaoUser;

import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

/**
 *
 * @author twang
 */
@Service
public class DfToutiaoUserWyService extends BaseService<DfToutiaoUser> {
	@Resource
	private DfToutiaoUserMapper dfToutiaoUserMapper;

	@Override
	public BaseMapper<DfToutiaoUser> getMapper() {
		return dfToutiaoUserMapper;
	}

	public List<DfToutiaoUser> queryList(DfToutiaoUser dftt) {
		return super.queryList(dftt, null);
	}

	@Transactional
	public String saveDftt(DfToutiaoUser dftt) throws IllegalAccessException, InvocationTargetException {
		logger.info("开始保存设备信息。。。");
		logger.info("imei={}|device={}", dftt.getImei(), dftt.getDevice());
		logger.info("info={}", dftt.getInfo());
		String msg = "";
		if (dftt.getId() != null) {
			super.updateBySelect(dftt);
		} else if (StringUtils.isNotBlank(dftt.getAccid())) {
			DfToutiaoUser t = new DfToutiaoUser();
			t.setAccid(dftt.getAccid());
			t = super.queryOne(t, null);
			logger.info("t是否为空." + t);
			if (t != null) {
				if (t.getError() == 1l) {
					msg = "安装作弊软件";
				}
				BeanUtils.copy(dftt, t);
				super.updateBySelect(t);
			} else {
				super.insert(dftt);
			}
		} else if (StringUtils.isNotBlank(dftt.getImei()) && StringUtils.isNotBlank(dftt.getDevice())) {
			DfToutiaoUser t = new DfToutiaoUser();
			t.setImei(dftt.getImei());
			t.setDevice(dftt.getDevice());
			if (super.queryList(t, null) == null) {
				super.insert(dftt);
			}
		} else {
			super.insert(dftt);
		}
		return msg;
	}

	@Transactional
	public String bindAccId(String accId, String device, String imei) {
		String msg = "";
		DfToutiaoUser t = new DfToutiaoUser();
		t.setImei(imei);
		t.setDevice(device);
		t = super.queryOne(t, null);
		if (t != null) {
			if (t.getError() == 1l) {
				msg = "安装作弊软件";
			}
			t.setAccid(accId);
			super.updateBySelect(t);
		}
		return msg;
	}

	@Transactional
	public String loginReturnMsg(String result, String device, String imei) {
		logger.info("device={}|imei={}|result= {}", device, imei, result);
		String msg = "";
		DfToutiaoUser t = new DfToutiaoUser();
		t.setImei(imei);
		t.setDevice(device);
		t = super.queryOne(t, null);
		t.setResult(result);
		super.updateBySelect(t);
		JSONObject r = JSONObject.parseObject(result);
		JSONObject sm = r.getJSONObject("data").getJSONObject("sm_arr");
		if ("安装作弊软件".equals(sm.getString("description")) || "虚拟机".equals(sm.getString("description"))) {
			msg = sm.getString("description");
			t.setError(1l);
		}
		logger.info("msg={}", msg);
		super.updateBySelect(t);
		return msg;
	}

	public List<DfToutiaoUser> getUsedUser() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		Date today = c.getTime();
		DfToutiaoUser dftt = new DfToutiaoUser();
		dftt.setUsed(true);
		List<DfToutiaoUser> users = super.queryList(dftt, null);
		for (DfToutiaoUser user : users) {
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

	public int readNewsCoin(DfToutiaoUser user) throws Exception {
		DtffHttp df = DtffHttp.getInstance(user);
		int readedNum = 0;
		long readNum = user.getReadNum();
		int time = 60 * 1000;
		logger.info("dftt-{}:readNum={},limitReadNum={}", user.getUsername(), readNum, user.getLimitReadNum());
		while (readNum < user.getLimitReadNum()) {
			JSONObject json = df.getNewsList();
			if (json == null) {
				logger.error("dftt-{}:没有查到新闻列表!", user.getUsername());
				continue;
			}
			JSONArray newsList = json.getJSONArray("data");
			for (int i = 0; i < newsList.size(); i++) {
				JSONObject news = newsList.getJSONObject(i);
				String url = news.getString("url");
				if (StringUtils.isBlank(url) || url.contains("douyin"))
					continue;
				if (!df.custom()) {
					continue;
				}
				Thread.sleep(time);
				JSONObject result = df.readNews(url);
				if (result.getBooleanValue("status")) {
					readNum = result.getIntValue("cur_read_nums");
					readedNum++;
					logger.info("dftt-{}:阅读新闻金币成功  已读次数={}", user.getUsername(), readNum);
					user.setReadNum(readNum);
					user.setReadTime(new Date());
					this.updateBySelect(user);
					time = result.getIntValue("next_timer_duration") * 1000 + new Random().nextInt(15000);
					if (readNum >= user.getLimitReadNum()) {
						return readedNum;
					}
				} else if ("read nums gt max limit!".equals(result.getString("msg"))) {
					readNum = user.getLimitReadNum();
					user.setReadNum(readNum);
					user.setReadTime(new Date());
					this.updateBySelect(user);
					logger.error("dftt-{}:阅读新闻金币失败，错误原因:今天已达到上限", user.getUsername());
					return readedNum;
				} else if (result.getString("msg").contains("sorry,you read too frequently")) {
					logger.error("dftt-{}: 阅读新闻金币失败，错误原因:阅读太快", user.getUsername());
					Thread.sleep(60000);
				}
			}
		}
		return readedNum;
	}

	public int readVideoCoin(DfToutiaoUser user) throws Exception {
		DtffHttp df = DtffHttp.getInstance(user);
		int readedNum = 0;
		long readNum = user.getVReadNum();
		int time = 30 * 1000;
		while (readNum < user.getVLimitReadNum()) {
			JSONObject json = df.videoList();
			if (json == null) {
				logger.error("dftt-{}:没有查到视频列表!", user.getUsername());
				continue;
			}
			JSONArray newsList = json.getJSONArray("data");
			for (int i = 0; i < newsList.size(); i++) {
				JSONObject news = newsList.getJSONObject(i);
				String url = news.getString("url");
				if (StringUtils.isBlank(url) || url.contains("douyin"))
					continue;
				if (!df.custom()) {
					continue;
				}
				Thread.sleep(time);
				JSONObject result = df.readVideo(url);
				if (result.getBooleanValue("status")) {
					readNum = result.getIntValue("cur_read_nums");
					readedNum++;
					logger.info("dftt-{}:阅读视频金币成功  已读次数={}", user.getUsername(), readNum);
					user.setVReadNum(readNum);
					user.setVReadTime(new Date());
					this.updateBySelect(user);
					time = result.getIntValue("next_timer_duration") * 1000 + new Random().nextInt(15000);
					if (readNum >= user.getVLimitReadNum()) {
						return readedNum;
					}
				} else if ("read nums gt max limit!".equals(result.getString("msg"))) {
					logger.error("dftt-{}:阅读视频金币失败，错误原因:今天已达到上限", user.getUsername());
					readNum = user.getVLimitReadNum();
					user.setVReadNum(readNum);
					user.setVReadTime(new Date());
					this.updateBySelect(user);
					return readedNum;
				} else if (result.getString("msg").contains("sorry,you read too frequently")) {
					logger.error("dftt-{}: 阅读视频金币失败，错误原因:阅读太快", user.getUsername());
					Thread.sleep(60000);
				}
			}
		}
		return readedNum;
	}

	public void readPushNews() {
		List<DfToutiaoUser> users = getUsedUser();
		for (DfToutiaoUser user : users) {
			DtffHttp df = DtffHttp.getInstance(user);
			JSONArray array = df.pushNewsList();
			int len = array.size();
			if (array.size() > 8) {
				len = 8;
			}
			for (int i = 0; i < len; i++) {
				String url = array.getJSONObject(i).getString("url");
				if (StringUtils.isNotBlank(url)) {
					String newsid = url.substring(url.lastIndexOf("/") + 1, url.indexOf(".html"));
					JSONObject result = df.readPushNews(newsid);
					/*
					 * if (!result.getBooleanValue("status") && result.getIntValue("err_code") == 3)
					 * { break; }
					 */
				}
			}
		}
	}

	@Override
	public Weekend<DfToutiaoUser> genSqlExample(DfToutiaoUser t) {
		Weekend<DfToutiaoUser> w = super.genSqlExample(t);
		WeekendCriteria<DfToutiaoUser, Object> c = w.weekendCriteria();
		if (StringUtils.isNotBlank(t.getAccid())) {
			c.andEqualTo(DfToutiaoUser::getAccid, t.getAccid());
		}
		if (StringUtils.isNotBlank(t.getImei())) {
			c.andEqualTo(DfToutiaoUser::getImei, t.getImei());
		}
		if (StringUtils.isNotBlank(t.getDevice())) {
			c.andEqualTo(DfToutiaoUser::getDevice, t.getDevice());
		}
		if (StringUtils.isNotBlank(t.getDeviceId())) {
			c.andEqualTo(DfToutiaoUser::getDeviceId, t.getDeviceId());
		}
		if (t.getUsed() != null) {
			c.andEqualTo(DfToutiaoUser::getUsed, t.getUsed());
		}
		
		return w;
	}

}
