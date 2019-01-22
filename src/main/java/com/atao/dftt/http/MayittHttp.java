package com.atao.dftt.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atao.base.util.StringUtils;
import com.atao.dftt.model.MayittUser;
import com.atao.dftt.util.CommonUtils;
import com.atao.dftt.util.MayittUtils;
import com.atao.dftt.util.MobileHttpUrlConnectUtils;
import com.atao.dftt.util.TaottUtils;
import com.atao.dftt.util.mayitt.PubKeySignature;

public class MayittHttp {
	private static Logger logger = LoggerFactory.getLogger(MayittHttp.class);
	private static Map<String, MayittHttp> userMap = new HashMap<String, MayittHttp>();
	public MayittUser user;
	public Long behot_time = System.currentTimeMillis();
	public String oid = "";
	public Long behot_timev = System.currentTimeMillis();
	public String oidv = "";
	public JSONObject adTaskData;
	public String startTaskUrl;

	public JSONArray eventList = new JSONArray();
	public static Random random = new Random();

	public MayittHttp(MayittUser user) {
		this.user = user;
	}

	public static synchronized MayittHttp getInstance(MayittUser user) {
		MayittHttp http = (MayittHttp) userMap.get(user.getUsername());
		if (http == null) {
			http = new MayittHttp(user);
			userMap.put(user.getUsername(), http);
		}
		return http;
	}

	public MayittHttp refreshUser(MayittUser user) {
		MayittHttp http = new MayittHttp(user);
		userMap.put(user.getUsername(), http);
		return http;
	}

