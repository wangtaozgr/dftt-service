package com.atao.dftt.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atao.dftt.model.Wltt;
import com.atao.dftt.util.MobileHttpUrlConnectUtils;
import com.atao.dftt.util.TaottUtils;
import com.atao.dftt.util.WlttUtils;
import com.atao.util.StringUtils;

public class WlttHttp {
	private static Logger logger = LoggerFactory.getLogger(WlttHttp.class);
	private static Map<String, WlttHttp> wlttMap = new HashMap<String, WlttHttp>();
	public Wltt wltt;
	public String userAgentEnd = "";
	public String taskId;// 新闻taskId
	public String timestamp;// 新闻timestamp
	public String vTaskId;// 视频taskId
	public String vTimestamp;//// 视频timestamp
	public JSONArray newsEventList = new JSONArray();

	public WlttHttp(Wltt wltt) {
		this.wltt = wltt;
		userAgentEnd = " ssy={WeiLiTT;V" + WlttUtils.verName + ";" + wltt.getChannel() + ";" + wltt.getCityKey() + ";}";
	}

	public static synchronized WlttHttp getInstance(Wltt wltt) {
		WlttHttp wlttHttp = (WlttHttp) wlttMap.get(wltt.getUsername());
		if (wlttHttp == null) {
			wlttHttp = new WlttHttp(wltt);
			wlttMap.put(wltt.getUsername(), wlttHttp);
		}
		return wlttHttp;
	}

	public JSONObject login() {
		try {
			String url = "https://cloud-server.weilitoutiao.net/api/login";
			Map<String, String> map = WlttUtils.init(wltt);
			map.put("lon", wltt.getLon());
			map.put("x", wltt.getX());
			map.put("auth_token", WlttUtils.getAuthToken(wltt.getDevice(), ""));
			map.put("uid", "");
			map.put("lat", wltt.getLon());
			String postData = WlttUtils.getStr(map);
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", wltt.getUserAgent() + userAgentEnd);
			heads.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			logger.info("wltt-{}:用户登陆结果={}", wltt.getUsername(), content);
			if (object.getIntValue("status") == 1000) {
				return object.getJSONObject("data");
			}
		} catch (Exception e) {
			logger.error("wltt-{}:用户登陆时发生异常!{}", wltt.getUsername(), e.getMessage());
		}
		return null;
	}

