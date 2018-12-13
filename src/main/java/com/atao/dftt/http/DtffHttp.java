package com.atao.dftt.http;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atao.dftt.model.DfToutiaoUser;
import com.atao.dftt.util.CommonUtils;
import com.atao.dftt.util.MobileHttpUrlConnectUtils;
import com.atao.dftt.util.SignHelper;
import com.atao.util.DateUtils;

public class DtffHttp {
	private static Logger logger = LoggerFactory.getLogger(DtffHttp.class);
	private static Map<String, DtffHttp> dfttMap = new HashMap<String, DtffHttp>();
	private DfToutiaoUser user;
	public static String version = "2.2.7";
	public static String appversion = "020207";
	private String lastparam = "null|null|null";
	private String startkey = "|||||||||||||";
	private String newkey = "|";
	private int pgnum = -1;
	private int idx = 0;

	public DtffHttp(DfToutiaoUser user) {
		this.user = user;
	}

	public static synchronized DtffHttp getInstance(DfToutiaoUser user) {
		DtffHttp userHttp = (DtffHttp) dfttMap.get(user.getUsername());
		if (userHttp == null) {
			userHttp = new DtffHttp(user);
			dfttMap.put(user.getUsername(), userHttp);
		}
		return userHttp;
	}

	public static void main(String[] args) {
		DfToutiaoUser user = new DfToutiaoUser();
		user.setUsername("17755117870");
		user.setAccid("809695873");
		user.setImei("99001065551084");
		user.setMachine("MI 5X");
		user.setQid("xiaomi181122");
		DtffHttp dftt = DtffHttp.getInstance(user);
		dftt.readPushNews("n181122095515101");

		// dftt.getNewsList();
		/*
		 * for(int i=0;i<30;i++) dftt.sendMsg();
		 */

	}

	public String sendChangeMobileMsg() {
		String url = "https://additional.dftoutiao.com/change_mobile/send_sms";
		TreeMap<String, String> paramTreeMap = new TreeMap<String, String>();
		paramTreeMap.put("token",
				"M3pYa2c0UyszYVhyY3R1VTZRbkpxMm1WQnVIcExGMC9hQUNoT3FuOWN0VmQ4S3VldzFLR2ZGTEkxRERBQTZTWTVoS20yV0t6RldTelR4OVdSTTNaczBjRW96NUQxNmg0QnBxdzNmNmw2bCs3REs2MHduZE9rM2JNdHpYa0ZoUmUvOWRlRmNicmFXb0xha1FPdkVlS0ZIZW1lQmNnaEg4dUVYeWZmRm5tazRvPQ==");
		paramTreeMap.put("mobile", "13865429076");
		paramTreeMap.put("agree", "");
		paramTreeMap.put("review", "");
		paramTreeMap.put("cashin", "");
		String ts = String.valueOf(System.currentTimeMillis() / 1000);
		String sign = SignHelper.sign(paramTreeMap, ts);
		paramTreeMap.put("ts", ts);
		paramTreeMap.put("sign", sign);
		String postData = CommonUtils.getStr(paramTreeMap);
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
		logger.info("dftt-{}:{}",user.getUsername(),content);
		return content;
	}

	public JSONObject getNewsList() {
		String url = "https://refreshnews.dftoutiao.com/toutiao_appnew02/newsgzip";
		TreeMap<String, String> paramTreeMap = new TreeMap<String, String>();
		paramTreeMap.put("type", "toutiao");
		paramTreeMap.put("startkey", startkey);
		paramTreeMap.put("newkey", newkey);
		paramTreeMap.put("pgnum", pgnum + "");
		paramTreeMap.put("idx", idx + "");
		paramTreeMap.put("key", user.getKeystr());// 6
		paramTreeMap.put("softtype", "TouTiao");
		paramTreeMap.put("softname", "DFTTAndroid");
		paramTreeMap.put("ime", user.getImei());// 9
		paramTreeMap.put("appqid", user.getQid());
		paramTreeMap.put("apptypeid", "DFTT");
		paramTreeMap.put("ver", version);
		paramTreeMap.put("os", user.getOsType());// 13
		paramTreeMap.put("ttaccid", user.getAccid());//
		paramTreeMap.put("appver", appversion);
		paramTreeMap.put("deviceid", user.getDevice());
		paramTreeMap.put("position", user.getPrince());
		paramTreeMap.put("iswifi", "wifi");
		paramTreeMap.put("channellabel", "null");
		paramTreeMap.put("citypos", user.getCity());// 2
		paramTreeMap.put("sublocal", user.getArea());
		paramTreeMap.put("hispos", user.getHispos());
		paramTreeMap.put("ispack_s", "1");
		paramTreeMap.put("appinfo", user.getInfo());
		paramTreeMap.put("sclog", "1");
		paramTreeMap.put("devicetp", "0");
		paramTreeMap.put("devicemode", "0");
		String postData = CommonUtils.getStr(paramTreeMap);
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
		JSONObject result = JSONObject.parseObject(content);
		if (result.getIntValue("stat") == 1) {
			startkey = result.getString("endkey");
			newkey = result.getString("newkey");
			pgnum = pgnum - 1;
			idx = idx - 8;
			return result;
		}
		return null;
	}

