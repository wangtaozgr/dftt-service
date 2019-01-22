package com.atao.dftt.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atao.dftt.model.ZqttUser;
import com.atao.dftt.util.mayitt.PubKeySignature;
import com.atao.util.StringUtils;

public class ZqttUtils {
	public static String app_version = "1.3.8";
	public static String version_code = "24";
	public static String lib_version = "2.1.2";
	public static String subv = "1.2.2";
	public static Random random = new Random();

	public static String getSignTag() {
		return encrypt(PubKeySignature.publicKey).substring(8, 24);
	}

	public static Map<String, String> initHeader(ZqttUser user) {
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("Accept-Encoding", "gzip");
		heads.put("User-Agent", "okhttp/3.3.0");
		heads.put("access", "WIFI");
		heads.put("app-version", ZqttUtils.app_version);
		heads.put("carrier", CommonUtils.encode(user.getCarrier()));
		heads.put("device-model", CommonUtils.encode(user.getModel()));
		heads.put("device-platform", "android");
		heads.put("openudid", user.getOpenudid());
		heads.put("os-api", user.getOsApi());
		heads.put("os-version", user.getOsVersion());
		heads.put("phone-sim", "1");
		return heads;
	}

	public static Map<String, String> init(ZqttUser user) {// 17个初始化参数
		Map<String, String> map = new HashMap<String, String>();
		if (map != null) {
			if (!map.containsKey("sm_device_id")) {
				map.put("sm_device_id", user.getSmDeviceId());
			}

			if (!map.containsKey("device_id")) {
				map.put("device_id", user.getDeviceId());
			}

			if (!map.containsKey("access")) {
				map.put("access", "WIFI");
			}

			if (!map.containsKey("channel")) {
				map.put("channel", user.getChannel());
			}

			if (!map.containsKey("app_version")) {
				map.put("app_version", app_version);
			}

			if (!map.containsKey("version_code")) {
				map.put("version_code", version_code);
			}

			if (!map.containsKey("device_platform")) {
				map.put("device_platform", "android");
			}

			if (!map.containsKey("os_version")) {
				map.put("os_version", user.getOsVersion());
			}

			if (!map.containsKey("os_api")) {
				map.put("os_api", user.getOsApi());
			}

			if (!map.containsKey("device_model")) {
				map.put("device_model", CommonUtils.encode(user.getModel()));
			}
			if (!map.containsKey("request_time")) {
				map.put("request_time", System.currentTimeMillis() / 1000 + "");
			}

			if (!map.containsKey("openudid")) {
				map.put("openudid", user.getOpenudid());
			}

			if (!map.containsKey("phone_sim")) {
				map.put("phone_sim", "1");
			}

			if (!map.containsKey("carrier")) {
				map.put("carrier", CommonUtils.encode(user.getCarrier()));
			}

			if (!map.containsKey("uid")) {
				map.put("uid", user.getUserId());
			}

			if (!map.containsKey("zqkey_id")) {
				map.put("zqkey_id", user.getZqkeyId());
			}

			if (!map.containsKey("zqkey")) {
				map.put("zqkey", user.getZqkey());
			}
		}
		return map;
	}

