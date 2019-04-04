package com.atao.dftt.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atao.dftt.model.MayittUser;
import com.atao.dftt.model.MayittUser;
import com.atao.dftt.model.Wltt;
import com.atao.dftt.util.mayitt.PubKeySignature;
import com.atao.util.StringUtils;

public class MayittUtils {
	public static String app_version = "5.3.6.2";
	public static String version_code = "536";
	public static String lib_version = "2.1.0";
	public static Random random = new Random();
	public static String[] adtitles = new String[] { "百鬼夜行抄", "终于可以在手机上玩这游戏了", "无限制自由刷本", "各种道具靠打", "近视做激光手术",
			"做完激光手术眼还会近视吗", "尿尿刺痛怎么办", "失之毫厘", "出出律律", "尼卡伊塞", "徐传统", "移船就岸", "搠笔巡街", "呆", "山梨醇酐三硬脂酸酯", "威尼斯", "皮特",
			"箪食瓢浆", "比拉马运动", "握拳透掌", "说千说万", "维尼斯", "基希舍夫", "代你发梦", "广式月饼", "像梦一样自由", "黑糖放电", "什袭珍藏", "潘杨之睦", "悬车告老",
			"风驰云卷", "汪永益", "意前笔后", "金鱼鸭掌", "底盘前悬高度", "放心", "麻圆", "荐贤举能", "明鉴高悬", "惠然之顾", "端砚品式", "风尘之警", "心如刀锉",
			"不敢把回忆去细细的看", "莎呦娜啦", "人字梯", "今宵久久", "约翰奥比米克尔", "激扬清浊", "时来铁似金", "积玉堆金", "椎锋陷陈", "魂消魄夺", "路帝", "勇敢说不",
			"鳞叶番杏属", "红当当飞吻", "一代儒宗", "一代儿", "一代国色", "一代女皇", "一代宗匠", "一代宗工", "跟我来", "鲁莉娅", "铁扒仔鸡", "心中的故乡", "一年景",
			"一床两好", "一底一面", "一座尽倾", "一座尽惊", "一建", "梁山伯与茱丽叶", "山荆子", "真味珍", "三色堇", "一泓清水", "一泡子", "一波未平﹐一波又起", "一注",
			"一泻汪洋", "一泼滩", "如果说离开", "最后的要求", "摇摆女郎", "豹皮花属", "一言立信", "一言而喻", "一言而定", "一言订交", "一言诗", "一言赖语", "让我罩着你",
			"让爱在灿烂中死去", "心路", "隔断", "丁庄村委会", "丁庄街道", "丁庄镇", "丁店", "丁店镇", "丁庸", "三角装", "知心一个", "我曾经爱过一个女孩", "栗子鸡", "七姐妹",
			"七姑子", "七娘寨村委会", "七娘石", "七子", "七子八壻", "在遥远的地方", "希望的种子", "小麦赤霉病", "七卒", "七苏木村", "七莘路", "七莘路七号桥", "七莘路五号桥",
			"七莘路十号桥", "七菱八落", "五轮仪", "川味牛肉", "加一些想象", "情人的眼泪" };

	public static String[] adSources = new String[] { "搜狗联盟", "百度联盟", "头条联盟" };
	public static String[] adTypes = new String[] { "大图", "三图", "单图" };

	public static String getAdtitle() {
		int len = adtitles.length;
		return adtitles[random.nextInt(len)];
	}

	public static String getAdSource() {
		int len = adtitles.length;
		return adtitles[random.nextInt(len)];
	}

	public static String getAdType() {
		int len = adtitles.length;
		return adtitles[random.nextInt(len)];
	}

	public static String getSignTag() {
		return encrypt(PubKeySignature.publicKey).substring(8, 24);
	}