	public JSONObject readNews(String newsUrl) {
		String nodes = genNodes(newsUrl, "news", "toutiao");
		String url = "https://yuedu.dftoutiao.com/taskyuedu/read_news";
		TreeMap<String, String> paramTreeMap = new TreeMap<String, String>();
		paramTreeMap.put("accid", user.getAccid());
		paramTreeMap.put("cqid", "dftt");
		paramTreeMap.put("deviceid", user.getDevice());
		paramTreeMap.put("imei", user.getImei());
		paramTreeMap.put("machine", user.getMachine());
		paramTreeMap.put("nodes", nodes);
		paramTreeMap.put("oem", "xiaomi");
		paramTreeMap.put("os", user.getOsType());
		paramTreeMap.put("plantform", user.getPlantform());
		paramTreeMap.put("qid", user.getQid());
		paramTreeMap.put("timer_type", "news_timer");
		paramTreeMap.put("version", version);
		String ts = String.valueOf(System.currentTimeMillis() / 1000);
		String sign = SignHelper.sign(paramTreeMap, ts);
		paramTreeMap.put("ts", ts);
		paramTreeMap.put("sign", sign);
		String postData = CommonUtils.getStr(paramTreeMap);
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
		JSONObject result = JSONObject.parseObject(content);
		return result;
	}

	public JSONArray pushNewsList() {
		String url = "https://pushhistory.dftoutiao.com/upushh/sph";
		TreeMap<String, String> paramTreeMap = new TreeMap<String, String>();
		paramTreeMap.put("softtype", "TouTiao");
		paramTreeMap.put("softname", "DFTTAndroid");
		paramTreeMap.put("imei", user.getImei());
		paramTreeMap.put("appqid", user.getQid());
		paramTreeMap.put("apptypeid", "DFTT");
		paramTreeMap.put("ver", version);
		paramTreeMap.put("os", user.getOsType());
		paramTreeMap.put("ttaccid", user.getAccid());
		paramTreeMap.put("appver", appversion);
		paramTreeMap.put("deviceid", user.getDevice());
		paramTreeMap.put("position", user.getPrince());
		paramTreeMap.put("loginname", user.getUsername());
		String postData = CommonUtils.getStr(paramTreeMap);
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
		JSONObject result = JSONObject.parseObject(content);
		if (result.getIntValue("status") == 1) {
			return result.getJSONArray("data");
		}
		return new JSONArray();
	}

	/**
	 * 阅读推送消息
	 * 
	 * @param newsId
	 * @return
	 */
	public JSONObject readPushNews(String newsId) {
		String url = "https://kp.dftoutiao.com/taskfinishnew/read_push_news_s";
		TreeMap<String, String> paramTreeMap = new TreeMap<String, String>();
		paramTreeMap.put("newsid", newsId);
		paramTreeMap.put("accid", user.getAccid());
		paramTreeMap.put("imei", user.getImei());
		paramTreeMap.put("machine", user.getMachine());
		paramTreeMap.put("oem", "DFTT");
		paramTreeMap.put("plantform", user.getPlantform());
		paramTreeMap.put("qid", user.getQid());
		paramTreeMap.put("version", version);
		String ts = String.valueOf(System.currentTimeMillis() / 1000);
		String sign = SignHelper.sign(paramTreeMap, ts);
		paramTreeMap.put("ts", ts);
		paramTreeMap.put("sign", sign);
		String postData = CommonUtils.getStr(paramTreeMap);
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
		logger.info("dftt-{}:阅读推送消息结果:{}",user.getUsername(),content);
		JSONObject result = JSONObject.parseObject(content);
		return result;
	}

