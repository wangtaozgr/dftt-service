package com.atao.dftt.http;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atao.dftt.model.ZqttUser;
import com.atao.dftt.util.CommonUtils;
import com.atao.dftt.util.MobileHttpUrlConnectUtils;
import com.atao.dftt.util.TaottUtils;
import com.atao.dftt.util.ZqttUtils;
import com.atao.dftt.util.zqtt.PubKeySignature;

public class ZqttHttp {
	private static Logger logger = LoggerFactory.getLogger(ZqttHttp.class);
	private static Map<String, ZqttHttp> userMap = new HashMap<String, ZqttHttp>();
	public ZqttUser user;
	public String zqkey_id;
	public String zqkey;
	public Long behot_time = System.currentTimeMillis();
	public String oid = "";
	public Long behot_timev = System.currentTimeMillis();
	public String oidv = "";
	public JSONObject adTaskData;
	public String startTaskUrl;

	public JSONArray eventList = new JSONArray();
	public static Random random = new Random();

	public ZqttHttp(ZqttUser user) {
		this.user = user;
	}

	public static synchronized ZqttHttp getInstance(ZqttUser user) {
		ZqttHttp http = (ZqttHttp) userMap.get(user.getUsername());
		if (http == null) {
			http = new ZqttHttp(user);
			userMap.put(user.getUsername(), http);
		}
		return http;
	}

	public ZqttHttp refreshUser(ZqttUser user) {
		ZqttHttp http = new ZqttHttp(user);
		userMap.put(user.getUsername(), http);
		return http;
	}