	public static Map<String, String> initHeader(MayittUser user) {
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("Accept-Encoding", "gzip");
		heads.put("User-Agent", "okhttp/3.9.1");
		heads.put("access", "WIFI");
		heads.put("app-version", MayittUtils.app_version);
		heads.put("carrier", CommonUtils.encode(user.getCarrier()));
		heads.put("device-model", CommonUtils.encode(user.getModel()));
		heads.put("device-platform", "android");
		heads.put("iid", "0");
		heads.put("openudid", user.getOpenudid());
		heads.put("os-api", user.getOsApi());
		heads.put("os-version", user.getOsVersion());
		heads.put("phone-sim", "1");
		heads.put("sign-tag", MayittUtils.getSignTag());
		heads.put("sm_device_id", user.getSmDeviceId());
		heads.put("uuid", user.getImei());
		return heads;
	}

	public static Map<String, String> init(MayittUser user) {// 16个初始化参数
		Map<String, String> map = new HashMap<String, String>();
		if (map != null) {
			if (!map.containsKey("iid")) {
				map.put("iid", "0");
			}

			if (!map.containsKey("request_time")) {
				map.put("request_time", System.currentTimeMillis() / 1000 + "");
			}

			if (!map.containsKey("device_id")) {
				map.put("device_id", user.getDeviceId());
			}

			if (!map.containsKey("sm_device_id")) {
				map.put("sm_device_id", user.getSmDeviceId());
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

			if (!map.containsKey("uuid")) {
				map.put("uuid", user.getImei());
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
		}
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
		return MayittUtils.encrypt(content.toString());
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

	public static JSONObject initProperties(MayittUser user) {
		JSONObject properties = new JSONObject(true);//
		properties.fluentPut("$device_id", user.getOpenudid()).fluentPut("$model", user.getModel())
				.fluentPut("$os_version", user.getLogOsVersion()).fluentPut("$app_version", app_version)
				.fluentPut("$manufacturer", user.getVendor()).fluentPut("$screen_height", 1920)
				.fluentPut("$os", "Android").fluentPut("$carrier", user.getCarrier()).fluentPut("$screen_width", 1080)
				.fluentPut("$lib_version", lib_version).fluentPut("$lib", "Android").fluentPut("app_name", "蚂蚁头条")
				.fluentPut("platformType", "Android").fluentPut("isLogin", true).fluentPut("uid", user.getUserId())
				.fluentPut("hasSdCardPermission", true).fluentPut("hasReadPhoneStatePermission", true)
				.fluentPut("hasFineLocationPermission", false).fluentPut("$wifi", true)
				.fluentPut("$network_type", "WIFI");
		return properties;

	}

	public static JSONObject initLib(MayittUser user) {
		JSONObject lib = new JSONObject(true);
		lib.fluentPut("$lib", "Android").fluentPut("$lib_version", lib_version).fluentPut("$app_version", app_version)
				.fluentPut("$lib_method", "code");
		return lib;
	}

	public static JSONObject common(MayittUser user, String type, JSONObject properties, JSONObject lib) {
		JSONObject object = initHeader();
		object.put("properties", properties);
		object.put("distinct_id", user.getUserId());
		object.put("lib", lib);
		object.put("event", type);
		return object;
	}

	public static JSONObject appStart(MayittUser user, String screen_name, String lib_detail) {
		JSONObject properties = initProperties(user).fluentPut("$resume_from_background", true)
				.fluentPut("$is_first_time", false).fluentPut("$screen_name", screen_name).fluentPut("$title", "蚂蚁头条")
				.fluentPut("$is_first_day", user.getFirstDay());
		JSONObject lib = initLib(user).fluentPut("$lib_detail", lib_detail);
		JSONObject AppViewScreen = common(user, "$AppStart", properties, lib);
		return AppViewScreen;
	}

	public static JSONObject splashLoad(MayittUser user, String lib_detail, String adPosition, String adType,
			String adTitle, String adSource, String adResourceID, boolean skip) {
		JSONObject properties = initProperties(user).fluentPut("adPosition", adPosition).fluentPut("adType", adType)
				.fluentPut("adTitle", adTitle).fluentPut("adSource", adSource).fluentPut("adResourceID", adResourceID)
				.fluentPut("skip", skip).fluentPut("$is_first_day", user.getFirstDay());
		JSONObject lib = initLib(user).fluentPut("$lib_detail", lib_detail);
		JSONObject splashLoad = common(user, "splashLoad", properties, lib);
		return splashLoad;
	}

	public static JSONObject adShow(MayittUser user) {
		JSONObject adShow = adShow(user,
				"com.sensorsdata.analytics.android.sdk.SensorsDataAPI$10##run##SensorsDataAPI.java##1979", "文章feed流",
				getAdType(), getAdtitle(), getAdSource(), "");

		return adShow;
	}

	public static JSONObject adShow(MayittUser user, String lib_detail, String adPosition, String adType,
			String adTitle, String adSource, String adResourceID) {
		JSONObject properties = initProperties(user).fluentPut("adPosition", adPosition).fluentPut("adType", adType)
				.fluentPut("adTitle", adTitle).fluentPut("adSource", adSource).fluentPut("adResourceID", adResourceID)
				.fluentPut("$is_first_day", user.getFirstDay());
		JSONObject lib = initLib(user).fluentPut("$lib_detail", lib_detail);
		JSONObject adShow = common(user, "adShow", properties, lib);
		return adShow;
	}

	public static JSONObject AppViewScreen(MayittUser user, String screen_name, String lib_detail) {
		JSONObject properties = initProperties(user).fluentPut("$screen_name", screen_name).fluentPut("$title", "蚂蚁头条")
				.fluentPut("$is_first_day", user.getFirstDay());
		JSONObject lib = initLib(user).fluentPut("$lib_detail", lib_detail);
		JSONObject AppViewScreen = common(user, "$AppViewScreen", properties, lib);
		return AppViewScreen;
	}

	public static JSONObject clickChannel(MayittUser user, String lib_detail, String belongTab, int channelIndex,
			int channelID, String channelName) {
		JSONObject properties = initProperties(user).fluentPut("belongTab", belongTab)
				.fluentPut("channelIndex", channelIndex).fluentPut("channelID", channelID)
				.fluentPut("channelName", channelName).fluentPut("$is_first_day", user.getFirstDay());
		JSONObject lib = initLib(user).fluentPut("$lib_detail", lib_detail);
		JSONObject AppViewScreen = common(user, "clickChannel", properties, lib);
		return AppViewScreen;
	}

	public static JSONObject AppClick(MayittUser user, String screen_name, String lib_detail, String element_id,
			String element_content, String element_type) {
		JSONObject properties = initProperties(user).fluentPut("$element_id", element_id)
				.fluentPut("$screen_name", screen_name).fluentPut("$title", "蚂蚁头条")
				.fluentPut("$element_content", element_content).fluentPut("$element_type", element_type)
				.fluentPut("$is_first_day", user.getFirstDay());
		JSONObject lib = initLib(user).fluentPut("$lib_detail", lib_detail);
		JSONObject AppClick = common(user, "$AppClick", properties, lib);
		return AppClick;
	}

	public static JSONObject viewContentDetail(MayittUser user, String event, String lib_detail, String contentType,
			String contentID, String contentTitle, String contentChannel, String exposureFrom) {
		JSONObject properties = initProperties(user).fluentPut("contentType", contentType)
				.fluentPut("contentID", contentID).fluentPut("contentTitle", contentTitle)
				.fluentPut("contentChannel", contentChannel).fluentPut("exposureFrom", exposureFrom)
				.fluentPut("$is_first_day", user.getFirstDay());
		JSONObject lib = initLib(user).fluentPut("$lib_detail", lib_detail);
		JSONObject viewContentDetail = common(user, event, properties, lib);
		return viewContentDetail;
	}

	public static JSONObject enterTab(MayittUser user, String lib_detail, String tabName) {
		JSONObject properties = initProperties(user).fluentPut("tabName", tabName).fluentPut("$is_first_day",
				user.getFirstDay());
		JSONObject lib = initLib(user).fluentPut("$lib_detail", lib_detail);
		JSONObject enterTab = common(user, "enterTab", properties, lib);
		return enterTab;
	}

	public static JSONObject ApiAdState(MayittUser user, Boolean isSuccess, String adViewTemplate, String brand,
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
				"com.sensorsdata.analytics.android.sdk.SensorsDataAPI$10##run##SensorsDataAPI.java##1979");
		JSONObject AppViewScreen = common(user, "ApiAdState", properties, lib);
		return AppViewScreen;
	}

	public static JSONArray AppStart(MayittUser user) {
		JSONArray appStart = new JSONArray();
		JSONObject start = AppViewScreen(user, "com.weishang.wxrd.activity.MainActivity",
				"com.weishang.wxrd.activity.MainActivity######");
		appStart.add(start);
		JSONObject splashLoad = splashLoad(user,
				"com.sensorsdata.analytics.android.sdk.SensorsDataAPI$10##run##SensorsDataAPI.java##1979", "5908892",
				"开屏", "开屏", "百度", "5908892", false);
		appStart.add(splashLoad);
		JSONObject appViewScreen01 = AppViewScreen(user, "com.weishang.wxrd.activity.SplashActivity",
				"com.weishang.wxrd.activity.SplashActivity######");
		appStart.add(appViewScreen01);
		JSONObject appViewScreen02 = AppViewScreen(user,
				"com.weishang.wxrd.activity.SplashActivity|com.weishang.wxrd.ui.SplashFragment",
				"com.weishang.wxrd.activity.SplashActivity######");
		appStart.add(appViewScreen02);
		JSONObject adShow = adShow(user,
				"com.sensorsdata.analytics.android.sdk.SensorsDataAPI$10##run##SensorsDataAPI.java##1979", "5908892",
				"开屏", "百度开屏", "百度", "5908892");
		appStart.add(adShow);
		JSONObject clickChannel = clickChannel(user,
				"com.sensorsdata.analytics.android.sdk.SensorsDataAPI$10##run##SensorsDataAPI.java##1979", "首页", 0, 0,
				"推荐");
		appStart.add(clickChannel);
		JSONObject appViewScreen03 = AppViewScreen(user, "com.weishang.wxrd.activity.MainActivity",
				"com.weishang.wxrd.activity.MainActivity######");
		appStart.add(appViewScreen03);
		JSONObject appViewScreen04 = AppViewScreen(user,
				"com.weishang.wxrd.activity.MainActivity|com.weishang.wxrd.ui.MainFragment",
				"com.weishang.wxrd.activity.MainActivity######");
		appStart.add(appViewScreen04);
		JSONObject appViewScreen05 = AppViewScreen(user,
				"com.weishang.wxrd.activity.MainActivity|com.weishang.wxrd.ui.HomeFragment",
				"com.weishang.wxrd.activity.MainActivity######");
		appStart.add(appViewScreen05);
		JSONObject adShow02 = adShow(user,
				"com.sensorsdata.analytics.android.sdk.SensorsDataAPI$10##run##SensorsDataAPI.java##1979", "文章feed流",
				getAdType(), getAdtitle(), getAdSource(), "");
		appStart.add(adShow02);
		appStart.add(appViewScreen03);
		appStart.add(appViewScreen04);
		appStart.add(appViewScreen05);
		JSONObject appViewScreen06 = AppViewScreen(user,
				"com.weishang.wxrd.activity.MainActivity|com.weishang.wxrd.ui.HomeListFragment",
				"com.weishang.wxrd.activity.MainActivity######");
		appStart.add(appViewScreen06);
		return appStart;
	}

	public static JSONArray readNews(MayittUser user, String newsId, String title, String account_name,
			String read_num) {
		JSONArray readNews = new JSONArray();
		JSONObject AppClick = AppClick(user, "com.weishang.wxrd.activity.MainActivity",
				"com.weishang.wxrd.activity.MainActivity######", "lr_article_item",
				title + "-" + account_name + "-" + read_num + "阅读-刚刚", "com.weishang.wxrd.widget.LabelRelativeLayout");
		readNews.add(AppClick);
		JSONObject viewContentDetail = viewContentDetail(user, "viewContentDetail",
				"com.sensorsdata.analytics.android.sdk.SensorsDataAPI$10##run##SensorsDataAPI.java##1979", "文章", newsId,
				title, account_name, "feed流");
		readNews.add(viewContentDetail);
		JSONObject appViewScreen01 = AppViewScreen(user, "com.weishang.wxrd.activity.WebViewActivity",
				"com.weishang.wxrd.activity.WebViewActivity######");
		readNews.add(appViewScreen01);
		JSONObject appViewScreen02 = AppViewScreen(user,
				"com.weishang.wxrd.activity.WebViewActivity|com.weishang.wxrd.ui.ArticleDetailFragment",
				"com.weishang.wxrd.activity.WebViewActivity######");
		readNews.add(appViewScreen02);
		JSONObject appViewScreen06 = AppViewScreen(user, "com.weishang.wxrd.activity.MainActivity",
				"com.weishang.wxrd.activity.MainActivity######");
		readNews.add(appViewScreen06);
		JSONObject appViewScreen07 = AppViewScreen(user,
				"com.weishang.wxrd.activity.MainActivity|com.weishang.wxrd.ui.MainFragment",
				"com.weishang.wxrd.activity.MainActivity######");
		readNews.add(appViewScreen07);
		JSONObject appViewScreen08 = AppViewScreen(user,
				"com.weishang.wxrd.activity.MainActivity|com.weishang.wxrd.ui.HomeFragment",
				"com.weishang.wxrd.activity.MainActivity######");
		readNews.add(appViewScreen08);
		JSONObject appViewScreen09 = AppViewScreen(user,
				"com.weishang.wxrd.activity.MainActivity|com.weishang.wxrd.ui.HomeListFragment",
				"com.weishang.wxrd.activity.MainActivity######");
		readNews.add(appViewScreen09);
		JSONObject exitContentDetail = viewContentDetail(user, "exitContentDetail",
				"com.sensorsdata.analytics.android.sdk.SensorsDataAPI$10##run##SensorsDataAPI.java##1979", "文章", newsId,
				title, account_name, "feed流");
		readNews.add(exitContentDetail);
		JSONObject adShow = adShow(user,
				"com.sensorsdata.analytics.android.sdk.SensorsDataAPI$10##run##SensorsDataAPI.java##1979", "文章feed流",
				getAdType(), getAdtitle(), getAdSource(), "");
		readNews.add(adShow);
		return readNews;
	}

	public static JSONArray readVideo(MayittUser user, String newsId, String title, String account_name,
			String read_num, String video_time) {
		JSONArray readNews = new JSONArray();
		JSONObject AppClick = AppClick(user, "com.weishang.wxrd.activity.MainActivity",
				"com.weishang.wxrd.activity.MainActivity######", "ll_article_list_video",
				video_time + "-" + title + "-" + read_num + "次播放", "com.weishang.wxrd.widget.LabelLinearLayout");
		readNews.add(AppClick);
		JSONObject viewContentDetail = viewContentDetail(user, "viewContentDetail",
				"com.sensorsdata.analytics.android.sdk.SensorsDataAPI$10##run##SensorsDataAPI.java##1979", "视频", newsId,
				title, account_name, "feed流");
		readNews.add(viewContentDetail);
		JSONObject appViewScreen01 = AppViewScreen(user, "com.weishang.wxrd.activity.WebViewActivity",
				"com.weishang.wxrd.activity.WebViewActivity######");
		readNews.add(appViewScreen01);
		JSONObject appViewScreen02 = AppViewScreen(user,
				"com.weishang.wxrd.activity.WebViewActivity|com.weishang.wxrd.ui.ArticleDetailFragment",
				"com.weishang.wxrd.activity.WebViewActivity######");
		readNews.add(appViewScreen02);
		JSONObject appViewScreen06 = AppViewScreen(user, "com.weishang.wxrd.activity.MainActivity",
				"com.weishang.wxrd.activity.MainActivity######");
		readNews.add(appViewScreen06);
		JSONObject appViewScreen07 = AppViewScreen(user,
				"com.weishang.wxrd.activity.MainActivity|com.weishang.wxrd.ui.MainFragment",
				"com.weishang.wxrd.activity.MainActivity######");
		readNews.add(appViewScreen07);
		JSONObject appViewScreen08 = AppViewScreen(user,
				"com.weishang.wxrd.activity.MainActivity|com.weishang.wxrd.ui.HomeFragment",
				"com.weishang.wxrd.activity.MainActivity######");
		readNews.add(appViewScreen08);
		JSONObject appViewScreen09 = AppViewScreen(user,
				"com.weishang.wxrd.activity.MainActivity|com.weishang.wxrd.ui.HomeListFragment",
				"com.weishang.wxrd.activity.MainActivity######");
		readNews.add(appViewScreen09);
		JSONObject exitContentDetail = viewContentDetail(user, "exitContentDetail",
				"com.sensorsdata.analytics.android.sdk.SensorsDataAPI$10##run##SensorsDataAPI.java##1979", "视频", newsId,
				title, account_name, "feed流");
		readNews.add(exitContentDetail);
		JSONObject adShow = adShow(user,
				"com.sensorsdata.analytics.android.sdk.SensorsDataAPI$10##run##SensorsDataAPI.java##1979", "文章feed流",
				getAdType(), getAdtitle(), getAdSource(), "");
		readNews.add(adShow);
		return readNews;
	}

	public static JSONArray readAdTask(MayittUser user, JSONObject adData) {
		JSONArray readAdTask = new JSONArray();
		JSONObject appViewScreen01 = MayittUtils.AppViewScreen(user,
				"com.weishang.wxrd.activity.MoreActivity|com.weishang.wxrd.ui.LookAd2Fragment",
				"com.weishang.wxrd.activity.MoreActivity######");
		readAdTask.add(appViewScreen01);
		JSONArray ad_positions = adData.getJSONArray("ad_positions");
		JSONArray dialogAdPosition = adData.getJSONArray("dialogAdPosition");
		for (int i = 0; i < ad_positions.size(); i++) {
			JSONObject ad_position = ad_positions.getJSONObject(i);
			JSONObject apiAdState = MayittUtils.ApiAdState(user, null, "0", ad_position.getString("channel"),
					"adApiShow", null);
			readAdTask.add(apiAdState);
			JSONObject apiAdState02 = MayittUtils.ApiAdState(user, true, "0", ad_position.getString("channel"),
					"adApiShow", ad_position.getString("positionId"));
			readAdTask.add(apiAdState02);
		}
		if(dialogAdPosition!=null&&dialogAdPosition.size()>0) {
			JSONObject dialogAd = dialogAdPosition.getJSONObject(random.nextInt(dialogAdPosition.size()));
			JSONObject apiAdState03 = MayittUtils.ApiAdState(user, true, "1", dialogAd.getString("channel"), "adApiRequest",
					dialogAd.getString("positionId"));
			readAdTask.add(apiAdState03);
		}
		return readAdTask;
	}
	
	public static String getUuid() {
		String boundary = UUID.randomUUID().toString();
		return boundary.replace("-","");
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
		System.out.println(getUuid());
		System.out.println(getSignTag());
		String key = "2KEJ0tWKbKWqr2aSAOASljXdSqqBhQIDAQAB";
		String s = encrypt(key).substring(8, 24);
		System.out.println(s);

	}
}