	public boolean custom() {
		String url = "https://tuoluolog.dftoutiao.com/apponline/custom.s";
		String param1 = lastparam;
		String param2 = SignHelper.getLastParam();
		String code = SignHelper.custom(user.getAccid(), "dftt" + DateUtils.formatDate(new Date(), "yyMMdd"),
				user.getDevice(), user.getImei(), user.getOsType(), user.getPrince(), user.getMac(), user.getSsid(),
				user.getBssid(), user.getLat(), user.getLng(), param1, param2);
		TreeMap<String, String> paramTreeMap = new TreeMap<String, String>();
		paramTreeMap.put("code", code);
		String postData = CommonUtils.getStr(paramTreeMap);
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
		JSONObject result = JSONObject.parseObject(content);
		if (result.getIntValue("status") == 0) {
			lastparam = param2;
			return true;
		}
		return false;
	}

	private String genNodes(String newsUrl, String type, String from) {
		JSONArray array = new JSONArray();
		JSONObject record01 = new JSONObject();
		record01.put("type", type);
		record01.put("urlto", newsUrl);
		record01.put("urlfrom", from);
		record01.put("node", "10");
		array.add(record01);

		JSONObject record02 = new JSONObject();
		record02.put("type", type);
		record02.put("urlto", newsUrl);
		record02.put("urlfrom", from);
		record02.put("node", "20");
		array.add(record02);

		JSONObject record03 = new JSONObject();
		record03.put("type", type);
		record03.put("urlto", newsUrl);
		record03.put("urlfrom", from);
		record03.put("node", "30");
		array.add(record03);

		JSONObject record04 = new JSONObject();
		record04.put("type", type);
		record04.put("urlto", newsUrl);
		record04.put("urlfrom", from);
		record04.put("node", "35");
		array.add(record04);
		return array.toJSONString();
	}
	
	public JSONObject videoList() {
		String url = "https://video.dftoutiao.com/app_video/getvideosgzip";
		TreeMap<String, String> paramTreeMap = new TreeMap<String, String>();
		paramTreeMap.put("categoryId", "799999");
		paramTreeMap.put("count", "20");
		paramTreeMap.put("startkey", startkey);
		paramTreeMap.put("newkey", newkey);
		paramTreeMap.put("pgnum", pgnum + "");
		paramTreeMap.put("position", user.getPrince());
		paramTreeMap.put("iswifi", "wifi");
		paramTreeMap.put("citypos", user.getCity());// 2
		paramTreeMap.put("sublocal", user.getArea());
		paramTreeMap.put("hispos", user.getHispos());
		paramTreeMap.put("ispack_s", "1");
		paramTreeMap.put("appinfo", user.getInfo());
		String param = "TouTiao	DFTTAndroid	"+user.getImei()+"	"+user.getQid()+"	DFTT	"+version+"	"+user.getOsType()+"	"+user.getAccid()+"	"+appversion+"	"+user.getDevice();
		paramTreeMap.put("param", param);
		paramTreeMap.put("sclog", "1");
		paramTreeMap.put("devicetp", "0");
		paramTreeMap.put("devicemode", "0");
		String postData = CommonUtils.getStr(paramTreeMap);
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
		//logger.info("{}:{}", user.getUsername(), content);
		JSONObject result = JSONObject.parseObject(content);
		startkey = result.getString("endkey");
		newkey = result.getString("newkey");
		pgnum = pgnum - 1;
		return result;
	}
	
	public JSONObject readVideo(String newsUrl) {
		if (!custom())
			return null;
		String nodes = genNodes(newsUrl, "video", "799999");
		String url = "https://yuedu.dftoutiao.com/taskyuedu/read_news";
		TreeMap<String, String> paramTreeMap = new TreeMap<String, String>();
		paramTreeMap.put("accid", user.getAccid());
		paramTreeMap.put("cqid", "dftt");
		paramTreeMap.put("deviceid", user.getDevice());
		paramTreeMap.put("imei", user.getImei());
		paramTreeMap.put("machine", user.getMachine());
		paramTreeMap.put("nodes", nodes);
		paramTreeMap.put("oem", "xiaomi");
		paramTreeMap.put("os", user.getOsType());
		paramTreeMap.put("plantform", user.getPlantform());
		paramTreeMap.put("qid", user.getQid());
		paramTreeMap.put("timer_type", "video_timer");
		paramTreeMap.put("version", version);
		String ts = String.valueOf(System.currentTimeMillis() / 1000);
		String sign = SignHelper.sign(paramTreeMap, ts);
		paramTreeMap.put("ts", ts);
		paramTreeMap.put("sign", sign);
		String postData = CommonUtils.getStr(paramTreeMap);
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("Accept-Encoding", "gzip");
		heads.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
		//logger.info(user.getUsername() + ":" + content);
		JSONObject result = JSONObject.parseObject(content);
		return result;
	}
}