	public JSONObject login() {
		try {
			String url = "https://ktt.woyaoq.com/v3/user/mobile/login.json?";
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.9.1");
			String boundary = UUID.randomUUID().toString();
			heads.put("Content-Type", "multipart/form-data; boundary=" + boundary);
			Map<String, String> map = MayittUtils.init(user);
			map.put("mobile", user.getUsername());
			map.put("password", user.getPassword());
			String sign = MayittUtils.sign(map);
			map.put("sign", sign);
			String postData = MayittUtils.getMultipartStr(map, boundary);
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getBooleanValue("success")) {
				logger.info("mayitt-{}:用户登陆成功,result={}", user.getUsername(), object.toString());
				return object;
			}
			logger.error("mayitt-{}:用户登陆时失败,result={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("mayitt-{}:用户登陆时发生异常!{}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public JSONObject queryMyCoin() {
		JSONObject object = userinfo();
		return object;
	}

	public JSONObject userinfo() {
		try {
			String url = "https://ktt.woyaoq.com/v3/user/userinfo.json?";
			Map<String, String> map = MayittUtils.init(user);
			map.put("uid", user.getUserId());
			String sign = MayittUtils.sign(map);
			map.put("sign", sign);
			String postData = MayittUtils.getStr(map);
			url = url + postData;
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.9.1");
			String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getBooleanValue("success")) {
				JSONObject result = object.getJSONObject("items");
				return result;
			}
			logger.error("mayitt-{}:获取用户信息失败,msg={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("mayitt-{}:获取用户信息异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public boolean qiandao() {
		JSONObject userinfo = userinfo();
		try {
			String url = "https://ktt.woyaoq.com/TaskCenter/daily_sign_action?";
			url += userinfo.getString("token_sign") + "&app_version=" + MayittUtils.app_version
					+ "&device_platform=android&channel=" + user.getChannel() + "&uuid=" + user.getImei();
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Pragma", "no-cache");
			heads.put("Cache-Control", "no-cache");
			heads.put("Accept", "*/*");
			heads.put("X-Requested-With", "XMLHttpRequest");
			heads.put("User-Agent", user.getUserAgent());
			heads.put("Accept-Encoding", "gzip, deflate");
			heads.put("Accept-Language", "zh-CN,en-US;q=0.9");
			heads.put("Referer", userinfo.getString("red_packet_url"));
			String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getIntValue("status") == 0) {
				logger.info("mayitt-{}:签到成功", user.getUsername());
				return true;
			}
			logger.error("mayitt-{}:签到失败, result={}", user.getUsername(), object.toJSONString());
		} catch (Exception e) {
			logger.error("mayitt-{}:打卡异常,msg={}", user.getUsername(), e.getMessage());
		}
		return false;
	}

	public boolean daka() {
		try {
			String url = "https://ktt.woyaoq.com/v5/article/treasure_chest.json?";
			Map<String, String> heads = MayittUtils.initHeader(user);
			String boundary = UUID.randomUUID().toString();
			heads.put("Content-Type", "multipart/form-data; boundary=" + boundary);
			Map<String, String> map = new HashMap<String, String>();
			map.put("channel", user.getChannel());
			map.put("device_type", "2");
			map.put("device_id", user.getDeviceId());
			map.put("sm_device_id", user.getSmDeviceId());
			map.put("access", "WIFI");
			map.put("version_code", MayittUtils.version_code);
			String request_time = System.currentTimeMillis() / 1000 + "";
			map.put("request_time", request_time);
			map.put("uid", user.getUserId());
			String sign = MayittUtils.sign(map);
			map.put("sign", sign);
			String data = "channel=" + user.getChannel() + "&device_type=2&device_id=" + user.getDeviceId()
					+ "&sm_device_id=" + user.getSmDeviceId() + "&access=WIFI&version_code=" + MayittUtils.version_code
					+ "&request_time=" + request_time + "&uid=" + user.getUserId() + "&sign=" + sign;
			String p = PubKeySignature.a(data);
			map = new HashMap<String, String>();
			map.put("p", p);
			String postData = MayittUtils.getMultipartStr(map, boundary);
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getBooleanValue("success")) {
				logger.info("mayitt-{}:打卡成功", user.getUsername());
				return true;
			}
			logger.error("mayitt-{}:打卡失败, result={}", user.getUsername(), object.toJSONString());
		} catch (Exception e) {
			logger.error("mayitt-{}:打卡异常,msg={}", user.getUsername(), e.getMessage());
		}
		return false;
	}

	public JSONArray newsList() {
		try {
			String url = "https://ktt.woyaoq.com/v4/article/lists.json?";
			Map<String, String> map = MayittUtils.init(user);
			map.put("uid", user.getUserId());
			map.put("catid", "0");
			map.put("op", "0");
			map.put("behot_time", behot_time + "");
			map.put("oid", oid);
			map.put("video_catid", "1453");
			String sign = MayittUtils.sign(map);
			map.put("sign", sign);
			String postData = MayittUtils.getStr(map);
			url = url + postData;
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.9.1");
			String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
			// logger.info(content);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getBooleanValue("success")) {
				JSONArray list = object.getJSONArray("items");
				if (list.size() > 0) {
					behot_time = list.getJSONObject(0).getLong("behot_time");
					oid = list.getJSONObject(0).getString("id");
				}
				return list;
			}
			logger.error("mayitt-{}:获取新闻列表失败,msg={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("mayitt-{}:获取新闻列表异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;

	}

	public JSONArray videoList() {
		try {
			String url = "https://ktt.woyaoq.com/v4/article/lists.json?";
			Map<String, String> map = MayittUtils.init(user);
			map.put("uid", user.getUserId());
			map.put("catid", "1453");
			map.put("op", "0");
			map.put("behot_time", behot_timev + "");
			map.put("oid", oidv);
			map.put("video_catid", "0");
			String sign = MayittUtils.sign(map);
			map.put("sign", sign);
			String postData = MayittUtils.getStr(map);
			url = url + postData;
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.9.1");
			String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getBooleanValue("success")) {
				JSONArray list = object.getJSONArray("items");
				if (list.size() > 0) {
					behot_timev = list.getJSONObject(0).getLong("behot_time");
					oidv = list.getJSONObject(0).getString("id");
				}
				return list;
			}
			logger.error("mayitt-{}:获取视频列表失败,msg={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("mayitt-{}:获取视频列表异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;

	}

	public JSONObject readNews(String newsId) {
		try {
			String url = "https://ktt.woyaoq.com/v5/article/complete_article.json?";
			Map<String, String> heads = MayittUtils.initHeader(user);
			String boundary = UUID.randomUUID().toString();
			heads.put("Content-Type", "multipart/form-data; boundary=" + boundary);

			Map<String, String> map = new HashMap<String, String>();
			map.put("article_id", newsId);
			map.put("read_type", "0");
			map.put("channel", user.getChannel());
			map.put("device_type", "2");
			map.put("device_id", user.getDeviceId());
			map.put("sm_device_id", user.getSmDeviceId());
			map.put("access", "WIFI");
			map.put("version_code", MayittUtils.version_code);
			String request_time = System.currentTimeMillis() / 1000 + "";
			map.put("request_time", request_time);
			map.put("uid", user.getUserId());
			String sign = MayittUtils.sign(map);
			String data = "article_id=" + newsId + "&read_type=0&channel=" + user.getChannel()
					+ "&device_type=2&device_id=" + user.getDeviceId() + "&sm_device_id=" + user.getSmDeviceId()
					+ "&access=WIFI&version_code=" + MayittUtils.version_code + "&request_time=" + request_time
					+ "&uid=" + user.getUserId() + "&sign=" + sign;
			String p = PubKeySignature.a(data);
			map = new HashMap<String, String>();
			map.put("p", p);
			String postData = MayittUtils.getMultipartStr(map, boundary);
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getBooleanValue("success")) {
				return object;
			}
			logger.error("mayitt-{}:读取新闻失败,content={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("mayitt-{}:读取新闻异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;

	}

	public JSONObject readVideo(String newsId) {
		try {
			String url = "https://ktt.woyaoq.com/v5/article/complete_article.json?";
			Map<String, String> heads = MayittUtils.initHeader(user);
			String boundary = UUID.randomUUID().toString();
			heads.put("Content-Type", "multipart/form-data; boundary=" + boundary);

			Map<String, String> map = new HashMap<String, String>();
			map.put("article_id", newsId);
			map.put("read_type", "0");
			map.put("channel", user.getChannel());
			map.put("device_type", "2");
			map.put("device_id", user.getDeviceId());
			map.put("sm_device_id", user.getSmDeviceId());
			map.put("access", "WIFI");
			map.put("version_code", MayittUtils.version_code);
			String request_time = System.currentTimeMillis() / 1000 + "";
			map.put("request_time", request_time);
			map.put("uid", user.getUserId());
			String sign = MayittUtils.sign(map);
			String data = "article_id=" + newsId + "&read_type=0&channel=" + user.getChannel()
					+ "&device_type=2&device_id=" + user.getDeviceId() + "&sm_device_id=" + user.getSmDeviceId()
					+ "&access=WIFI&version_code=" + MayittUtils.version_code + "&request_time=" + request_time
					+ "&uid=" + user.getUserId() + "&sign=" + sign;
			String p = PubKeySignature.a(data);
			map = new HashMap<String, String>();
			map.put("p", p);
			String postData = MayittUtils.getMultipartStr(map, boundary);
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getBooleanValue("success")) {
				return object;
			}
			logger.error("mayitt-{}:读取视频失败,content={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("mayitt-{}:读取视频异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public JSONArray cointxList(String type) {
		try {
			int catId = 0;
			if ("wxpay".equals(type))// wx
				catId = 0;
			else if ("alipay".equals(type)) {// ali
				catId = 2;
			}
			String url = "https://ktt.woyaoq.com/v3/cash/apply_money_config.json?";
			Map<String, String> map = new HashMap<String, String>();
			map.put("type", catId + "");
			map.put("phone_code", user.getOpenudid());
			map.put("device_type", "2");
			map.put("phone_network", "WIFI");
			map.put("channel_code", user.getChannel());
			map.put("client_version", MayittUtils.app_version);
			map.put("sm_device_id", user.getSmDeviceId());
			map.put("device_id", user.getDeviceId());
			map.put("uuid", user.getUuid());
			map.put("openudid", user.getOpenudid());
			map.put("phone_sim", "1");
			map.put("carrier", CommonUtils.encode(user.getCarrier()));
			map.put("uid", user.getUserId());
			String sign = MayittUtils.sign(map);
			map.put("sign", sign);
			url = url + MayittUtils.getStr(map);
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.9.1");
			String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getBooleanValue("success")) {
				if ("wxpay".equals(type)) {
					return object.getJSONObject("items").getJSONArray("withdrawal_set");
				} else if ("alipay".equals(type)) {
					return object.getJSONObject("items").getJSONArray("alipay_set");
				}
			}
			logger.info("mayitt-{}:查询可提现金币列表失败,content={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("mayitt-{}:查询可提现金币列表异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public boolean cointxAli(int money) {
		try {
			String url = "https://ktt.woyaoq.com/v3/cash/alipay.json?";
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.9.1");
			String boundary = UUID.randomUUID().toString();
			heads.put("Content-Type", "multipart/form-data; boundary=" + boundary);
			Map<String, String> map = new HashMap<String, String>();
			map.put("money", money + "");
			map.put("account", user.getTxUser());
			map.put("name", user.getTxName());
			map.put("validate", "");
			map.put("phone_code", user.getOpenudid());
			map.put("device_type", "2");
			map.put("phone_network", "WIFI");
			map.put("channel_code", user.getChannel());
			map.put("client_version", MayittUtils.app_version);
			map.put("sm_device_id", user.getSmDeviceId());
			map.put("device_id", user.getDeviceId());
			map.put("uuid", user.getUuid());
			map.put("openudid", user.getOpenudid());
			map.put("phone_sim", "1");
			map.put("carrier", CommonUtils.encode(user.getCarrier()));
			map.put("uid", user.getUserId());
			String sign = MayittUtils.sign(map);
			map.put("sign", sign);
			String postData = MayittUtils.getMultipartStr(map, boundary);
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getBooleanValue("success")) {
				logger.info("mayitt-{}:支付宝提现成功,content={}", user.getUsername(), content);
				return true;
			}
			logger.error("mayitt-{}:支付宝提现失败,msg={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("mayitt-{}:支付宝提现金币异常,msg={}", user.getUsername(), e.getMessage());
		}
		return false;
	}

	public boolean cointxWx(int money) {
		try {
			String url = "https://ktt.woyaoq.com/v3/cash/wechat.json?";
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.9.1");
			String boundary = UUID.randomUUID().toString();
			heads.put("Content-Type", "multipart/form-data; boundary=" + boundary);
			Map<String, String> map = new HashMap<String, String>();
			map.put("money", money + "");
			map.put("name", user.getTxName());
			map.put("validate", "");
			map.put("phone_code", user.getOpenudid());
			map.put("device_type", "2");
			map.put("phone_network", "WIFI");
			map.put("channel_code", user.getChannel());
			map.put("client_version", MayittUtils.app_version);
			map.put("sm_device_id", user.getSmDeviceId());
			map.put("device_id", user.getDeviceId());
			map.put("uuid", user.getUuid());
			map.put("openudid", user.getOpenudid());
			map.put("phone_sim", "1");
			map.put("carrier", CommonUtils.encode(user.getCarrier()));
			map.put("uid", user.getUserId());
			String sign = MayittUtils.sign(map);
			map.put("sign", sign);
			String postData = MayittUtils.getMultipartStr(map, boundary);
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getBooleanValue("success")) {
				logger.info("mayitt-{}:微信提现成功,content={}", user.getUsername(), content);
				return true;
			}
			logger.error("mayitt-{}:微信提现失败,msg={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("mayitt-{}:微信提现金币异常,msg={}", user.getUsername(), e.getMessage());
		}
		return false;
	}

	public boolean sendData() {
		try {
			if (eventList.size() < 1)
				return true;
			for (int i = 0; i < eventList.size(); i++) {
				JSONObject event = eventList.getJSONObject(i);
				event.put("_flush_time", System.currentTimeMillis());
			}
			String x = TaottUtils.encodeData(eventList.toJSONString());
			int crc = x.hashCode();
			String url = "https://sc.baertt.com:8106/sa?project=kttproduction";
			String postData = "data_list=" + x + "&gzip=1&crc=" + crc;
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent",
					"Dalvik/2.1.0 (Linux; U; Android " + user.getLogOsVersion() + "; " + user.getModel() + ")");
			heads.put("Content-Type", "application/x-www-form-urlencoded");
			int code = MobileHttpUrlConnectUtils.httpPostStatusCode(url, postData, heads, null);
			// logger.info("mayitt-{}:发送日志结果|code={}", user.getUsername(), code);
			if (code == 200)
				return true;
		} catch (Exception e) {
			logger.error("mayitt-{}:发送手机日志异常,msg={}", user.getUsername(), e.getMessage());
		}
		return false;
	}

	public JSONObject kkUrl() {
		JSONObject userinfo = userinfo();
		String content = "";
		try {
			String url = "https://ktt.woyaoq.com/TaskCenter/daily_task?";
			String token_sign = userinfo.getString("token_sign");
			String cookieStr = token_sign.replace("zn=", "cookie=").replace("zt=", "cookie_id=");
			String request_time = System.currentTimeMillis() / 1000 + "";
			url += userinfo.getString("token_sign") + "&access=WIFI&app_version=" + MayittUtils.app_version
					+ "&carrier=" + CommonUtils.encode(user.getCarrier()) + "&channel=" + user.getChannel() + "&"
					+ cookieStr + "&device_brand=" + user.getVendor().toLowerCase() + "&device_id=" + user.getDeviceId()
					+ "&device_model=" + CommonUtils.encode(user.getModel())
					+ "&device_type=android&mc=D8%3A63%3A75%3A92%3A20%3AE2&openudid=" + user.getOpenudid() + "&os_api="
					+ user.getOsApi() + "&os_version=" + user.getOsVersion() + "&request_time=" + request_time
					+ "&resolution=1080.0x1920.0&sim=1&sm_device_id=" + user.getSmDeviceId() + "&uid="
					+ user.getUserId() + "&uuid=" + user.getImei();
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			heads.put("Accept-Encoding", "gzip, deflate");
			heads.put("User-Agent", user.getUserAgent());
			heads.put("X-Requested-With", "com.ldzs.zhangxin");
			heads.put("Upgrade-Insecure-Requests", "1");
			content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
			Document doc = Jsoup.parse(content);
			Elements taskEl = doc.getElementsByClass("Earns-kankan");
			Elements hongbaoEl = doc.getElementsByClass("Earns-hongbao");
			JSONObject object = new JSONObject();
			if (taskEl != null) {
				String c = taskEl.get(0).attr("onclick");
				int start = c.indexOf("openWindows('") + 13;
				int end = c.indexOf("'", start);
				String kankanUrl = c.substring(start, end);
				logger.info("mayitt-{}: 解析任务中心的看看赚地址成功 url={}", user.getUsername(), kankanUrl);
				object.put("kankanUrl", kankanUrl);
			}
			if (hongbaoEl != null) {
				String c = hongbaoEl.get(0).attr("onclick");
				int start = c.indexOf("openWindows('") + 13;
				int end = c.indexOf("'", start);
				String hongbaoUrl = c.substring(start, end);
				logger.info("mayitt-{}: 解析任务中心的红包赚地址成功 url={}", user.getUsername(), hongbaoUrl);
				object.put("hongbaoUrl", hongbaoUrl);
			}
			logger.error("mayitt-{}:查询kkUrl页面异常,content={}", user.getUsername(), content);
			return object;
		} catch (Exception e) {
			logger.error("mayitt-{}:查询kkUrl页面异常,msg={}", user.getUsername(), content);
		}
		return null;
	}

	public JSONObject adTaskUrl() {
		JSONObject urlObject = kkUrl();
		if (StringUtils.isBlank(urlObject.getString("kankanUrl"))) {
			adTaskData = null;
			return null;
		}
		String kankanUrl = urlObject.getString("kankanUrl");
		String hongbaoUrl = urlObject.getString("hongbaoUrl");
		
		try {
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			heads.put("Accept-Encoding", "gzip, deflate");
			heads.put("User-Agent", user.getUserAgent());
			heads.put("X-Requested-With", "com.ldzs.zhangxin");
			heads.put("Upgrade-Insecure-Requests", "1");
			String content = MobileHttpUrlConnectUtils.httpGet(kankanUrl, heads, null);
			Document doc = Jsoup.parse(content);
			Element task_ad_score_num = doc.getElementById("task_ad_score_num");
			Element task_ad_score_num_num = doc.getElementById("task_hot_score_num");
			JSONObject object = new JSONObject();
			if (task_ad_score_num != null) {
				String c = task_ad_score_num.attr("onclick");
				int start = c.indexOf("openWindows('") + 13;
				int end = c.indexOf("'", start);
				String taskUrl = c.substring(start, end);
				logger.info("mayitt-{}: 解析看看赚的看广告地址成功  url={}", user.getUsername(), taskUrl);
				adTaskData(taskUrl);
				object.put("adTaskUrl", taskUrl);
				String startTaskContent = adTaskUrlGet(taskUrl);
				int startTaskContents = startTaskContent.indexOf("var url='") + 9;
				int startTaskContente = startTaskContent.indexOf("'", startTaskContents);
				startTaskUrl = startTaskContent.substring(startTaskContents, startTaskContente);
				logger.info("mayitt-{}: 解析看广告的开始嫌地址成功 url={}", user.getUsername(), startTaskUrl);
			}
			if (task_ad_score_num_num != null) {
				String c = task_ad_score_num_num.attr("onclick");
				int start = c.indexOf("openWindows('") + 13;
				int end = c.indexOf("'", start);
				String taskUrl = c.substring(start, end);
				logger.info("mayitt-{}: 解析看看赚的看执文地址成功  url={}", user.getUsername(), taskUrl);
				object.put("kkTaskUrl", taskUrl);
				adTaskUrlGet(taskUrl);
			}
			String taskApiUrl = content.substring(content.indexOf("/TaskCenter/task_browse?"),
					content.indexOf("'", content.indexOf("/TaskCenter/task_browse?")));
			taskApiUrl = "https://ktt.woyaoq.com" + taskApiUrl;
			object.put("taskApiUrl", taskApiUrl);

			content = MobileHttpUrlConnectUtils.httpGet(hongbaoUrl, heads, null);
			String hongbaoApiUrl = content.substring(content.indexOf("/TaskCenter/task_browse?"),
					content.indexOf("'", content.indexOf("/TaskCenter/task_browse?")));
			hongbaoApiUrl = "https://ktt.woyaoq.com" + hongbaoApiUrl;
			object.put("hongbaoApiUrl", hongbaoApiUrl);
			logger.info("mayitt-{}:查询adTaskUrl页面异常,msg={}", user.getUsername(), object);
			return object;
		} catch (Exception e) {
			logger.error("mayitt-{}:查询adTaskUrl页面异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public String adTaskUrlGet(String url) {
		try {
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			heads.put("Accept-Encoding", "gzip, deflate");
			heads.put("User-Agent", user.getUserAgent());
			heads.put("X-Requested-With", "com.ldzs.zhangxin");
			heads.put("Upgrade-Insecure-Requests", "1");
			String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
			return content;
		} catch (Exception e) {
			logger.error("mayitt-{}:查询任务页面异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public JSONObject startAdTask() {
		if (StringUtils.isBlank(startTaskUrl)) {
			adTaskUrl();
		}
		try {
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept", "*/*");
			heads.put("Accept-Encoding", "gzip, deflate");
			heads.put("User-Agent", user.getUserAgent());
			heads.put("X-Requested-With", "XMLHttpRequest");
			heads.put("Origin", "https://ktt.woyaoq.com");
			String postData = "type=1";
			String content = MobileHttpUrlConnectUtils.httpPost(startTaskUrl, postData, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			logger.info("mayitt-{}: 开始广告任务返回数据={}", user.getUsername(), object);
			return object;
		} catch (Exception e) {
			logger.error("mayitt-{}:开始广告任务异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public JSONObject adTaskData(String url) {
		try {
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			heads.put("Accept-Encoding", "gzip, deflate");
			heads.put("User-Agent", user.getUserAgent());
			heads.put("X-Requested-With", "com.ldzs.zhangxin");
			heads.put("Upgrade-Insecure-Requests", "1");
			String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
			content = content.replace("\r\n", "").replace("\r", "").replace("\t", "");
			int start = content.indexOf("var e=") + 6;
			int end = content.indexOf("}]};", start) + 3;
			content = content.substring(start, end);
			JSONObject object = JSONObject.parseObject(content);
			adTaskData = object;
			logger.info("mayitt-{}: 看广告中的广告数据={}", user.getUsername(), object.toJSONString());
			return object;
		} catch (Exception e) {
			logger.error("mayitt-{}:查询广告id异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public JSONObject taskApi() {
		JSONObject taskUrl = adTaskUrl();
		if (taskUrl == null || StringUtils.isBlank(taskUrl.getString("taskApiUrl"))) {
			return null;
		}
		String url = taskUrl.getString("taskApiUrl");
		try {
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept", "*/*");
			heads.put("Accept-Encoding", "gzip, deflate");
			heads.put("User-Agent", user.getUserAgent());
			heads.put("X-Requested-With", "XMLHttpRequest");
			heads.put("Origin", "https://ktt.woyaoq.com");
			String content = MobileHttpUrlConnectUtils.httpPost(url, null, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			logger.info("mayitt-{}: taskApi数据={}", user.getUsername(), object.toJSONString());
			return object;
		} catch (Exception e) {
			logger.error("mayitt-{}:查询taskApi异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}
	
	public JSONObject hongbaoApi() {
		JSONObject taskUrl = adTaskUrl();
		if (taskUrl == null || StringUtils.isBlank(taskUrl.getString("hongbaoApiUrl"))) {
			return null;
		}
		String url = taskUrl.getString("hongbaoApiUrl");
		try {
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept", "*/*");
			heads.put("Accept-Encoding", "gzip, deflate");
			heads.put("User-Agent", user.getUserAgent());
			heads.put("X-Requested-With", "XMLHttpRequest");
			heads.put("Origin", "https://ktt.woyaoq.com");
			String content = MobileHttpUrlConnectUtils.httpPost(url, null, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			logger.info("mayitt-{}: hongbaoApiUrl数据={}", user.getUsername(), object.toJSONString());
			return object;
		} catch (Exception e) {
			logger.error("mayitt-{}:查询hongbaoApiUrl异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public JSONObject readRwCoin(String ad_id) {
		try {
			String url = "https://ktt.woyaoq.com/v5/user/task_ad_callback.json?";
			Map<String, String> heads = MayittUtils.initHeader(user);
			String boundary = UUID.randomUUID().toString();
			heads.put("Content-Type", "multipart/form-data; boundary=" + boundary);
			Map<String, String> map = new HashMap<String, String>();
			map.put("ad_id", ad_id);
			map.put("channel", user.getChannel());
			map.put("device_type", "2");
			map.put("device_id", user.getDeviceId());
			map.put("sm_device_id", user.getSmDeviceId());
			map.put("access", "WIFI");
			map.put("version_code", MayittUtils.version_code);
			String request_time = System.currentTimeMillis() / 1000 + "";
			map.put("request_time", request_time);
			map.put("uid", user.getUserId());
			String sign = MayittUtils.sign(map);
			String data = "ad_id=" + ad_id + "&channel=" + user.getChannel() + "&device_type=2&device_id="
					+ user.getDeviceId() + "&sm_device_id=" + user.getSmDeviceId() + "&access=WIFI&version_code="
					+ MayittUtils.version_code + "&request_time=" + request_time + "&uid=" + user.getUserId() + "&sign="
					+ sign;
			String p = PubKeySignature.a(data);
			map = new HashMap<String, String>();
			map.put("p", p);
			String postData = MayittUtils.getMultipartStr(map, boundary);
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getBooleanValue("success")) {
				return object;
			}
			logger.error("mayitt-{}:读取广告任务失败,content={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("mayitt-{}:读取广告任务异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public JSONObject readAdCoin(String ad_id) {
		try {
			String url = "https://ktt.woyaoq.com/v5/user/task_ad_callback.json?";
			Map<String, String> heads = MayittUtils.initHeader(user);
			String boundary = UUID.randomUUID().toString();
			heads.put("Content-Type", "multipart/form-data; boundary=" + boundary);
			Map<String, String> map = new HashMap<String, String>();
			map.put("ad_id", ad_id);
			map.put("ad_type", "0");
			map.put("channel", user.getChannel());
			map.put("device_type", "2");
			map.put("device_id", user.getDeviceId());
			map.put("sm_device_id", user.getSmDeviceId());
			map.put("access", "WIFI");
			map.put("version_code", MayittUtils.version_code);
			String request_time = System.currentTimeMillis() / 1000 + "";
			map.put("request_time", request_time);
			map.put("uid", user.getUserId());
			String sign = MayittUtils.sign(map);
			String data = "ad_id=" + ad_id + "&ad_type=0&channel=" + user.getChannel() + "&device_type=2&device_id="
					+ user.getDeviceId() + "&sm_device_id=" + user.getSmDeviceId() + "&access=WIFI&version_code="
					+ MayittUtils.version_code + "&request_time=" + request_time + "&uid=" + user.getUserId() + "&sign="
					+ sign;
			String p = PubKeySignature.a(data);
			map = new HashMap<String, String>();
			map.put("p", p);
			String postData = MayittUtils.getMultipartStr(map, boundary);
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getBooleanValue("success")) {
				return object;
			}
			logger.error("mayitt-{}:读取广告任务失败,content={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("mayitt-{}:读取广告任务异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public JSONObject adlickstart(String task_id) {
		try {
			String url = "https://ktt.woyaoq.com/v5/user/adlickstart.json?";
			Map<String, String> heads = MayittUtils.initHeader(user);
			String boundary = UUID.randomUUID().toString();
			heads.put("Content-Type", "multipart/form-data; boundary=" + boundary);
			Map<String, String> map = new HashMap<String, String>();
			map.put("task_id", task_id);
			map.put("channel", user.getChannel());
			map.put("device_type", "2");
			map.put("device_id", user.getDeviceId());
			map.put("sm_device_id", user.getSmDeviceId());
			map.put("access", "WIFI");
			map.put("version_code", MayittUtils.version_code);
			String request_time = System.currentTimeMillis() / 1000 + "";
			map.put("request_time", request_time);
			map.put("uid", user.getUserId());
			String sign = MayittUtils.sign(map);
			String data = "task_id=" + task_id + "&channel=" + user.getChannel() + "&device_type=2&device_id="
					+ user.getDeviceId() + "&sm_device_id=" + user.getSmDeviceId() + "&access=WIFI&version_code="
					+ MayittUtils.version_code + "&request_time=" + request_time + "&uid=" + user.getUserId() + "&sign="
					+ sign;
			String p = PubKeySignature.a(data);
			map = new HashMap<String, String>();
			map.put("p", p);
			String postData = MayittUtils.getMultipartStr(map, boundary);
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getBooleanValue("success")) {
				return object;
			}
			logger.error("mayitt-{}:读取其它任务失败,content={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("mayitt-{}:读取其它任务异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public JSONObject bannerstatus(String task_id) {
		try {
			String url = "https://ktt.woyaoq.com/v5/user/bannerstatus.json?";
			Map<String, String> heads = MayittUtils.initHeader(user);
			String boundary = UUID.randomUUID().toString();
			heads.put("Content-Type", "multipart/form-data; boundary=" + boundary);
			Map<String, String> map = new HashMap<String, String>();
			map.put("task_id", task_id);
			map.put("channel", user.getChannel());
			map.put("device_type", "2");
			map.put("device_id", user.getDeviceId());
			map.put("sm_device_id", user.getSmDeviceId());
			map.put("access", "WIFI");
			map.put("version_code", MayittUtils.version_code);
			String request_time = System.currentTimeMillis() / 1000 + "";
			map.put("request_time", request_time);
			map.put("uid", user.getUserId());
			String sign = MayittUtils.sign(map);
			String data = "task_id=" + task_id + "&channel=" + user.getChannel() + "&device_type=2&device_id="
					+ user.getDeviceId() + "&sm_device_id=" + user.getSmDeviceId() + "&access=WIFI&version_code="
					+ MayittUtils.version_code + "&request_time=" + request_time + "&uid=" + user.getUserId() + "&sign="
					+ sign;
			String p = PubKeySignature.a(data);
			map = new HashMap<String, String>();
			map.put("p", p);
			String postData = MayittUtils.getMultipartStr(map, boundary);
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getBooleanValue("success")) {
				return object;
			}
			logger.error("mayitt-{}:读取其它任务失败,content={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("mayitt-{}:读取其它任务异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public JSONObject adlickend(String task_id) {
		try {
			String url = "https://ktt.woyaoq.com/v5/user/adlickend.json?";
			Map<String, String> heads = MayittUtils.initHeader(user);
			String boundary = UUID.randomUUID().toString();
			heads.put("Content-Type", "multipart/form-data; boundary=" + boundary);
			Map<String, String> map = new HashMap<String, String>();
			map.put("task_id", task_id);
			map.put("channel", user.getChannel());
			map.put("device_type", "2");
			map.put("device_id", user.getDeviceId());
			map.put("sm_device_id", user.getSmDeviceId());
			map.put("access", "WIFI");
			map.put("version_code", MayittUtils.version_code);
			String request_time = System.currentTimeMillis() / 1000 + "";
			map.put("request_time", request_time);
			map.put("uid", user.getUserId());
			String sign = MayittUtils.sign(map);
			String data = "task_id=" + task_id + "&channel=" + user.getChannel() + "&device_type=2&device_id="
					+ user.getDeviceId() + "&sm_device_id=" + user.getSmDeviceId() + "&access=WIFI&version_code="
					+ MayittUtils.version_code + "&request_time=" + request_time + "&uid=" + user.getUserId() + "&sign="
					+ sign;
			String p = PubKeySignature.a(data);
			map = new HashMap<String, String>();
			map.put("p", p);
			String postData = MayittUtils.getMultipartStr(map, boundary);
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getBooleanValue("success")) {
				return object;
			}
			logger.error("mayitt-{}:读取其它任务失败,content={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("mayitt-{}:读取其它任务异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public JSONObject taskAdStatus(String ad_id, String is_config) {
		try {
			String url = "https://ktt.woyaoq.com/v5/user/task_ad_status.json?";
			Map<String, String> heads = MayittUtils.initHeader(user);
			String boundary = UUID.randomUUID().toString();
			heads.put("Content-Type", "multipart/form-data; boundary=" + boundary);
			Map<String, String> map = new HashMap<String, String>();
			map.put("ad_id", ad_id);
			map.put("ad_type", "0");
			map.put("channel", user.getChannel());
			map.put("device_type", "2");
			map.put("device_id", user.getDeviceId());
			map.put("sm_device_id", user.getSmDeviceId());
			map.put("access", "WIFI");
			map.put("version_code", MayittUtils.version_code);
			String request_time = System.currentTimeMillis() / 1000 + "";
			map.put("request_time", request_time);
			map.put("uid", user.getUserId());
			String sign = MayittUtils.sign(map);
			String data = "ad_id=" + ad_id + "&is_config=" + is_config + "&channel=" + user.getChannel()
					+ "&device_type=2&device_id=" + user.getDeviceId() + "&sm_device_id=" + user.getSmDeviceId()
					+ "&access=WIFI&version_code=" + MayittUtils.version_code + "&request_time=" + request_time
					+ "&uid=" + user.getUserId() + "&sign=" + sign;
			String p = PubKeySignature.a(data);
			map = new HashMap<String, String>();
			map.put("p", p);
			String postData = MayittUtils.getMultipartStr(map, boundary);
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getBooleanValue("success")) {
				return object;
			}
			logger.error("mayitt-{}:读取看看赚失败,content={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("mayitt-{}:读取看看赚异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public static void main(String[] args) {
		MayittUser user = new MayittUser();
		user.setUsername("17755117870");
		user.setUserId("39781740");
		MayittHttp http = MayittHttp.getInstance(user);
	}

}