	public JSONObject login() {
		try {
			String url = "https://kandian.youth.cn/v3/user/mobile/login.json?";
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.3.0");
			String boundary = UUID.randomUUID().toString();
			heads.put("Content-Type", "multipart/form-data; boundary=" + boundary);
			Map<String, String> map = new HashMap<String, String>();
			map.put("sm_device_id", user.getSmDeviceId());
			map.put("device_id", user.getDeviceId());
			map.put("access", "WIFI");
			map.put("channel", user.getChannel());
			map.put("app_version", ZqttUtils.app_version);
			map.put("version_code", ZqttUtils.version_code);
			map.put("device_platform", "android");
			map.put("os_version", user.getOsVersion());
			map.put("os_api", user.getOsApi());
			map.put("device_model", CommonUtils.encode(user.getModel()));
			map.put("request_time", System.currentTimeMillis() / 1000 + "");
			map.put("openudid", user.getOpenudid());
			map.put("phone_sim", "1");
			map.put("carrier", CommonUtils.encode(user.getCarrier()));
			map.put("uid", user.getUserId());
			map.put("mobile", user.getUsername());
			map.put("password", user.getPassword());
			String sign = ZqttUtils.sign(map);
			map.put("sign", sign);
			String postData = ZqttUtils.getMultipartStr(map, boundary);
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getBooleanValue("success")) {
				logger.info("zqtt-{}:用户登陆成功,result={}", user.getUsername(), object.toString());
				return object;
			}
			logger.error("zqtt-{}:用户登陆时失败,result={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("zqtt-{}:用户登陆时发生异常!{}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public JSONObject signature() {
		try {
			String url = "https://kandian.youth.cn/v5/user/signature.json?";
			Map<String, String> heads = ZqttUtils.initHeader(user);
			String boundary = UUID.randomUUID().toString();
			heads.put("Content-Type", "multipart/form-data; boundary=" + boundary);
			Map<String, String> map = new HashMap<String, String>();
			map.put("sm_device_id", user.getSmDeviceId());
			map.put("zqkey_id", user.getZqkeyId());
			map.put("zqkey", user.getZqkey());
			map.put("channel", user.getChannel());
			map.put("device_id", user.getDeviceId());
			map.put("app_version", ZqttUtils.app_version);
			map.put("version_code", ZqttUtils.version_code);
			String request_time = System.currentTimeMillis() / 1000 + "";
			map.put("request_time", request_time);
			map.put("uid", user.getUserId());
			String sign = ZqttUtils.sign(map);
			map.put("sign", sign);
			String data = "sm_device_id=" + user.getSmDeviceId() + "&zqkey_id=" + user.getZqkeyId() + "&zqkey="
					+ user.getZqkey() + "&channel=" + user.getChannel() + "&device_id=" + user.getDeviceId()
					+ "&app_version=" + ZqttUtils.app_version + "&version_code=" + ZqttUtils.version_code
					+ "&request_time=" + request_time + "&uid=" + user.getUserId() + "&sign=" + sign;

			String p = PubKeySignature.a(data);
			map = new HashMap<String, String>();
			map.put("p", p);
			String postData = ZqttUtils.getMultipartStr(map, boundary);
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getBooleanValue("success")) {
				user.setZqkey(object.getString("zqkey"));
				user.setZqkeyId(object.getString("zqkey_id"));
				refreshUser(user);
				return object;
			}
			logger.error("zqtt-{}:用户更新签名时失败,result={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("zqtt-{}:用户更新签名时异常!{}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public JSONObject queryMyCoin() {
		JSONObject object = userinfo();
		return object;
	}

	public JSONObject userinfo() {
		try {
			String url = "https://kandian.youth.cn/v3/user/userinfo.json?";
			Map<String, String> map = ZqttUtils.init(user);
			map.put("uid", user.getUserId());
			String sign = ZqttUtils.sign(map);
			map.put("sign", sign);
			String postData = ZqttUtils.getStr(map);
			url = url + postData;
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.3.0");
			String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getBooleanValue("success")) {
				JSONObject result = object.getJSONObject("items");
				return result;
			}
			logger.error("zqtt-{}:获取用户信息失败,msg={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("zqtt-{}:获取用户信息异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public boolean qiandao() {
		// JSONObject userinfo = userinfo();
		try {
			String url = "https://kd.youth.cn/cash/sign?_=" + new Date().getTime();
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept", "application/json");
			heads.put("Origin", "https://kd.youth.cn");
			heads.put("X-Requested-With", "XMLHttpRequest");
			heads.put("User-Agent", user.getUserAgent());
			heads.put("Accept-Encoding", "gzip, deflate");
			heads.put("Content-Type", "application/x-www-form-urlencoded");
			String postData = "cookie=" + user.getZqkey() + "&cookie_id=" + user.getZqkeyId() + "&app_version="
					+ ZqttUtils.app_version + "&channel=" + user.getChannel();
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getIntValue("status") == 1) {
				logger.info("zqtt-{}:签到成功", user.getUsername());
				return true;
			}
			logger.error("zqtt-{}:签到失败, result={}", user.getUsername(), object.toJSONString());
		} catch (Exception e) {
			logger.error("zqtt-{}:打卡异常,msg={}", user.getUsername(), e.getMessage());
		}
		return false;
	}

	public boolean daka() {
		try {
			String url = "https://ktt.woyaoq.com/v5/article/treasure_chest.json?";
			Map<String, String> heads = ZqttUtils.initHeader(user);
			String boundary = UUID.randomUUID().toString();
			heads.put("Content-Type", "multipart/form-data; boundary=" + boundary);
			Map<String, String> map = new HashMap<String, String>();
			map.put("channel", user.getChannel());
			map.put("device_type", "2");
			map.put("device_id", user.getDeviceId());
			map.put("sm_device_id", user.getSmDeviceId());
			map.put("access", "WIFI");
			map.put("version_code", ZqttUtils.version_code);
			String request_time = System.currentTimeMillis() / 1000 + "";
			map.put("request_time", request_time);
			map.put("uid", user.getUserId());
			String sign = ZqttUtils.sign(map);
			map.put("sign", sign);
			String data = "channel=" + user.getChannel() + "&device_type=2&device_id=" + user.getDeviceId()
					+ "&sm_device_id=" + user.getSmDeviceId() + "&access=WIFI&version_code=" + ZqttUtils.version_code
					+ "&request_time=" + request_time + "&uid=" + user.getUserId() + "&sign=" + sign;
			String p = PubKeySignature.a(data);
			map = new HashMap<String, String>();
			map.put("p", p);
			String postData = ZqttUtils.getMultipartStr(map, boundary);
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getBooleanValue("success")) {
				logger.info("zqtt-{}:打卡成功", user.getUsername());
				return true;
			}
			logger.error("zqtt-{}:打卡失败, result={}", user.getUsername(), object.toJSONString());
		} catch (Exception e) {
			logger.error("zqtt-{}:打卡异常,msg={}", user.getUsername(), e.getMessage());
		}
		return false;
	}

	public JSONArray newsList() {
		try {
			String url = "https://kandian.youth.cn/v3/article/lists.json?";
			Map<String, String> map = ZqttUtils.init(user);
			map.put("catid", "0");
			map.put("op", "0");
			map.put("behot_time", behot_time + "");
			map.put("oid", oid);
			// map.put("step", "0");
			map.put("video_catid", "1453");
			String sign = ZqttUtils.sign(map);
			map.put("sign", sign);
			String postData = ZqttUtils.getStr(map);
			url = url + postData;
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.3.0");
			String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getBooleanValue("success")) {
				top2();
				JSONArray list = object.getJSONArray("items");
				if (list.size() > 0) {
					behot_time = list.getJSONObject(0).getLong("behot_time");
					oid = list.getJSONObject(0).getString("id");
				}
				return list;
			}
			logger.error("zqtt-{}:获取新闻列表失败,msg={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("zqtt-{}:获取新闻列表异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public boolean top2() {
		try {
			String url = "http://kandian.youth.cn/v6/article/top2.json";
			Map<String, String> map = ZqttUtils.initLogParam(user);
			String token = ZqttUtils.token(map);
			map.put("token", token);
			String postData = ZqttUtils.getStr(map);
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.3.0");
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			logger.info(content);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getBooleanValue("success")) {
				return true;
			}
			logger.error("zqtt-{}:发送新闻列表日志失败,msg={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("zqtt-{}:发送新闻列表日志异常,msg={}", user.getUsername(), e.getMessage());
		}
		return false;
	}

	public boolean start() {
		try {
			String url = "https://kandian.youth.cn/v6/count/start.json";
			Map<String, String> map = ZqttUtils.initLogParam(user);
			String token = ZqttUtils.token(map);
			map.put("token", token);
			String postData = ZqttUtils.getStr(map);
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.3.0");
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			logger.info(content);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getBooleanValue("success")) {
				return true;
			}
			logger.error("zqtt-{}:发送启动日志(start)失败,msg={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("zqtt-{}:发送启动日志(start)异常,msg={}", user.getUsername(), e.getMessage());
		}
		return false;
	}

	public boolean get2() {
		try {
			String url = "https://kandian.youth.cn/v6/popup/get2.json?";
			Map<String, String> map = ZqttUtils.initLogParam(user);
			String token = ZqttUtils.token(map);
			map.put("token", token);
			String postData = ZqttUtils.getStr(map);
			url = url + postData;
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.3.0");
			String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
			logger.info(content);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getBooleanValue("success")) {
				return true;
			}
			logger.error("zqtt-{}:发送启动日志(get2)失败,msg={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("zqtt-{}:发送启动日志(get2)异常,msg={}", user.getUsername(), e.getMessage());
		}
		return false;

	}

	public JSONArray videoList() {
		try {
			String url = "https://ktt.woyaoq.com/v4/article/lists.json?";
			Map<String, String> map = ZqttUtils.init(user);
			map.put("uid", user.getUserId());
			map.put("catid", "1453");
			map.put("op", "0");
			map.put("behot_time", behot_timev + "");
			map.put("oid", oidv);
			map.put("video_catid", "0");
			String sign = ZqttUtils.sign(map);
			map.put("sign", sign);
			String postData = ZqttUtils.getStr(map);
			url = url + postData;
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.3.0");
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
			logger.error("zqtt-{}:获取视频列表失败,msg={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("zqtt-{}:获取视频列表异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;

	}

	/**
	 * 进入新闻详细时，为计时准备的
	 * 
	 * @param newsId
	 * @return
	 */
	public JSONObject gotoNews(String newsId, String catid) {
		try {
			String url = "https://kandian.youth.cn/v5/article/info/get.json?";
			Map<String, String> heads = ZqttUtils.initHeader(user);
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", newsId);
			map.put("catid", catid);
			map.put("from", "home");
			map.put("ad", "1");
			map.put("sm_device_id", user.getSmDeviceId());
			map.put("zqkey_id", user.getZqkeyId());
			map.put("zqkey", user.getZqkey());
			map.put("channel", user.getChannel());
			map.put("device_id", user.getDeviceId());
			map.put("app_version", ZqttUtils.app_version);
			map.put("version_code", ZqttUtils.version_code);
			String request_time = System.currentTimeMillis() / 1000 + "";
			map.put("request_time", request_time);
			map.put("uid", user.getUserId());
			String sign = ZqttUtils.sign(map);
			String data = "id=" + newsId + "&catid=" + catid + "&from=home&ad=1&sm_device_id=" + user.getSmDeviceId()
					+ "&zqkey_id=" + user.getZqkeyId() + "&zqkey=" + user.getZqkey() + "&channel=" + user.getChannel()
					+ "&device_id=" + user.getDeviceId() + "&app_version=" + ZqttUtils.app_version + "&version_code="
					+ ZqttUtils.version_code + "&request_time=" + request_time + "&uid=" + user.getUserId() + "&sign="
					+ sign;
			String p = PubKeySignature.a(data);
			url = url + "p=" + p;
			String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
			logger.error("zqtt-{}:进入新闻,content={}", user.getUsername(), content);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getBooleanValue("success")) {
				return object;
			}
			logger.error("zqtt-{}:进入新闻失败,content={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("zqtt-{}:进入新闻异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public JSONObject leaveNews(String newsId, String rid) {
		try {
			String url = "https://kandian.youth.cn/v3/article/leave/event.json?";
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.3.0");
			String boundary = UUID.randomUUID().toString();
			heads.put("Content-Type", "multipart/form-data; boundary=" + boundary);
			Map<String, String> map = ZqttUtils.init(user);
			map.put("rid", rid);
			map.put("article_id", newsId);
			String sign = ZqttUtils.sign(map);
			map.put("sign", sign);
			String postData = ZqttUtils.getMultipartStr(map, boundary);
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			logger.info("zqtt-{}:离开新闻,content={}", user.getUsername(), content);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getBooleanValue("success")) {
				return object;
			}
			logger.error("zqtt-{}:离开新闻失败,content={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("zqtt-{}:离开新闻异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	// 文章列表统计
	public JSONObject article_collect(String newsId) {
		try {
			String url = "https://kandian.youth.cn/v3/article/shows.json??";
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.3.0");
			String boundary = UUID.randomUUID().toString();
			heads.put("Content-Type", "multipart/form-data; boundary=" + boundary);
			Map<String, String> map = ZqttUtils.init(user);
			map.put("ids", newsId + "_1");
			map.put("catid", "0");
			map.put("from", "home");
			map.put("sm_device_id", user.getSmDeviceId());
			map.put("zqkey_id", user.getZqkeyId());
			map.put("zqkey", user.getZqkey());
			map.put("phone_code", user.getOpenudid());
			map.put("device_type", "2");
			map.put("phone_network", "WIFI");
			map.put("channel_code", user.getChannel());
			map.put("client_version", ZqttUtils.app_version);
			map.put("device_id", user.getDeviceId());
			map.put("uuid", user.getUuid());
			map.put("phone_sim", "1");
			map.put("phone_network", "WIFI");
			map.put("carrier", CommonUtils.encode(user.getCarrier()));
			map.put("uid", user.getUserId());
			String sign = ZqttUtils.sign(map);
			map.put("sign", sign);
			String postData = ZqttUtils.getMultipartStr(map, boundary);
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			logger.info("zqtt-{}:文章列表统计,content={}", user.getUsername(), content);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getBooleanValue("success")) {
				return object;
			}
			logger.error("zqtt-{}:文章列表统计失败,content={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("zqtt-{}:文章列表统计异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}
	
	//用户阅读时长
	public JSONObject user_readtime(int second) {
		try {
			String url = "https://kandian.youth.cn/v5/user/stay.json?";
			Map<String, String> heads = ZqttUtils.initHeader(user);
			String boundary = UUID.randomUUID().toString();
			heads.put("Content-Type", "multipart/form-data; boundary=" + boundary);
			Map<String, String> map = new HashMap<String, String>();
			map.put("second", second+"");
			map.put("sm_device_id", user.getSmDeviceId());
			map.put("zqkey_id", user.getZqkeyId());
			map.put("zqkey", user.getZqkey());
			map.put("channel", user.getChannel());
			map.put("device_id", user.getDeviceId());
			map.put("app_version", ZqttUtils.app_version);
			map.put("version_code", ZqttUtils.version_code);
			String request_time = System.currentTimeMillis() / 1000 + "";
			map.put("request_time", request_time);
			map.put("uid", user.getUserId());
			String sign = ZqttUtils.sign(map);
			String data = "second=" + second + "&sm_device_id=" + user.getSmDeviceId() + "&zqkey_id="
					+ user.getZqkeyId() + "&zqkey=" + user.getZqkey() + "&channel=" + user.getChannel() + "&device_id="
					+ user.getDeviceId() + "&app_version=" + ZqttUtils.app_version + "&version_code="
					+ ZqttUtils.version_code + "&request_time=" + request_time + "&uid=" + user.getUserId() + "&sign="
					+ sign;
			String p = PubKeySignature.a(data);
			map = new HashMap<String, String>();
			map.put("p", p);
			String postData = ZqttUtils.getMultipartStr(map, boundary);
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			logger.info("zqtt-{}:用户阅读时长,content={}", user.getUsername(), content);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getBooleanValue("success")) {
				return object;
			}
			logger.error("zqtt-{}:用户阅读时长失败,content={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("zqtt-{}:用户阅读时长异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public JSONObject readNews(String newsId) {
		try {
			String url = "https://kandian.youth.cn/v5/article/complete.json?";
			Map<String, String> heads = ZqttUtils.initHeader(user);
			String boundary = UUID.randomUUID().toString();
			heads.put("Content-Type", "multipart/form-data; boundary=" + boundary);
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", newsId);
			map.put("read_type", "0");
			map.put("sm_device_id", user.getSmDeviceId());
			map.put("zqkey_id", user.getZqkeyId());
			map.put("zqkey", user.getZqkey());
			map.put("channel", user.getChannel());
			map.put("device_id", user.getDeviceId());
			map.put("app_version", ZqttUtils.app_version);
			map.put("version_code", ZqttUtils.version_code);
			String request_time = System.currentTimeMillis() / 1000 + "";
			map.put("request_time", request_time);
			map.put("uid", user.getUserId());
			String sign = ZqttUtils.sign(map);
			String data = "id=" + newsId + "&read_type=0" + "&sm_device_id=" + user.getSmDeviceId() + "&zqkey_id="
					+ user.getZqkeyId() + "&zqkey=" + user.getZqkey() + "&channel=" + user.getChannel() + "&device_id="
					+ user.getDeviceId() + "&app_version=" + ZqttUtils.app_version + "&version_code="
					+ ZqttUtils.version_code + "&request_time=" + request_time + "&uid=" + user.getUserId() + "&sign="
					+ sign;
			String p = PubKeySignature.a(data);
			map = new HashMap<String, String>();
			map.put("p", p);
			String postData = ZqttUtils.getMultipartStr(map, boundary);
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			return object;
		} catch (Exception e) {
			logger.error("zqtt-{}:读取新闻异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public JSONObject readVideo(String newsId) {
		try {
			String url = "https://ktt.woyaoq.com/v5/article/complete_article.json?";
			Map<String, String> heads = ZqttUtils.initHeader(user);
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
			map.put("version_code", ZqttUtils.version_code);
			String request_time = System.currentTimeMillis() / 1000 + "";
			map.put("request_time", request_time);
			map.put("uid", user.getUserId());
			String sign = ZqttUtils.sign(map);
			String data = "article_id=" + newsId + "&read_type=0&channel=" + user.getChannel()
					+ "&device_type=2&device_id=" + user.getDeviceId() + "&sm_device_id=" + user.getSmDeviceId()
					+ "&access=WIFI&version_code=" + ZqttUtils.version_code + "&request_time=" + request_time + "&uid="
					+ user.getUserId() + "&sign=" + sign;
			String p = PubKeySignature.a(data);
			map = new HashMap<String, String>();
			map.put("p", p);
			String postData = ZqttUtils.getMultipartStr(map, boundary);
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getBooleanValue("success")) {
				return object;
			}
			logger.error("zqtt-{}:读取视频失败,content={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("zqtt-{}:读取视频异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public JSONArray cointxList(String type) {
		try {
			String url = "https://kandian.youth.cn/v6/withdraw/payMethodList.json?";
			Map<String, String> map = ZqttUtils.initLogParam(user);
			map.put("appid", "wxac1addaad7a006fe");
			String token = ZqttUtils.token(map);
			map.put("token", token);
			String postData = ZqttUtils.getStr(map);
			url = url + postData;
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent", "okhttp/3.3.0");
			String content = MobileHttpUrlConnectUtils.httpGet(url, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getBooleanValue("success")) {
				JSONArray list = object.getJSONObject("items").getJSONArray("list");
				if ("wxpay".equals(type)) {
					for (int i = 0; i < list.size(); i++) {
						if (list.getJSONObject(i).getIntValue("type") == 2) {
							return list.getJSONObject(i).getJSONArray("moneyList");
						}
					}
				} else if ("alipay".equals(type)) {
					for (int i = 0; i < list.size(); i++) {
						if (list.getJSONObject(i).getIntValue("type") == 1) {
							return list.getJSONObject(i).getJSONArray("moneyList");
						}
					}
				}
			}
			logger.info("zqtt-{}:查询可提现金币列表失败,content={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("zqtt-{}:查询可提现金币列表异常,msg={}", user.getUsername(), e.getMessage());
		}
		return null;
	}

	public boolean cointxAli(int money) {
		try {
			String url = "https://kandian.youth.cn/v5/withdraw/alipay.json?";
			Map<String, String> heads = ZqttUtils.initHeader(user);
			String boundary = UUID.randomUUID().toString();
			heads.put("Content-Type", "multipart/form-data; boundary=" + boundary);
			Map<String, String> map = new HashMap<String, String>();
			map.put("username", CommonUtils.encode(user.getTxName()));
			map.put("account", user.getTxUser());
			map.put("money", money + "");
			map.put("sm_device_id", user.getSmDeviceId());
			map.put("zqkey_id", user.getZqkeyId());
			map.put("zqkey", user.getZqkey());
			map.put("channel", user.getChannel());
			map.put("device_id", user.getDeviceId());
			map.put("app_version", ZqttUtils.app_version);
			map.put("version_code", ZqttUtils.version_code);
			String request_time = System.currentTimeMillis() / 1000 + "";
			map.put("request_time", request_time);
			map.put("uid", user.getUserId());
			String sign = ZqttUtils.sign(map);
			map.put("sign", sign);
			String data = "username=" + CommonUtils.encode(user.getTxName()) + "&account=" + user.getTxUser()
					+ "&money=" + money + "&sm_device_id=" + user.getSmDeviceId() + "&zqkey_id=" + user.getZqkeyId()
					+ "&zqkey=" + user.getZqkey() + "&channel=" + user.getChannel() + "&device_id=" + user.getDeviceId()
					+ "&app_version=" + ZqttUtils.app_version + "&version_code=" + ZqttUtils.version_code
					+ "&request_time=" + request_time + "&uid=" + user.getUserId() + "&sign=" + sign;
			String p = PubKeySignature.a(data);
			map = new HashMap<String, String>();
			map.put("p", p);
			String postData = ZqttUtils.getMultipartStr(map, boundary);
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getBooleanValue("success")) {
				logger.info("zqtt-{}:支付宝提现成功,content={}", user.getUsername(), content);
				return true;
			}
			logger.error("zqtt-{}:支付宝提现失败,msg={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("zqtt-{}:支付宝提现金币异常,msg={}", user.getUsername(), e.getMessage());
		}
		return false;
	}

	public boolean cointxWx(int money) {
		try {
			String url = "https://kandian.youth.cn//v5/wechat/withdraw1.json?";
			Map<String, String> heads = ZqttUtils.initHeader(user);
			String boundary = UUID.randomUUID().toString();
			heads.put("Content-Type", "multipart/form-data; boundary=" + boundary);
			Map<String, String> map = new HashMap<String, String>();
			map.put("username", CommonUtils.encode(user.getTxName()));
			map.put("money", money + "");
			map.put("validate", "");
			map.put("cardId", "");
			map.put("sm_device_id", user.getSmDeviceId());
			map.put("zqkey_id", user.getZqkeyId());
			map.put("zqkey", user.getZqkey());
			map.put("channel", user.getChannel());
			map.put("device_id", user.getDeviceId());
			map.put("app_version", ZqttUtils.app_version);
			map.put("version_code", ZqttUtils.version_code);
			String request_time = System.currentTimeMillis() / 1000 + "";
			map.put("request_time", request_time);
			map.put("uid", user.getUserId());
			String sign = ZqttUtils.sign(map);
			map.put("sign", sign);
			String data = "username=" + CommonUtils.encode(user.getTxName()) + "&money=" + money + "&account="
					+ user.getTxUser() + "&validate=&cardId=" + "&zqkey_id=" + user.getZqkeyId() + "&zqkey="
					+ user.getZqkey() + "&channel=" + user.getChannel() + "&device_id=" + user.getDeviceId()
					+ "&app_version=" + ZqttUtils.app_version + "&version_code=" + ZqttUtils.version_code
					+ "&request_time=" + request_time + "&uid=" + user.getUserId() + "&sign=" + sign;
			String p = PubKeySignature.a(data);
			map = new HashMap<String, String>();
			map.put("p", p);
			String postData = ZqttUtils.getMultipartStr(map, boundary);
			String content = MobileHttpUrlConnectUtils.httpPost(url, postData, heads, null);
			JSONObject object = JSONObject.parseObject(content);
			if (object.getBooleanValue("success")) {
				logger.info("zqtt-{}:微信提现成功,content={}", user.getUsername(), content);
				return true;
			}
			logger.error("zqtt-{}:微信提现失败,msg={}", user.getUsername(), content);
		} catch (Exception e) {
			logger.error("zqtt-{}:微信提现金币异常,msg={}", user.getUsername(), e.getMessage());
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
			logger.info("zqtt-{}:发送日志data={}", user.getUsername(), eventList.toJSONString());
			String x = TaottUtils.encodeData(eventList.toJSONString());
			int crc = x.hashCode();
			String url = "https://sc.baertt.com:8106/sa?project=production";
			String postData = "data_list=" + x + "&gzip=1&crc=" + crc;
			Map<String, String> heads = new HashMap<String, String>();
			heads.put("Accept-Encoding", "gzip");
			heads.put("User-Agent",
					"Dalvik/2.1.0 (Linux; U; Android " + user.getLogOsVersion() + "; " + user.getModel() + ")");
			heads.put("Content-Type", "application/x-www-form-urlencoded");
			int code = MobileHttpUrlConnectUtils.httpPostStatusCode(url, postData, heads, null);
			logger.info("zqtt-{}:发送日志结果|code={}", user.getUsername(), code);
			if (code == 200)
				return true;
		} catch (Exception e) {
			logger.error("zqtt-{}:发送手机日志异常,msg={}", user.getUsername(), e.getMessage());
		}
		return false;
	}

	public static void main(String[] args) {
		ZqttUser user = new ZqttUser();
		user.setUsername("17755117870");
		user.setUserId("39781740");
		ZqttHttp http = ZqttHttp.getInstance(user);
	}

}