	public static Map<String, String> initLogParam(ZqttUser user) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("os_api", user.getOsApi());
		map.put("device_type", "andorid");
		map.put("zqkey", user.getZqkey());
		map.put("sim", "1");
		map.put("carrier", CommonUtils.encode(user.getCarrier()));
		map.put("mc", user.getMac());
		map.put("version_code", ZqttUtils.version_code);
		map.put("zqkey_id", user.getZqkeyId());
		map.put("app_version", ZqttUtils.app_version);
		map.put("request_time", System.currentTimeMillis() / 1000 + "");
		map.put("openudid", user.getOpenudid());
		map.put("device_id", user.getDeviceId());
		map.put("device_model", CommonUtils.encode(user.getModel()));
		map.put("uid", user.getUserId());
		map.put("sm_device_id", user.getSmDeviceId());
		map.put("resolution", "1080.0x1920.0");
		map.put("subv", subv);
		map.put("os_version", user.getOsVersion());
		map.put("device_brand", user.getBrand());
		map.put("access", "WIFI");
		map.put("channel", user.getChannel());
		return map;
	}

	public static String getStr(Map<String, String> map) {
		StringBuffer content = new StringBuffer();
		Iterator<Entry<String, String>> iterator01 = map.entrySet().iterator();
		Entry<String, String> entry01;
		while (iterator01.hasNext()) {
			entry01 = iterator01.next();
			content.append("&" + entry01.getKey() + "=" + CommonUtils.encode(entry01.getValue()));
		}
		if (content.length() > 0) {
			return content.toString().substring(1);
		} else {
			return "";
		}
	}

	public static String getMultipartStr(Map<String, String> map, String boundary) {
		StringBuffer content = new StringBuffer();
		Iterator<Entry<String, String>> iterator01 = map.entrySet().iterator();
		Entry<String, String> entry01;
		while (iterator01.hasNext()) {
			entry01 = iterator01.next();
			content.append("--" + boundary + "\r\n");
			content.append("Content-Disposition: form-data; name=\"" + entry01.getKey() + "\"\r\n");
			int length = entry01.getValue().getBytes().length;
			content.append("Content-Length: " + length + "\r\n\r\n");
			content.append(entry01.getValue() + "\r\n");
		}
		content.append("--" + boundary + "\r\n");
		return content.toString();

	}

	public static String sign(Map<String, String> map) {
		TreeMap<String, String> tree = new TreeMap<String, String>();
		if (map != null) {
			Iterator<Entry<String, String>> iterator01 = map.entrySet().iterator();
			Entry<String, String> entry01;
			while (iterator01.hasNext()) {
				entry01 = iterator01.next();
				tree.put(entry01.getKey(), entry01.getValue());
			}
		}
		StringBuffer content = new StringBuffer();
		Iterator<String> iterator02 = tree.keySet().iterator();
		while (iterator02.hasNext()) {
			String s = iterator02.next();
			content.append(s).append("=").append(tree.get(s));
		}
		content.append("jdvylqchJZrfw0o2DgAbsmCGUapF1YChc");
		return ZqttUtils.encrypt(content.toString());
	}

	public static String token(Map<String, String> map) {
		TreeMap<String, String> tree = new TreeMap<String, String>();
		if (map != null) {
			Iterator<Entry<String, String>> iterator01 = map.entrySet().iterator();
			Entry<String, String> entry01;
			while (iterator01.hasNext()) {
				entry01 = iterator01.next();
				tree.put(entry01.getKey(), entry01.getValue());
			}
		}
		StringBuffer content = new StringBuffer();
		Iterator<String> iterator02 = tree.keySet().iterator();
		while (iterator02.hasNext()) {
			String s = iterator02.next();
			content.append(s).append("=").append(tree.get(s));
		}
		content.append("zWpfzystJLrfw7o3SgGlMmGGPupK2YLhB");
		return ZqttUtils.encrypt(content.toString());
	}

	public static String encrypt(String arg6) {
		int v2;
		StringBuilder v0;
		if (StringUtils.isEmpty(((CharSequence) arg6))) {
			return "";
		}
		try {
			byte[] v6_1 = MessageDigest.getInstance("MD5").digest(arg6.getBytes());
			v0 = new StringBuilder();
			int v1 = v6_1.length;
			v2 = 0;
			while (v2 < v1 + 1) {
				if (v2 >= v1) {
					return v0.toString();
				}
				String v3 = Integer.toHexString(v6_1[v2] & 255);
				if (v3.length() == 1) {
					v0.append("0" + v3);
				} else {
					v0.append(v3);
				}
				++v2;
			}
		} catch (NoSuchAlgorithmException v6) {
			throw new RuntimeException(((Throwable) v6));
		}
		return null;
	}

	public static JSONObject initHeader() {
		JSONObject object = new JSONObject(true);
		object.put("_track_id", new Random().nextInt());
		object.put("time", System.currentTimeMillis());
		object.put("type", "track");
		try {
			Thread.sleep(random.nextInt(100));
		} catch (InterruptedException e) {
		}
		return object;
	}

	public static JSONObject initProperties(ZqttUser user) {
		JSONObject properties = new JSONObject(true);//
		properties.fluentPut("$device_id", user.getOpenudid()).fluentPut("$model", user.getModel())
				.fluentPut("$os_version", user.getLogOsVersion()).fluentPut("$app_version", app_version)
				.fluentPut("$manufacturer", user.getVendor()).fluentPut("$screen_height", 1920)
				.fluentPut("$os", "Android").fluentPut("$carrier", user.getCarrier()).fluentPut("$screen_width", 1080)
				.fluentPut("$lib_version", lib_version).fluentPut("$lib", "Android").fluentPut("app_name", "中青看点")
				.fluentPut("platformType", "Android").fluentPut("isLogin", true).fluentPut("$wifi", true)
				.fluentPut("$network_type", "WIFI");
		return properties;

	}

	public static JSONObject initLib(ZqttUser user) {
		JSONObject lib = new JSONObject(true);
		lib.fluentPut("$lib", "Android").fluentPut("$lib_version", lib_version).fluentPut("$app_version", app_version)
				.fluentPut("$lib_method", "code");
		return lib;
	}

	public static JSONObject common(ZqttUser user, String type, JSONObject properties, JSONObject lib) {
		JSONObject object = initHeader();
		object.put("properties", properties);
		object.put("distinct_id", user.getOpenudid());
		object.put("lib", lib);
		object.put("event", type);
		return object;
	}

	public static JSONObject appStart(ZqttUser user, String screen_name, String lib_detail) {
		JSONObject properties = initProperties(user).fluentPut("$resume_from_background", true)
				.fluentPut("$is_first_time", false).fluentPut("$screen_name", screen_name).fluentPut("$title", "中青看点")
				.fluentPut("$is_first_day", user.getFirstDay());
		JSONObject lib = initLib(user).fluentPut("$lib_detail", lib_detail);
		JSONObject AppViewScreen = common(user, "$AppStart", properties, lib);
		return AppViewScreen;
	}

	public static JSONObject appEnd(ZqttUser user) {
		JSONObject properties = initProperties(user).fluentPut("$resume_from_background", true)
				.fluentPut("$is_first_time", false)
				.fluentPut("$screen_name", "com.weishang.wxrd.activity.WebViewActivity").fluentPut("$title", "中青看点")
				.fluentPut("$is_first_day", user.getFirstDay());
		JSONObject lib = initLib(user).fluentPut("$lib_detail", "com.weishang.wxrd.activity.WebViewActivity######");
		JSONObject AppViewScreen = common(user, "$AppEnd", properties, lib);
		return AppViewScreen;
	}

	public static JSONObject sp_ad(ZqttUser user, String lib_detail) {
		JSONObject properties = initProperties(user).fluentPut("source", "FLY").fluentPut("$is_first_day",
				user.getFirstDay());
		JSONObject lib = initLib(user).fluentPut("$lib_detail", lib_detail);
		JSONObject splashLoad = common(user, "sp_ad", properties, lib);
		return splashLoad;
	}

	public static JSONObject list_ad(ZqttUser user, String lib_detail, String source) {
		JSONObject properties = initProperties(user).fluentPut("source", source).fluentPut("$is_first_day",
				user.getFirstDay());
		JSONObject lib = initLib(user).fluentPut("$lib_detail", lib_detail);
		JSONObject splashLoad = common(user, "list_ad", properties, lib);
		return splashLoad;
	}

	public static JSONObject adShow(ZqttUser user, String lib_detail, String adPosition, String adType, String adTitle,
			String adSource, String adResourceID) {
		JSONObject properties = initProperties(user).fluentPut("adPosition", adPosition).fluentPut("adType", adType)
				.fluentPut("adTitle", adTitle).fluentPut("adSource", adSource).fluentPut("adResourceID", adResourceID)
				.fluentPut("$is_first_day", user.getFirstDay());
		JSONObject lib = initLib(user).fluentPut("$lib_detail", lib_detail);
		JSONObject adShow = common(user, "adShow", properties, lib);
		return adShow;
	}

	public static JSONObject AppViewScreen(ZqttUser user, String screen_name, String lib_detail) {
		JSONObject properties = initProperties(user).fluentPut("$screen_name", screen_name).fluentPut("$title", "中青看点")
				.fluentPut("$is_first_day", user.getFirstDay());
		JSONObject lib = initLib(user).fluentPut("$lib_detail", lib_detail);
		JSONObject AppViewScreen = common(user, "$AppViewScreen", properties, lib);
		return AppViewScreen;
	}

	public static JSONObject clickChannel(ZqttUser user, String lib_detail, String belongTab, int channelIndex,
			int channelID, String channelName) {
		JSONObject properties = initProperties(user).fluentPut("belongTab", belongTab)
				.fluentPut("channelIndex", channelIndex).fluentPut("channelID", channelID)
				.fluentPut("channelName", channelName).fluentPut("$is_first_day", user.getFirstDay());
		JSONObject lib = initLib(user).fluentPut("$lib_detail", lib_detail);
		JSONObject AppViewScreen = common(user, "clickChannel", properties, lib);
		return AppViewScreen;
	}

	public static JSONObject AppClick(ZqttUser user, String screen_name, String lib_detail, String element_id,
			String element_content, String element_type) {
		JSONObject properties = initProperties(user);
		if (StringUtils.isNotBlank(element_id)) {
			properties.fluentPut("$element_id", element_id);
		}
		properties.fluentPut("$screen_name", screen_name).fluentPut("$title", "中青看点")
				.fluentPut("$element_content", element_content).fluentPut("$element_type", element_type)
				.fluentPut("$is_first_day", user.getFirstDay());
		JSONObject lib = initLib(user).fluentPut("$lib_detail", lib_detail);
		JSONObject AppClick = common(user, "$AppClick", properties, lib);
		return AppClick;
	}

	public static JSONObject viewContentDetail(ZqttUser user, String event, String lib_detail, String contentType,
			String contentID, String contentTitle, String contentChannel, String exposureFrom) {
		JSONObject properties = initProperties(user).fluentPut("contentType", contentType)
				.fluentPut("contentID", contentID).fluentPut("contentTitle", contentTitle)
				.fluentPut("contentChannel", contentChannel).fluentPut("exposureFrom", exposureFrom)
				.fluentPut("$is_first_day", user.getFirstDay());
		JSONObject lib = initLib(user).fluentPut("$lib_detail", lib_detail);
		JSONObject viewContentDetail = common(user, event, properties, lib);
		return viewContentDetail;
	}

	public static JSONObject enterTab(ZqttUser user, String lib_detail, String tabName) {
		JSONObject properties = initProperties(user).fluentPut("tabName", tabName).fluentPut("$is_first_day",
				user.getFirstDay());
		JSONObject lib = initLib(user).fluentPut("$lib_detail", lib_detail);
		JSONObject enterTab = common(user, "enterTab", properties, lib);
		return enterTab;
	}

	public static JSONObject ApiAdState(ZqttUser user, Boolean isSuccess, String adViewTemplate, String brand,
			String ad_event, String positionId) {
		JSONObject properties = initProperties(user);
		if (isSuccess != null)
			properties = properties.fluentPut("isSuccess", isSuccess);
		properties = properties.fluentPut("adViewTemplate", adViewTemplate).fluentPut("brand", brand)
				.fluentPut("ad_event", ad_event);
		if (StringUtils.isNotBlank(positionId)) {
			properties = properties.fluentPut("positionId", positionId);
		}
		properties = properties.fluentPut("$is_first_day", user.getFirstDay());
		JSONObject lib = initLib(user).fluentPut("$lib_detail",
				"com.sensorsdata.analytics.android.sdk.SensorsDataAPI$10##run##SensorsDataAPI.java##2098");
		JSONObject AppViewScreen = common(user, "ApiAdState", properties, lib);
		return AppViewScreen;
	}

	public static JSONArray AppStart(ZqttUser user) {
		try {
			JSONArray appStart = new JSONArray();
			JSONObject start = appStart(user, "com.weishang.wxrd.activity.MainActivity",
					"com.weishang.wxrd.activity.MainActivity######");
			appStart.add(start);
			Thread.sleep(random.nextInt(300));
			JSONObject appViewScreen01 = AppViewScreen(user, "com.weishang.wxrd.activity.WebViewActivity",
					"com.weishang.wxrd.activity.WebViewActivity######");
			appStart.add(appViewScreen01);
			JSONObject sp_ad = sp_ad(user,
					"com.sensorsdata.analytics.android.sdk.SensorsDataAPI$10##run##SensorsDataAPI.java##2098");
			appStart.add(sp_ad);
			JSONObject list_ad01 = list_ad(user,
					"com.sensorsdata.analytics.android.sdk.SensorsDataAPI$10##run##SensorsDataAPI.java##2098", "GDT");
			appStart.add(list_ad01);
			JSONObject list_ad02 = list_ad(user,
					"com.sensorsdata.analytics.android.sdk.SensorsDataAPI$10##run##SensorsDataAPI.java##2098", "BAIDU");
			appStart.add(list_ad02);
			JSONObject list_ad03 = list_ad(user,
					"com.sensorsdata.analytics.android.sdk.SensorsDataAPI$10##run##SensorsDataAPI.java##2098",
					"TOUTIAO");
			appStart.add(list_ad03);
			JSONObject clickChannel = clickChannel(user,
					"com.sensorsdata.analytics.android.sdk.SensorsDataAPI$10##run##SensorsDataAPI.java##2098", "首页", 0,
					0, "推荐");
			appStart.add(clickChannel);
			JSONObject appViewScreen03 = AppViewScreen(user, "com.weishang.wxrd.activity.MainActivity",
					"com.weishang.wxrd.activity.MainActivity######");
			appStart.add(appViewScreen03);
			JSONObject appViewScreen04 = AppViewScreen(user, "com.bytedance.sdk.openadsdk.activity.TTDelegateActivity",
					"com.bytedance.sdk.openadsdk.activity.TTDelegateActivity######");
			appStart.add(appViewScreen04);

			JSONObject appViewScreen05 = AppViewScreen(user, "com.weishang.wxrd.activity.MainActivity",
					"com.weishang.wxrd.activity.MainActivity######");
			appStart.add(appViewScreen05);
			return appStart;
		} catch (Exception e) {
		}
		return null;
	}

	public static JSONArray readNews(ZqttUser user, String newsId, String title, String account_name, String read_num) {
		JSONArray readNews = new JSONArray();
		JSONObject AppClick = AppClick(user, "com.weishang.wxrd.activity.MainActivity",
				"com.weishang.wxrd.activity.MainActivity######", "lr_more_article",
				title + "-" + account_name + "-" + read_num + "阅读-刚刚", "com.weishang.wxrd.widget.LabelRelativeLayout");
		readNews.add(AppClick);
		JSONObject appViewScreen01 = AppViewScreen(user, "com.weishang.wxrd.activity.WebViewActivity",
				"com.weishang.wxrd.activity.WebViewActivity######");
		readNews.add(appViewScreen01);
		return readNews;
	}


	public static String getUuid() {
		String boundary = UUID.randomUUID().toString();
		return boundary.replace("-", "");
	}

	public static void main(String[] args) {
		/*
		 * Map<String, String> map = new HashMap<String, String>();
		 * //article_id=6834766&read_type=0&channel=c1001&device_type=2&device_id=
		 * 2064239&
		 * //sm_device_id=20181224211015c3dd055d19d7fe76a487e2520bfe9bb6017d6f4d715feba1
		 * &access=WIFI& //version_code=535&request_time=1545979466&uid=6161529&sign=
		 * a12e949953814df3998b6347efce6ef5 map.put("article_id", "6834766");
		 * map.put("read_type", "0"); map.put("channel", "c1001");
		 * map.put("device_type", "2"); map.put("device_id", "2064239");
		 * map.put("sm_device_id",
		 * "20181224211015c3dd055d19d7fe76a487e2520bfe9bb6017d6f4d715feba1");
		 * map.put("access", "WIFI"); map.put("version_code", "535");
		 * map.put("request_time", "1545979466"); map.put("uid", "6161529");
		 * System.out.println(sign(map));
		 */
		String key = "access=WIFIapp-version=1.3.8app_version=1.3.8carrier=中国电信channel=c1015device_brand=xiaomidevice_id=17857827device_model=MI 5Xdevice_type=androidmc=02:00:00:00:00:00openudid=392fa75cb0a20b2aos_api=25os_version=QL1515-tiffany-build-20180608213248request_time=1548054011resolution=1080.0x1920.0sim=1sm_device_id=2018121123135548c814d2fb71fcd294f7b44cce19f992012ff88e87f5d810subv=1.2.2uid=18317504version_code=24zqkey=MDAwMDAwMDAwMJCMpN-w09Wtg5-Bb36eh6CPqHualIej3rB1saqx3YmwhXyp4LDPyGl9onqkj3ZqYJa8Y898najWsJupY7Gnm7CFjIbdrqnIapqGcXYzqkey_id=db38c282bb3e1a3355bf03245ecf6db9zWpfzystJLrfw7o3SgGlMmGGPupK2YLhB";
		String s = encrypt(key);
		System.out.println(s);
		// 0f1e1d19ca0141795593dcf683043afd
		// 0f1e1d19ca0141795593dcf683043afd

	}
}