	public JSONObject queryMyCoin() {
		try {
			String url = "https://wltask.weilitoutiao.net/wltask/api/coin/auth/gold/flows?";
			Map<String, String> map = WlttUtils.init(wltt);
			map.put("page_size", "10");
			map.put("page", "1");
			map.put("lon", wltt.getLon());
			map.put("lat", wltt.getLat());
			String sign = WlttUtils.sign(map);
			map.put("app_sign", sign);
			String params = WlttUtils.getStr(map);
			url = url + params;
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", wltt.getUserAgent() + userAgentEnd);
			String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getIntValue("status") == 1000) {
				JSONObject result = object.getJSONObject("data").getJSONObject("brief");
				logger.info("wltt-{}:查询今日获取的金币信息成功.", wltt.getUsername());
				return result;
			}
			logger.error("wltt-{}:查询今日获取的金币信息失败,msg={}", wltt.getUsername(), content);
		} catch (Exception e) {
			logger.error("wltt-{}:查询今日获取的金币信息异常,msg={}", wltt.getUsername(), e.getMessage());
		}
		return null;
	}

	public boolean qiandao() {
		if (StringUtils.isBlank(wltt.getOpenId()))
			return false;
		try {
			String t = System.currentTimeMillis() + "";
			String app_sign = WlttUtils.qiandaoEncode(t);
			String url = "https://wltask.weilitoutiao.net/wltask/api/coin/h5/checkin?open_uid=" + wltt.getOpenId()
					+ "&app_sign=" + app_sign + "&timestamp=" + t + "&device_id=" + wltt.getDeviceId() + "&app_key="
					+ wltt.getAppKey() + "&ver_name=v" + WlttUtils.verName;
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("User-Agent",
					wltt.getUserAgent() + " ssy={WeiLiTT;v" + WlttUtils.verName + ";" + wltt.getChannel() + ";"
							+ wltt.getCityKey() + ";WIFI;libertyad;ebrowser;suid=" + wltt.getOpenId() + ";device_id="
							+ wltt.getDeviceId() + ";lon=" + wltt.getLon() + ";lat=" + wltt.getLat()
							+ ";ad_code=340103;}");
			heads.put("Content-Type", "application/json, text/plain, */*");
			heads.put("X-Requested-Wit", "cn.weli.story");
			heads.put("Origin", "https://f-wltask.weilitoutiao.net");
			heads.put("Referer", "https://f-wltask.weilitoutiao.net/daySign.html");
			String content = MobileHttpUrlConnectUtils.httpPost(url, null, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getIntValue("status") == 1000) {
				logger.info("wltt-{}:签到成功!", wltt.getUsername());
				Thread.sleep(2000);
				shareQiandao();
				return true;
			} else if (object.getIntValue("status") == 8102) {
				logger.info("wltt-{}:已签到!", wltt.getUsername());
				return true;
			}
		} catch (Exception e) {
			logger.error("wltt-{}:签到发生异常!{}", wltt.getUsername(), e.getMessage());
		}
		return false;
	}

	public boolean shareQiandao() {
		try {
			String url = "https://wltask.weilitoutiao.net/wltask/api/coin/auth/tasks/share_sign_on/doing";
			Map<String, String> map = WlttUtils.init(wltt);
			map.put("lon", wltt.getLon());
			map.put("lat", wltt.getLon());
			String app_sign = WlttUtils.sign(map);
			map.put("app_sign", app_sign);
			String postData = WlttUtils.getStr(map);
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", wltt.getUserAgent() + userAgentEnd);
			heads.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			logger.info("wltt-{}:分享签到结果|result={}", wltt.getUsername(), content);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getIntValue("status") == 1000) {
				logger.info("wltt-{}:分享签到成功", wltt.getUsername());
				return true;
			}
			logger.error("wltt-{}:分享签到失败 msg={}", wltt.getUsername(), content);
		} catch (Exception e) {
			logger.error("wltt-{}:分享签到发生异常! msg={}", wltt.getUsername(), e.getMessage());
		}
		return false;
	}

	public JSONObject daka() {
		try {
			String url = "https://wltask.weilitoutiao.net/wltask/api/coin/auth/box/open";
			Map<String, String> map = WlttUtils.init(wltt);
			map.put("lon", wltt.getLon());
			map.put("lat", wltt.getLon());
			String app_sign = WlttUtils.sign(map);
			map.put("app_sign", app_sign);
			String postData = WlttUtils.getStr(map);
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", wltt.getUserAgent() + userAgentEnd);
			heads.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			logger.info("wltt-{}:打卡结果|result={}", wltt.getUsername(), content);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getIntValue("status") == 1000) {
				logger.info("wltt-{}:打卡成功", wltt.getUsername());
				return object;
			}
			logger.error("wltt-{}:打卡失败 msg={}", wltt.getUsername(), content);
		} catch (Exception e) {
			logger.error("wltt-{}:打卡发生异常! msg={}", wltt.getUsername(), e.getMessage());
		}
		return null;
	}

	public JSONArray newsList() {
		try {
			String url = "https://pc.weilitoutiao.net/lizhi/api/zhwnl/v4/headline?&";
			Map<String, String> map = WlttUtils.init(wltt);
			map.put("local_svc_version", WlttUtils.verCode);
			String app_sign = WlttUtils.sign(map);
			map.put("app_sign", app_sign);
			String params = WlttUtils.getStr(map);
			url = url + params;
			String postData = "{\"c_click\":0,\"c_pv\":5,\"is_all_tab\":0,\"is_cus_tag\":1,\"page_size\":12,\"page\":1,\"tab_id\":1,\"text\":\"\",\"city_key\":\""
					+ wltt.getCityKey() + "\",\"app\":{\"bundle\":\"cn.weli.story\",\"version\":\"" + WlttUtils.verName
					+ "\"},\"geo\":{\"lat\":\"" + wltt.getLat() + "\",\"lon\":\"" + wltt.getLon()
					+ "\"},\"device\":{\"android_advertising_id\":\"\",\"android_id\":\"" + wltt.getAndroidid()
					+ "\",\"carrier\":\"CHINA_TELECOM\",\"density\":\"3.0\",\"orientation\":\"VERTICAL\",\"language\":\"zh-CN\",\"os\":\""
					+ wltt.getOs()
					+ "\",\"open_udid\":\"\",\"duid\":\"\",\"idfa\":\"\",\"ssid\":\"\",\"root\":0,\"user_agent\":\""
					+ wltt.getUserAgent() + "\",\"ip\":\"\",\"imei\":\"" + wltt.getImei() + "\",\"imsi\":\""
					+ wltt.getImsi() + "\",\"mac\":\"" + wltt.getMac() + "\",\"model\":\"" + wltt.getModel()
					+ "\",\"network\":\"WIFI\",\"os_version\":\"" + wltt.getOsVersion() + "\",\"vendor\":\""
					+ wltt.getVendor() + "\",\"resolution\":{\"height\":1920,\"width\":1080}}}";
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", wltt.getUserAgent() + userAgentEnd);
			heads.put("Content-Type", "application/json; charset=utf-8");
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getIntValue("status") == 1000) {
				JSONArray list = object.getJSONObject("data").getJSONArray("list");
				return list;
			}
		} catch (Exception e) {
			logger.error("wltt-{}:查询新闻列表时发生异常!{}", wltt.getUsername(), e.getMessage());
		}
		return new JSONArray();
	}

	public String upTask(String itemId) {
		try {
			String url = "https://client-lz.weilitoutiao.net/lizhi/api/upTask";
			Map<String, String> map = WlttUtils.init(wltt);
			map.put("itemId", itemId);
			map.put("local_svc_version", WlttUtils.verCode);
			map.put("locale", "zh");
			map.put("task", "ZHWNL_READ");
			String app_sign = WlttUtils.sign(map);
			map.put("app_sign", app_sign);
			String postData = WlttUtils.getStr(map);
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", wltt.getUserAgent() + userAgentEnd);
			heads.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			return content;
		} catch (Exception e) {
			logger.error("wltt-{}:upTask时发生异常!{}", wltt.getUsername(), e.getMessage());
		}
		return null;
	}

	public JSONObject firstGetTask(String itemId) {
		try {
			String url = "https://wltask.weilitoutiao.net/wltask/api/coin/auth/tasks/read_consult/doing/";
			Map<String, String> map = WlttUtils.init(wltt);
			map.put("info_id", itemId);
			map.put("key", "read_consult");
			String app_sign = WlttUtils.sign(map);
			map.put("app_sign", app_sign);
			String postData = WlttUtils.getStr(map);
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", wltt.getUserAgent() + userAgentEnd);
			heads.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			logger.info("wltt-{}:第一次获取新闻任务id|result={}", wltt.getUsername(), content);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getIntValue("status") == 1000) {
				JSONObject data = object.getJSONObject("data");
				taskId = data.getString("task_id");
				timestamp = data.getString("timestamp");
				return object;
			} else if (object.getIntValue("status") == 8403) {
				return object;
			} else if (object.getIntValue("status") == 8309) {
				return object;
			}
			return object;
		} catch (Exception e) {
			logger.error("wltt-{}:第一次获取新闻任务id时发生异常!{}", wltt.getUsername(), e.getMessage());
		}
		return null;
	}

	/**
	 * 阅读新闻
	 * 
	 * @param itemId
	 * @param taskId
	 * @param timestamp
	 * @return
	 */
	public JSONObject readNews(String itemId, String taskId, String timestamp) {
		try {
			String url = "https://wltask.weilitoutiao.net/wltask/api/coin/auth/tasks/read_consult/doing/";
			Map<String, String> map = WlttUtils.init(wltt);
			map.put("timestamp", timestamp);
			map.put("task_id", taskId);
			map.put("info_id", itemId);
			map.put("key", "read_consult");
			String app_sign = WlttUtils.sign(map);
			map.put("app_sign", app_sign);
			String postData = WlttUtils.getStr(map);
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", wltt.getUserAgent() + userAgentEnd);
			heads.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			logger.info("wltt-{}:阅读新闻结果|result={}", wltt.getUsername(), content);
			JSONObject object = JSONObject.parseObject(content);
			return object;
		} catch (Exception e) {
			logger.error("wltt-{}:阅读新闻时发生异常!{}", wltt.getUsername(), e.getMessage());
		}
		return null;
	}

	public JSONArray getRelatedRead(String item_id, String post_id) {
		try {
			String url = "https://pc.weilitoutiao.net/lizhi/api/zhwnl/v3/getRelatedRead?";
			Map<String, String> map = WlttUtils.init(wltt);
			map.put("local_svc_version", WlttUtils.verCode);
			map.put("vercode", WlttUtils.verCode);
			map.put("item_id", item_id);
			map.put("post_id", post_id);
			map.put("locale", "zh_CN");
			map.put("platform", wltt.getUp());
			String sign = WlttUtils.sign(map);
			map.put("app_sign", sign);
			String params = WlttUtils.getStr(map);
			url = url + params;
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", wltt.getUserAgent() + userAgentEnd);
			String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getIntValue("status") == 1000) {
				JSONArray list = object.getJSONArray("data").getJSONObject(0).getJSONArray("list");
				return list;
			}
		} catch (Exception e) {
			logger.error("wltt-{}:getRelatedRead时发生异常!{}", wltt.getUsername(), e.getMessage());
		}
		return new JSONArray();
	}

	/**
	 * 领奖 一天20次
	 * 
	 * @param itemId
	 * @param taskId
	 * @param timestamp
	 * @return
	 */
	public JSONObject readGift(String giftId) {
		try {
			String url = "https://wltask.weilitoutiao.net/wltask/api/coin/auth/gift/receive";
			Map<String, String> map = WlttUtils.init(wltt);
			map.put("gift_id", giftId);
			String app_sign = WlttUtils.sign(map);
			map.put("app_sign", app_sign);
			String postData = WlttUtils.getStr(map);
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", wltt.getUserAgent() + userAgentEnd);
			heads.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			// logger.info("wltt-{}:领取礼物结果|result={}", wltt.getUsername(), content);
			JSONObject object = JSONObject.parseObject(content);
			return object;
		} catch (Exception e) {
			logger.error("wltt-{}:领取礼物时发生异常!{}", wltt.getUsername(), e.getMessage());
		}
		return null;
	}

	public JSONArray videoList() {

		try {
			String url = "http://opensdk.duan.360kan.com/api/sdk/video/list?";
			TreeMap<String, String> map = new TreeMap<String, String>();
			map.put("m", "fb9d5409fa9ad058db6d54e31ca9f544");
			map.put("m2", "5db6dd0ab63055cc755502a1c18d867a");
			map.put("ch", "10001");
			map.put("detection", "1");
			map.put("svc", "3");
			map.put("appid", "opensdk_weilikankan");
			map.put("channel_id", "24");
			map.put("cdn_url", "1");
			map.put("columns", "0");
			map.put("direction", "up");
			map.put("vc", WlttUtils.verCode);
			map.put("sdkvc", "10106");
			map.put("sys", wltt.getModel());
			map.put("net", "4");
			map.put("os", "25");
			map.put("os_type", wltt.getOs());
			map.put("mf", wltt.getVendor());
			String ts = String.valueOf(System.currentTimeMillis() / 1000);
			map.put("ts", ts);
			String s = WlttUtils.getStr(map);
			String sign = WlttUtils.videoListSign(s);
			map.put("sign", sign);
			String params = WlttUtils.getStr(map);
			url = url + params;
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", wltt.getUserAgent() + userAgentEnd);
			heads.put("X-REQ-TRY", "1/2");
			String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getIntValue("errno") == 0) {
				JSONArray list = object.getJSONObject("data").getJSONArray("videoList");
				return list;
			} else {
				logger.error("wltt-{}: 视频列表错误信息|result={}", wltt.getUsername(), object.getString("errmsg"));
				return new JSONArray();
			}
		} catch (Exception e) {
			logger.error("wltt-{}:查询视频列表时发生异常!{}", wltt.getUsername(), e.getMessage());
		}
		return new JSONArray();
	}

	public String queryVideoId(String itemId) {
		try {
			String url = "https://client-lz.weilitoutiao.net/lizhi/api/post/detail/dynamic?";
			Map<String, String> map = WlttUtils.init(wltt);
			String ts = String.valueOf(System.currentTimeMillis());
			map.put("app_ts", ts);
			map.put("video_source", "KUAI_360");
			map.put("post_id", "0");
			map.put("channel", wltt.getChannel());
			map.put("video_id", itemId);
			String app_sign = WlttUtils.sign(map);
			map.put("app_sign", app_sign);
			String params = WlttUtils.getStr(map);
			url = url + params;
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("User-Agent", wltt.getUserAgent() + userAgentEnd);
			String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getIntValue("status") == 1000) {
				return object.getJSONObject("data").getString("item_id");
			}
		} catch (Exception e) {
			logger.error("wltt-{}:查询视频列表时发生异常!{}", wltt.getUsername(), e.getMessage());
		}
		return null;
	}

	public boolean firstGetVideoTask(String itemId) {
		try {
			String url = "https://wltask.weilitoutiao.net/wltask/api/coin/auth/tasks/watch_video/doing/";
			Map<String, String> map = WlttUtils.init(wltt);
			map.put("video_id", itemId);
			map.put("key", "watch_video");
			String app_sign = WlttUtils.sign(map);
			map.put("app_sign", app_sign);
			String postData = WlttUtils.getStr(map);
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", wltt.getUserAgent() + userAgentEnd);
			heads.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			logger.info("wltt-{}:第一次获取视频任务id|result={}", wltt.getUsername(), content);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getIntValue("status") == 1000) {
				JSONObject data = object.getJSONObject("data");
				vTaskId = data.getString("task_id");
				vTimestamp = data.getString("timestamp");
			} else if (object.getIntValue("status") == 8403) {
				return false;
			} else if (object.getIntValue("status") == 8309) {
				return false;
			}
		} catch (Exception e) {
			logger.error("wltt-{}:第一次获取视频任务id时发生异常!{}", wltt.getUsername(), e.getMessage());
		}
		return false;
	}

	public JSONObject readVideos(String itemId, String taskId, String timestamp) {
		try {
			String url = "https://wltask.weilitoutiao.net/wltask/api/coin/auth/tasks/watch_video/doing/";
			Map<String, String> map = WlttUtils.init(wltt);
			map.put("timestamp", timestamp);
			map.put("task_id", taskId);
			map.put("video_id", itemId);
			map.put("key", "watch_video");
			String app_sign = WlttUtils.sign(map);
			map.put("app_sign", app_sign);
			String postData = WlttUtils.getStr(map);
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", wltt.getUserAgent() + userAgentEnd);
			heads.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			logger.info("wltt-{}:阅读视频结果|result={}", wltt.getUsername(), content);
			JSONObject object = JSONObject.parseObject(content);
			return object;
		} catch (Exception e) {
			logger.error("wltt-{}:阅读视频时发生异常!{}", wltt.getUsername(), e.getMessage());
		}
		return null;
	}

	public void search() {
		try {
			String url = "https://wltask.weilitoutiao.net/wltask/api/coin/auth/search/confirm?";
			Map<String, String> map = WlttUtils.init(wltt);
			String ts = String.valueOf(System.currentTimeMillis());
			map.put("app_ts", ts);
			map.put("channel", wltt.getChannel());
			String app_sign = WlttUtils.sign(map);
			map.put("app_sign", app_sign);
			String params = WlttUtils.getStr(map);
			url = url + params;
			String postData = "{\"keyword\":\"化妆刷\",\"location\":1}";
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", wltt.getUserAgent() + userAgentEnd);
			heads.put("Content-Type", "application/json; charset=utf-8");
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			logger.info("wltt-{}:搜索结果|result={}", wltt.getUsername(), content);
			JSONObject object = JSONObject.parseObject(content);
		} catch (Exception e) {
			logger.error("wltt-{}:搜索时发生异常!{}", wltt.getUsername(), e.getMessage());
		}

	}

	public void ad() {
		try {
			String url = "https://ads.weilitoutiao.net/kuaima_ads/api/ad/get";
			String postData = "{\"pid\":\"" + wltt.getPid() + "\",\"debug\":0,\"ts\":" + System.currentTimeMillis()
					+ ",\"version\":\"1.0.0\",\"city_key\":\"" + wltt.getCityKey()
					+ "\",\"app\":{\"bundle\":\"cn.weli.story\",\"app_key\":\"" + wltt.getAppKey() + "\",\"channel\":\""
					+ wltt.getChannel() + "\",\"app_version\":\"" + WlttUtils.verName + "\",\"app_version_code\":"
					+ WlttUtils.verCode + "},\"device\":{\"os\":\"" + wltt.getOs() + "\",\"osv\":\"" + wltt.getOsv()
					+ "\",\"carrier\":3,\"network\":2,\"resolution\":\"1080*1920\",\"density\":\"3.0\",\"open_udid\":\"\",\"aid\":\""
					+ wltt.getAndroidid() + "\",\"imei\":\"" + wltt.getImei() + "\",\"idfa\":\"\",\"mac\":\""
					+ wltt.getMac() + "\",\"aaid\":\"\",\"duid\":\"\",\"orientation\":0,\"vendor\":\""
					+ wltt.getVendor() + "\",\"model\":\"" + wltt.getModel()
					+ "\",\"lan\":\"zh\",\"ssid\":\"\",\"root\":0,\"zone\":\"+008\",\"nation\":\"CN\"},\"geo\":{\"lat\":\""
					+ wltt.getLat() + "\",\"lon\":\"" + wltt.getLon() + "\"}}";
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept", "application/json, text/plain, */*");
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", wltt.getUserAgent() + userAgentEnd);
			heads.put("Content-Type", "application/json");
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			logger.info("wltt-{}:发送广告|result={}", wltt.getUsername(), content);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getIntValue("status") == 1000) {
				JSONArray list = object.getJSONArray("data").getJSONObject(0).getJSONArray("view_track_urls");
				for (int i = 0; i < list.size(); i++) {
					String adUrl = list.getString(i);
					adView(adUrl);
				}
			}
		} catch (Exception e) {
			logger.error("wltt-{}:发送广告时发生异常!{}", wltt.getUsername(), e.getMessage());
		}

	}

	private void adView(String adUrl) {
		try {
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", wltt.getUserAgent() + userAgentEnd);
			int statusCode = MobileHttpUrlConnectUtils.httpGetStatusCode(adUrl, heads, null);
			if (statusCode > 400)
				adView(adUrl);
		} catch (Exception e) {
			logger.error("wltt-{}:发送广告adView时发生异常!{}", wltt.getUsername(), e.getMessage());
		}

	}

	public JSONObject retriveEventFromList(JSONArray newsList, int i) {
		JSONObject news = newsList.getJSONObject(i);
		return convertNesToEvent(news, "-3.1." + (i + 1));
	}

	public JSONObject convertNesToEvent(JSONObject news, String pos) {
		JSONObject cm = news.getJSONObject("content_model");
		cm.put("from", cm.getInteger("from"));
		int md = cm == null ? 0 : cm.getIntValue("md");
		JSONObject event = createEvent("view", news.getIntValue("item_id") + "", cm == null ? "" : cm.toJSONString(),
				pos, md, "{\"category_id\":\"1\"}", 11);
		return event;
	}

	public JSONObject createEvent(String e, String cId, String cM, String pos, int md, int sn) {
		JSONObject event = new JSONObject(true);
		event.put("e", e);
		event.put("id", UUID.randomUUID().toString());
		event.put("t", System.currentTimeMillis());
		event.put("c_id", cId);
		event.put("c_m", cM);
		event.put("pos", pos);
		event.put("md", md);
		event.put("sn", sn);
		event.put("x3d", Double.valueOf("0." + getNumberStr(16)));
		event.put("y3d", Double.valueOf("-0." + getNumberStr(16)));
		event.put("z3d", Double.valueOf("9." + getNumberStr(14)));
		return event;
	}

	public JSONObject createEvent(String e, String cId, String cM, String pos, int md, String args, int sn) {
		JSONObject event = new JSONObject(true);
		event.put("e", e);
		event.put("id", UUID.randomUUID().toString());
		event.put("t", System.currentTimeMillis());
		event.put("c_id", cId);
		event.put("c_m", cM);
		event.put("pos", pos);
		event.put("md", md);
		event.put("args", args);
		event.put("sn", sn);
		event.put("x3d", "0." + getNumberStr(16));
		event.put("y3d", "-0." + getNumberStr(16));
		event.put("z3d", "9." + getNumberStr(14));
		return event;
	}

	public JSONArray getNewsDetailEventList(JSONArray newsList, int i) {
		JSONArray eventList = new JSONArray();
		JSONObject news = newsList.getJSONObject(i);
		JSONObject cm = news.getJSONObject("content_model");
		cm.put("from", cm.getInteger("from"));
		int md = cm == null ? 0 : cm.getInteger("md");
		String pos = "-3.1." + (i + 1);
		JSONObject click = createEvent("click", news.getIntValue("item_id") + "", cm == null ? "" : cm.toJSONString(),
				pos, md, "{\"category_id\":\"1\"}", 11);
		JSONObject channel_exit = createEvent("channel_exit", "1", "", "-3.1", md,
				"{\"use_time_ms\":" + (10000 + new Random().nextInt(2000)) + "}", 11);
		JSONObject page_view = createEvent("page_view", "-2", "", pos, md, "{\"category_id\":\"1\"}", 11);
		eventList.add(click);
		eventList.add(channel_exit);
		eventList.add(page_view);
		return eventList;
	}

	public JSONArray getBackNewsListEventList(JSONArray newsList, int i) {
		JSONArray eventList = new JSONArray();
		JSONObject news = newsList.getJSONObject(i);
		JSONObject cm = news.getJSONObject("content_model");
		cm.put("from", cm.getInteger("from"));
		int md = cm == null ? 0 : cm.getInteger("md");
		String pos = "-3.1." + (i + 1);

		JSONObject exit = createEvent("exit", news.getIntValue("item_id") + "", cm == null ? "" : cm.toJSONString(),
				pos, md, "{\"category_id\":\"1\"}", 11);
		JSONObject page_view = createEvent("page_view", "1", "", "", md, 11);
		JSONObject view = createEvent("view", "-1400", "", "", md, 11);
		JSONObject re = createEvent("return", news.getIntValue("item_id") + "", cm == null ? "" : cm.toJSONString(),
				pos, md, "{\"category_id\":\"1\"}", 11);
		JSONObject returnView = createEvent("view", news.getIntValue("item_id") + "",
				cm == null ? "" : cm.toJSONString(), pos, md, "{\"category_id\":\"1\"}", 11);
		eventList.add(exit);
		eventList.add(page_view);
		eventList.add(view);
		eventList.add(re);
		eventList.add(returnView);
		return eventList;
	}

	private String getNumberStr(int len) {
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < len; i++) {
			s.append(new Random().nextInt(10));
		}
		return s.toString();
	}

	public JSONArray getEventArray(JSONArray event_list) {
		JSONArray eventArray = new JSONArray();
		JSONObject eventJson = new JSONObject(true);
		JSONObject identity = new JSONObject(true);
		identity.put("appkey", wltt.getAppKey());
		identity.put("publish", "wltt");
		identity.put("imei", wltt.getImei());
		identity.put("imsi", wltt.getImsi());
		identity.put("mac", wltt.getMac());
		identity.put("uid", Long.valueOf(wltt.getUid()));
		JSONObject client_info = new JSONObject(true);
		client_info.put("lat", wltt.getLat());
		client_info.put("lon", wltt.getLon());
		client_info.put("city", wltt.getCityKey());
		client_info.put("os", wltt.getOs());
		client_info.put("os_ver", wltt.getOsv());
		client_info.put("pkg", "cn.weli.story");
		client_info.put("ver_code", WlttUtils.verCode);
		client_info.put("sdk_ver", "v91");
		client_info.put("app_ver", WlttUtils.verName);
		client_info.put("r", "1080x1920");
		client_info.put("network", "Wi-Fi");
		client_info.put("country", "CN");
		client_info.put("spec", wltt.getModel());
		client_info.put("tz", "8");
		client_info.put("sp", "");
		client_info.put("lang", "zh");
		client_info.put("channel", wltt.getChannel());
		eventJson.put("identity", identity);
		eventJson.put("event_list", event_list);
		eventJson.put("client_info", client_info);
		eventArray.add(eventJson);
		return eventArray;
	}

	public void collectEventLog(JSONArray event_list) {
		try {
			String url = "https://log-dmp.weilitoutiao.net/collect/event/log";
			JSONArray eventArray = getEventArray(event_list);
			String x = WlttUtils.collectLog(eventArray.toJSONString());
			String postData = "x=" + x + "&cps=gzip";
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "Apache-HttpClient/Suishen Peacock (91988061 v91)");
			heads.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			int code = MobileHttpUrlConnectUtils.httpPostStatusCode(url, postData, heads, null);
			// logger.info("wltt-{}:发送日志结果|code={}", wltt.getUsername(), code);
		} catch (Exception e) {
			logger.error("wltt-{}:发送日志时发生异常!{}", wltt.getUsername(), e.getMessage());
		}

	}
}
