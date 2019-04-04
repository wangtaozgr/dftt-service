package com.atao.dftt.util;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.zip.GZIPOutputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atao.dftt.model.TaoToutiaoUser;
import com.atao.util.StringUtils;

public class TaottUtils {

	private static final char[] signChar01 = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b',
			'c', 'd', 'e', 'f' };
	private static final char[] signChar02 = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B',
			'C', 'D', 'E', 'F' };
	public static String lib_version = "1.8.18";
	public static String js_lib_version = "1.7.14";
	public static String app_version = "3.3.8.5";
	// public static String rwUrl =
	// "https://a.dazhongapp.com/share/xwz_article/new_article/article_newby5_dazhongapp_long.html";
	public static void main(String[] args) throws IOException {
		/*String p = "3.3.2.2" + "^" + "39781740" + "^" + "673bd95fa7727c8d0a87ff4cb0165932";
		byte[] b = AndroidBase64.decode("7WcNcg2P2YAruAO5Y1WoRw==".getBytes(), 8);
		String s = new String(AndroidBase64.encode(baseKey02(p.getBytes(), b), 10));
		System.out.println(s);*/
		System.out.println(new Date(1545296962000l));
		/*
		 * System.out.println(new Date(1543482022195l)); System.out.println(new
		 * Date(1543482022219l)); System.out.println(new Date(1543482022276l));
		 * System.out.println(sign("39781740","10608844_1",54));
		 */
		// String s =
		// "[{\"_track_id\":876004147,\"time\":1543801495046,\"type\":\"track\",\"properties\":{\"$device_id\":\"14b5f23968b97345\",\"$model\":\"MI
		// 5X\",\"$os_version\":\"7.1.2\",\"$app_version\":\"3.2.8.5\",\"$manufacturer\":\"Xiaomi\",\"$screen_height\":1920,\"$os\":\"Android\",\"$carrier\":\"中国电信\",\"$screen_width\":1080,\"$lib_version\":\"1.8.18\",\"$lib\":\"Android\",\"$wifi\":true,\"$network_type\":\"WIFI\",\"begin_time\":1543801147485,\"end_time\":1543801495044,\"minus\":347559,\"userId\":\"39781740\",\"channel\":\"shouji-guanwang\",\"imei\":\"99001065551084\",\"area\":0,\"is_anonymity\":0,\"pure\":0,\"is_charging\":1,\"battery_level\":77,\"is_simulator\":2,\"is_cmd\":1,\"$is_first_day\":true},\"distinct_id\":\"34b6f1ec-4f26-4415-bafd-04dbaebe0bfa\",\"lib\":{\"$lib\":\"Android\",\"$lib_version\":\"1.8.18\",\"$app_version\":\"3.2.8.5\",\"$lib_method\":\"code\",\"$lib_detail\":\"com.coohua.xinwenzhuan.helper.aw##a##SourceFile##47\"},\"event\":\"AppUse\",\"_flush_time\":1543801495067}]";
		// System.out.println(encodeData(s));
		/*
		 * String s = "3.2.8.5^39781740^f69f33bdfe08a3728a83baf7bd995968";
		 * TaoToutiaoUser user = new TaoToutiaoUser(); user.setUserId("39781740");
		 * user.setTicket("f69f33bdfe08a3728a83baf7bd995968");
		 * System.out.println(baseKey(user));
		 */
		// dIwhY1xbuImCEEVgGh_ez5QKE-mx0vqru-gbc-PbX7WMocuf_HterlPqXz-TBf8vtNbZ9oUuF3ScRwjV5cypsA==
		// /dIwhY1xbuImCEEVgGh_ez5QKE-mx0vqru-gbc-PbX7WMocuf_HterlPqXz-TBf8vtNbZ9oUuF3ScRwjV5cypsA%3D%3D
		// String s =
		// "{\"distinct_id\":\"1677c54d3fc4b-077cf67b1cd5e1-43480420-1049088-1677c54d3fd29b\",\"lib\":{\"$lib\":\"js\",\"$lib_method\":\"code\",\"$lib_version\":\"1.7.14\"},\"properties\":{\"$screen_height\":63,\"$screen_width\":309,\"$lib\":\"js\",\"$lib_version\":\"1.7.14\",\"$latest_referrer\":\"\",\"$latest_referrer_host\":\"\",\"ad_page\":\"news_detail\",\"ad_position\":1,\"ad_action\":\"exposure\",\"ad_id\":981932,\"ad_type\":\"2-19\",\"page_url\":\"https://a.dazhongapp.com/share/xwz_article/new_article/article_newby5_dazhongapp_long.html\",\"channel\":\"\",\"source\":\"4\",\"category\":0,\"origin\":\"recommend\",\"userId\":\"39781740\",\"ua\":\"mozilla/5.0
		// (linux; android 6.0; nexus 5 build/mra58n) applewebkit/537.36 (khtml, like
		// gecko) chrome/68.0.3440.106 mobile
		// safari/537.36\",\"$os\":\"android\",\"$device_id\":\"39781740\",\"$imei\":\"39781740\",\"$is_first_day\":true},\"type\":\"track\",\"event\":\"WebAdData\",\"_nocache\":\"012480832281202\"}";
		// "{"distinct_id":"1677c54d3fc4b-077cf67b1cd5e1-43480420-1049088-1677c54d3fd29b","lib":{"$lib":"js","$lib_method":"code","$lib_version":"1.7.14"},"properties":{"$screen_height":63,"$screen_width":309,"$lib":"js","$lib_version":"1.7.14","$latest_referrer":"","$latest_referrer_host":"","ad_page":"news_detail","ad_position":1,"ad_action":"exposure","ad_id":981932,"ad_type":"2-19","page_url":"https://a.dazhongapp.com/share/xwz_article/new_article/article_newby5_dazhongapp_long.html","channel":"","source":"4","category":0,"origin":"recommend","userId":"39781740","ua":"mozilla/5.0
		// (linux; android 6.0; nexus 5 build/mra58n) applewebkit/537.36 (khtml, like
		// gecko) chrome/68.0.3440.106 mobile
		// safari/537.36","$os":"android","$device_id":"39781740","$imei":"39781740","$is_first_day":true},"type":"track","event":"WebAdData","_nocache":"012480832281202"}"
		// eyJkaXN0aW5jdF9pZCI6IjE2NzdjNTRkM2ZjNGItMDc3Y2Y2N2IxY2Q1ZTEtNDM0ODA0MjAtMTA0OTA4OC0xNjc3YzU0ZDNmZDI5YiIsImxpYiI6eyIkbGliIjoianMiLCIkbGliX21ldGhvZCI6ImNvZGUiLCIkbGliX3ZlcnNpb24iOiIxLjcuMTQifSwicHJvcGVydGllcyI6eyIkc2NyZWVuX2hlaWdodCI6NjMsIiRzY3JlZW5fd2lkdGgiOjMwOSwiJGxpYiI6ImpzIiwiJGxpYl92ZXJzaW9uIjoiMS43LjE0IiwiJGxhdGVzdF9yZWZlcnJlciI6IiIsIiRsYXRlc3RfcmVmZXJyZXJfaG9zdCI6IiIsImFkX3BhZ2UiOiJuZXdzX2RldGFpbCIsImFkX3Bvc2l0aW9uIjoxLCJhZF9hY3Rpb24iOiJleHBvc3VyZSIsImFkX2lkIjo5ODE5MzIsImFkX3R5cGUiOiIyLTE5IiwicGFnZV91cmwiOiJodHRwczovL2EuZGF6aG9uZ2FwcC5jb20vc2hhcmUveHd6X2FydGljbGUvbmV3X2FydGljbGUvYXJ0aWNsZV9uZXdieTVfZGF6aG9uZ2FwcF9sb25nLmh0bWwiLCJjaGFubmVsIjoiIiwic291cmNlIjoiNCIsImNhdGVnb3J5IjowLCJvcmlnaW4iOiJyZWNvbW1lbmQiLCJ1c2VySWQiOiIzOTc4MTc0MCIsInVhIjoibW96aWxsYS81LjAgKGxpbnV4OyBhbmRyb2lkIDYuMDsgbmV4dXMgNSBidWlsZC9tcmE1OG4pIGFwcGxld2Via2l0LzUzNy4zNiAoa2h0bWwsIGxpa2UgZ2Vja28pIGNocm9tZS82OC4wLjM0NDAuMTA2IG1vYmlsZSBzYWZhcmkvNTM3LjM2IiwiJG9zIjoiYW5kcm9pZCIsIiRkZXZpY2VfaWQiOiIzOTc4MTc0MCIsIiRpbWVpIjoiMzk3ODE3NDAiLCIkaXNfZmlyc3RfZGF5Ijp0cnVlfSwidHlwZSI6InRyYWNrIiwiZXZlbnQiOiJXZWJBZERhdGEiLCJfbm9jYWNoZSI6IjAxMjQ4MDgzMjI4MTIwMiJ9"

		// s = jsEncode(s);
		// System.out.println(s);
		// (String(Math.random()) + String(Math.random()) +
		// String(Math.random())).replace(/\./g, "").slice(0, 15);
		//System.out.println(i1i1iI1I11iI("LyEQVGI="));
		//System.out.println(getErrorMsg());
		//System.out.println(c("eyJvcyI6IkFuZHJvaWQiLCJ2ZXJzaW9uIjoiMy4xLjgiLCJwYWNrYWdlcyI6ImNvbS5jb29odWEueGlud2Vuemh1YW4qJjMuMi44LjUiLCJlcnJvcl9pbml0Ijp7ImVycm9yX2NvZGUiOiJbXSIsImVycm9yX21zZyI6IltdNjNiZGZlNDRhODY5NGE0NTRjYTZhMjhlMDk0OWQxOWUiLCJkZXZpY2UiOiJ4aWFvbWleXk1JIDVYXl4yNV5eZTg5YjE1OGU0YmNmOTg4ZWJkMDllYjgzZjUzNzhlODdeXlthcm02NC12OGEsIGFybWVhYmktdjdhLCBhcm1lYWJpXV5eYXJtZWFiaSJ9fQ==%$%_17755117870_ch"));
	}

	public static String getStr(Map<String, String> map) {
		StringBuffer content = new StringBuffer();
		Iterator<Entry<String, String>> iterator01 = map.entrySet().iterator();
		Entry<String, String> entry01;
		while (iterator01.hasNext()) {
			entry01 = iterator01.next();
			content.append("&" + entry01.getKey() + "=" + entry01.getValue());
		}
		if (content.length() > 0) {
			return content.toString().substring(1);
		} else {
			return "";
		}
	}

	public static String sign(String userId, String newsId, int r) {
		return new String(sign03(sign01("RxUjygPDcWX6waedUYgwBtsqq6PoKKWK_" + userId + "_" + newsId + "_" + r), false));
	}

	private static byte[] sign01(String arg1) {
		return sign02(arg1.getBytes());
	}

	private static byte[] sign02(byte[] arg1) {
		byte[] v0_2;
		try {
			MessageDigest v0_1 = MessageDigest.getInstance("MD5");
			v0_1.update(arg1);
			v0_2 = v0_1.digest();
		} catch (NoSuchAlgorithmException v0) {
			v0.printStackTrace();
			v0_2 = null;
		}

		return v0_2;
	}

	private static char[] sign03(byte[] arg1, boolean arg2) {
		char[] v0 = arg2 ? signChar01 : signChar02;
		return sign04(arg1, v0);
	}

	private static char[] sign04(byte[] arg6, char[] arg7) {
		int v3 = arg6.length;
		char[] v4 = new char[v3 << 1];
		int v0 = 0;
		int v2;
		for (v2 = 0; v2 < v3; ++v2) {
			int v1 = v0 + 1;
			v4[v0] = arg7[(arg6[v2] & 240) >>> 4];
			v0 = v1 + 1;
			v4[v1] = arg7[arg6[v2] & 15];
		}

		return v4;
	}

	public static String baseKey(TaoToutiaoUser user) {
		try {
			String p = app_version + "^" + user.getUserId() + "^" + user.getTicket();
			byte[] b = AndroidBase64.decode("7WcNcg2P2YAruAO5Y1WoRw==".getBytes(), 8);
			String s = new String(AndroidBase64.encode(baseKey02(p.getBytes(), b), 10));
			return s;
		} catch (Exception e) {
		}
		return "";
	}

	public static byte[] baseKey02(byte[] arg2, byte[] arg3) {
		byte[] v0 = null;
		if (arg2 != null && arg3 != null) {
			try {
				v0 = baseKey03(arg2, arg3);
			} catch (Exception v1) {
				v1.printStackTrace();
			}
		}
		return v0;
	}

	public static byte[] baseKey03(byte[] arg1, byte[] arg2) throws Exception {
		return baseKey04(arg1, arg2, "AES/ECB/PKCS5Padding");
	}

	public static byte[] baseKey04(byte[] arg1, byte[] arg2, String arg3) throws Exception {
		return baseKey06(arg1, baseKey05(arg2), arg3);
	}

	public static Key baseKey05(byte[] arg2) {
		return new SecretKeySpec(arg2, "AES");
	}

	public static byte[] baseKey06(byte[] arg2, Key arg3, String arg4) throws Exception {
		Cipher v0 = Cipher.getInstance(arg4);
		v0.init(1, arg3);
		return v0.doFinal(arg2);
	}

	public static String encodeData(String arg4) {
		try {
			ByteArrayOutputStream v0 = new ByteArrayOutputStream(arg4.getBytes().length);
			GZIPOutputStream v1 = new GZIPOutputStream(((OutputStream) v0));
			v1.write(arg4.getBytes());
			v1.close();
			byte[] v1_1 = v0.toByteArray();
			v0.close();
			return new String(Base64Coder.encode(v1_1));
		} catch (Exception e) {
		}
		return null;

	}

	/**
	 * 主页面变化
	 * 
	 * @param user
	 * @param method
	 *            下拉 上拉 切换
	 * @param category
	 *            推荐 热文
	 * @return
	 */
	public static JSONObject AppNewsObtain(TaoToutiaoUser user, String method, String category) {
		JSONObject properties = initProperties(user).fluentPut("method", method).fluentPut("category", category);
		JSONObject object = common(user, "AppPageView", properties);
		return object;
	}

	/**
	 * 加载页面
	 * 
	 * @param user
	 * @param element_page
	 *            挖宝 新闻赚钱页 视频feed页
	 * @return
	 */
	public static JSONObject AppPageView(TaoToutiaoUser user, String element_page, String element_name) {
		JSONObject properties = initProperties(user).fluentPut("element_page", element_page);
		if (StringUtils.isNotBlank(element_name)) {
			properties.fluentPut("element_name", element_name);
		}
		JSONObject object = common(user, "AppPageView", properties);
		return object;
	}

	// 单击热文中的文章
	public static JSONObject appClickRw(TaoToutiaoUser user, String newsId) {
		JSONObject properties = initProperties(user);
		properties.fluentPut("element_page", "新闻赚钱页").fluentPut("element_name", "文章").fluentPut("category", "热文")
				.fluentPut("source", "4").fluentPut("article_id", newsId).fluentPut("pos", 10000)
				.fluentPut("has_redbag", false).fluentPut("left_read_count", -1);
		JSONObject object = common(user, "AppClick", properties);
		return object;
	}

	public static JSONObject appTaskMonitorRw(TaoToutiaoUser user) {
		JSONObject properties = initProperties(user).fluentPut("category", "文章").fluentPut("action", "article_click")
				.fluentPut("minus", 5 + new Random().nextInt(20)).fluentPut("source", "自家");
		JSONObject object = common(user, "AppTaskMonitor", properties);
		return object;
	}

	public static JSONObject readCreditRequest(TaoToutiaoUser user) {
		JSONObject properties = initProperties(user).fluentPut("element_page", "read_credit_request")
				.fluentPut("element_name", "request");
		JSONObject object = common(user, "AppClick", properties);
		return object;

	}

	public static JSONObject readCreditResponse(TaoToutiaoUser user) {
		JSONObject properties = initProperties(user).fluentPut("element_page", "read_credit_response")
				.fluentPut("element_name", "success");
		JSONObject object = common(user, "AppClick", properties);
		return object;
	}

	// 新闻详细页面
	public static JSONObject AppNewsView(TaoToutiaoUser user, String newsId, String page_url) {
		JSONObject properties = initProperties(user).fluentPut("element_page", "资讯明细页").fluentPut("source", "4")
				.fluentPut("article_id", newsId).fluentPut("article_time", new Random().nextInt(300))
				.fluentPut("origin", "feed").fluentPut("type", 2).fluentPut("page_url", page_url);
		JSONObject object = common(user, "AppNewsView", properties);
		return object;
	}

	// ApplicationResquest
	public static JSONObject ApplicationResquest(TaoToutiaoUser user, int type) {
		JSONObject properties = initProperties(user).fluentPut("type", type).fluentPut("article_time",
				type * 100 + new Random().nextInt(200));
		JSONObject object = common(user, "ApplicationResquest", properties);
		return object;
	}

	// ApplicationResquest
	public static JSONObject appStart(TaoToutiaoUser user) {
		JSONObject properties = initProperties(user).fluentPut("element_page", "start").fluentPut("element_name",
				"open");
		JSONObject object = common(user, "AppStart", properties);
		return object;

	}

	// app启动时用的
	public static JSONObject AppUsed(TaoToutiaoUser user) {
		JSONObject properties = initProperties(user).fluentPut("is_push", 0);
		JSONObject object = common(user, "AppUse", properties);
		return object;
	}

	public static JSONObject AppClick(TaoToutiaoUser user, String element_page, String element_name) {
		JSONObject properties = initProperties(user).fluentPut("element_page", element_page).fluentPut("element_name",
				element_name);
		JSONObject object = common(user, "AppClick", properties);
		return object;
	}

	public static JSONObject AdData(TaoToutiaoUser user, String adId) {
		JSONObject properties = initProperties(user).fluentPut("ad_action", "request").fluentPut("ad_id", adId)
				.fluentPut("ad_page", "splash").fluentPut("ad_type", "baidu").fluentPut("reason", "");
		JSONObject object = common(user, "AdData", properties);
		return object;
	}

	public static JSONArray AppStart(TaoToutiaoUser user) {
		JSONArray appStart = new JSONArray();
		try {
			JSONObject start = appStart(user);
			JSONObject applicationResquest1 = ApplicationResquest(user, 1);
			Thread.sleep(new Random().nextInt(300));
			JSONObject applicationResquest2 = ApplicationResquest(user, 2);
			Thread.sleep(new Random().nextInt(300));
			JSONObject adData1 = AdData(user, "5867985");
			Thread.sleep(new Random().nextInt(300));
			JSONObject adData2 = AdData(user, "5867985");
			Thread.sleep(new Random().nextInt(300));
			JSONObject applicationResquest3 = ApplicationResquest(user, 3);
			Thread.sleep(new Random().nextInt(300));
			JSONObject applicationResquest4 = ApplicationResquest(user, 4);
			Thread.sleep(new Random().nextInt(300));
			JSONObject index = AppClick(user, "首页", "新闻赚钱页");
			JSONObject indexPage = AppPageView(user, "新闻赚钱页", null);
			Thread.sleep(new Random().nextInt(300));
			JSONObject used = AppUsed(user);
			JSONObject noticeOpenProperties = initProperties(user).fluentPut("element_page", "通知开启")
					.fluentPut("element_name", "1");
			JSONObject noticeOpen = common(user, "AppUse", noticeOpenProperties);
			Thread.sleep(new Random().nextInt(300));
			JSONObject indexAppTaskMonitorPro = initProperties(user).fluentPut("category", "推荐")
					.fluentPut("minus", 300 + new Random().nextInt(300)).fluentPut("action", "startup")
					.fluentPut("status", 1);
			JSONObject indexAppTaskMonitor = common(user, "AppTaskMonitor", indexAppTaskMonitorPro);
			appStart.add(start);
			appStart.add(applicationResquest1);
			appStart.add(applicationResquest2);
			appStart.add(adData1);
			appStart.add(adData2);
			appStart.add(applicationResquest3);
			appStart.add(applicationResquest4);
			appStart.add(index);
			appStart.add(indexPage);
			appStart.add(used);
			appStart.add(noticeOpen);
			appStart.add(indexAppTaskMonitor);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return appStart;
	}

	public static JSONArray readNews(TaoToutiaoUser user, String newsId, String page_url) {
		JSONArray read = new JSONArray();
		try {
			JSONObject appClickRw = appClickRw(user, newsId);
			Thread.sleep(new Random().nextInt(500));
			JSONObject appTaskMonitorRw = appTaskMonitorRw(user);
			Thread.sleep(new Random().nextInt(200));
			JSONObject readCreditRequest = readCreditRequest(user);
			Thread.sleep(200 + new Random().nextInt(500));
			JSONObject readCreditResponse = readCreditResponse(user);
			Thread.sleep(new Random().nextInt(200));
			JSONObject wb = AppPageView(user, "挖宝", null);
			Thread.sleep(new Random().nextInt(200));
			JSONObject zq = AppPageView(user, "新闻赚钱页", null);
			Thread.sleep(new Random().nextInt(200));
			JSONObject newsDetail = AppNewsView(user, newsId, page_url);
			read.add(appClickRw);
			read.add(appTaskMonitorRw);
			read.add(readCreditRequest);
			read.add(readCreditResponse);
			read.add(wb);
			read.add(zq);
			read.add(newsDetail);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return read;

	}

	public static JSONArray qianDao(TaoToutiaoUser user) {
		JSONArray qianDao = new JSONArray();
		try {
			JSONObject task = AppClick(user, "首页", "任务大厅");
			Thread.sleep(5000 + new Random().nextInt(2000));
			JSONObject index = AppClick(user, "首页", "新闻赚钱页");
			Thread.sleep(new Random().nextInt(300));
			JSONObject view = AppPageView(user, "视频feed页", null);
			Thread.sleep(new Random().nextInt(1000));
			JSONObject rw = AppNewsObtain(user, "切换", "热文");
			qianDao.add(task);
			qianDao.add(index);
			qianDao.add(view);
			qianDao.add(rw);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return qianDao;

	}

	public static JSONObject common(TaoToutiaoUser user, String type, JSONObject properties) {
		JSONObject object = initHeader();
		properties.fluentPut("userId", user.getUserId()).fluentPut("channel", "shouji-guanwang")
				.fluentPut("imei", user.getImei()).fluentPut("area", 0).fluentPut("is_anonymity", 0)
				.fluentPut("pure", 0).fluentPut("is_charging", 1).fluentPut("battery_level", battery_level())
				.fluentPut("is_simulator", 2).fluentPut("is_cmd", 1).fluentPut("$is_first_day", user.getFirstDay());
		object.put("properties", properties);
		object.put("distinct_id", user.getDistinctId());
		object.put("lib", initLib(user));
		object.put("event", type);
		return object;
	}

	public static JSONObject initHeader() {
		JSONObject object = new JSONObject(true);
		object.put("_track_id", new Random().nextInt());
		object.put("time", System.currentTimeMillis());
		object.put("type", "track");
		return object;
	}

	public static JSONObject initProperties(TaoToutiaoUser user) {
		JSONObject properties = new JSONObject(true);//
		properties.fluentPut("$device_id", user.getDeviceId()).fluentPut("$model", user.getModel())
				.fluentPut("$os_version", user.getOsVersion()).fluentPut("$app_version", app_version)
				.fluentPut("$manufacturer", user.getVendor()).fluentPut("$screen_height", 1920)
				.fluentPut("$os", user.getOs()).fluentPut("$carrier", user.getCarrier())
				.fluentPut("$screen_width", 1080).fluentPut("$lib_version", lib_version).fluentPut("$lib", "Android")
				.fluentPut("$wifi", true).fluentPut("$network_type", "WIFI");
		return properties;

	}

	public static JSONObject initLib(TaoToutiaoUser user) {
		JSONObject lib = new JSONObject(true);
		lib.fluentPut("$lib", "Android").fluentPut("$lib_version", lib_version).fluentPut("$app_version", app_version)
				.fluentPut("$lib_method", "code")
				.fluentPut("$lib_detail", "com.coohua.xinwenzhuan.helper.aw##a##SourceFile##47");
		return lib;
	}

	public static int battery_level() {
		return 99;
	}

	/**
	 * 热文
	 * 
	 * @param user
	 * @param adId
	 * @param position
	 * @return
	 */
	public static JSONObject getJsObject(TaoToutiaoUser user, String adId, int position, String rwUrl) {
		JSONObject object = new JSONObject(true);
		object.put("distinct_id", user.getDistinctId());
		JSONObject lib = new JSONObject(true);
		lib.fluentPut("$lib", "js").fluentPut("$lib_method", "code").fluentPut("$lib_version", js_lib_version);
		object.put("lib", lib);
		JSONObject properties = new JSONObject(true);
		properties.fluentPut("$screen_height", 1920).fluentPut("$screen_width", 1080).fluentPut("$lib", "js")
				.fluentPut("$lib_version", js_lib_version).fluentPut("$latest_referrer", "")
				.fluentPut("$latest_referrer_host", "").fluentPut("ad_page", "news_detail")
				.fluentPut("ad_position", position).fluentPut("ad_action", "exposure").fluentPut("ad_id", adId)
				.fluentPut("ad_type", "2-19").fluentPut("page_url", rwUrl).fluentPut("channel", "shouji-guanwang")
				.fluentPut("source", "4").fluentPut("category", 0).fluentPut("origin", "recommend")
				.fluentPut("userId", user.getUserId()).fluentPut("ua", user.getUserAgent()).fluentPut("$os", "android")
				.fluentPut("$device_id", user.getDeviceId()).fluentPut("$imei", user.getImei())
				.fluentPut("$is_first_day", user.getFirstDay());
		object.put("properties", properties);
		object.put("type", "track");
		object.put("event", "WebAdData");
		String _nocache = (Math.random() + "" + Math.random() + "" + Math.random()).replace(".", "").substring(0, 15);
		object.put("_nocache", _nocache);
		return object;
	}

	/**
	 * 任务中心的
	 * 
	 * @param user
	 * @param adId
	 * @param position
	 * @return
	 */
	public static JSONObject taskJsObject(TaoToutiaoUser user, String element_page, String element_name,
			String jump_url) {
		JSONObject object = new JSONObject(true);
		object.put("distinct_id", user.getDistinctId());
		JSONObject lib = new JSONObject(true);
		lib.fluentPut("$lib", "js").fluentPut("$lib_method", "code").fluentPut("$lib_version", js_lib_version);
		object.put("lib", lib);
		JSONObject properties = new JSONObject(true);
		properties.fluentPut("$screen_height", 1920).fluentPut("$screen_width", 1080).fluentPut("$lib", "js")
				.fluentPut("$lib_version", js_lib_version).fluentPut("$latest_referrer", "")
				.fluentPut("$latest_referrer_host", "").fluentPut("element_page", element_name)
				.fluentPut("element_name", element_name)
				.fluentPut("page_url", "https://www.coohua.com/xinwenzhuan/weal_hall.html?userId=" + user.getUserId()
						+ "&ticket=" + user.getTicket() + "&env=production&version=" + TaottUtils.app_version
						+ "&baseKey=" + baseKey(user)
						+ "&channel=shouji-guanwang&region=0&anomy=0&push=true&download=0&u-key=6,6wlrb-_81_fXfy2RzaEuiC9DlUvQ0ry3_ZNzh525IAOi-u08YsVIygSeHiwgusKQOgBjmFf5twqlRaWar1hzFfaJpDi2kIMkprcPQCd_iNL1S9ELo8o66539M-EOwEVHfdJ_OwPl11Q8gjyLGU2cjfgYWUCtrRslvXbejKFh3kvplb1JQ3OeC1CLlT5OyYz51wMtMvOLplv6BRz2vumrQ==&income=0&suopingInstall=false&mobile="
						+ user.getUsername())
				.fluentPut("userId", user.getUserId()).fluentPut("type", 1).fluentPut("jump_url", jump_url)
				.fluentPut("is_business", 0).fluentPut("$area", 0).fluentPut("$is_anonymity", 0)
				.fluentPut("$app_version", TaottUtils.app_version).fluentPut("$os", "android")
				.fluentPut("$device_id", user.getDeviceId()).fluentPut("$imei", user.getImei())
				.fluentPut("$is_first_day", user.getFirstDay());
		object.put("properties", properties);
		object.put("type", "track");
		object.put("event", "WebClick");
		String _nocache = (Math.random() + "" + Math.random() + "" + Math.random()).replace(".", "").substring(0, 15);
		object.put("_nocache", _nocache);
		return object;
	}

	public static String jsEncode(String code) {
		String p_result = "";
		try {
			ScriptEngineManager sem = new ScriptEngineManager();
			ScriptEngine engine = sem.getEngineByName("js");
			Resource resource = new ClassPathResource("taottEncode.js");
			Reader fr = new InputStreamReader(resource.getInputStream());
			engine.eval(fr);
			Invocable inv = (Invocable) engine;
			p_result = inv.invokeFunction("base64Encode", code).toString();
		} catch (ScriptException e1) {
			e1.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p_result;
	}

	public static String linkxxxxx(String arg7, int arg8) {
		String v0_2;
		int v2;
		int v0 = 0;
		try {
			int v3 = arg7.length() / 2;
			char[] v1 = arg7.toCharArray();
			byte[] v4 = new byte[v3];
			while (v0 < v3) {
				v2 = v0 << 1;
				v4[v0] = ((byte) ("0123456789abcdef".indexOf(v1[v2 + 1]) | "0123456789abcdef".indexOf(v1[v2]) << 4));
				++v0;
			}

			byte v5 = ((byte) (arg8 ^ 115));
			v4[0] = ((byte) (v4[0] ^ 58));
			int v1_1 = v4[0];
			v0 = 1;
			while (v0 < v3) {
				v2 = v4[v0];
				v4[v0] = ((byte) (v1_1 ^ v4[v0] ^ v5));
				++v0;
				v1_1 = v2;
			}

			v0_2 = new String(v4, "utf-8");
		} catch (Exception v0_1) {
			v0_1.printStackTrace();
			v0_2 = null;
		}

		return v0_2;
	}

	public static String i1i1iI1I11iI(String arg7) {
		String v0_2;
		int v0 = 0;
		try {
			byte[] v1 = AndroidBase64.decode(arg7, 0);
			int v2 = v1.length;
			byte[] v3 = "ZUvyZJz".getBytes();
			int v4 = v3.length;
			while (v0 < v2) {
				v1[v0] = ((byte) (v1[v0] ^ v3[v0 % v4]));
				++v0;
			}

			v0_2 = new String(v1, "utf-8");
		} catch (Exception v0_1) {
			v0_2 = null;
		}

		return v0_2;
	}

	public static String c(String arg3) {
		String v0 = "";
		try {
			v0 = AndroidBase64.encodeToString(a(arg3.getBytes(),
					"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCFnqsHV+s0xKA6LeAvM2Q1fesA+4gUiuMk4KseWSSrdiL19Y495wSwcRaOVA6LcXh8fmYxwzVrN7hfN6zvYfstt/mi1rVs6eGNu+6Yk5ezWZra2QLxpmMHFcovjKzrFOwlUv7SwbkT7/nnS96cF/1Wq8bYCBwD/IyiCb0KfRdoqwIDAQAB"),
					10);
		} catch (Exception v1) {
			v1.printStackTrace();
		}
		return v0;
	}

	public static byte[] a(byte[] arg8, String arg9) throws Exception {
		byte[] v0_1;
		int v7 = 117;
		PublicKey v0 = KeyFactory.getInstance("RSA")
				.generatePublic(new X509EncodedKeySpec(AndroidBase64.decode(arg9, 0)));
		Cipher v4 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		v4.init(1, ((Key) v0));
		int v5 = arg8.length;
		ByteArrayOutputStream v6 = new ByteArrayOutputStream();
		int v1 = 0;
		int v3 = 0;
		while (v5 - v3 > 0) {
			v0_1 = v5 - v3 > v7 ? v4.doFinal(arg8, v3, v7) : v4.doFinal(arg8, v3, v5 - v3);
			v6.write(v0_1, 0, v0_1.length);
			int v0_2 = v1 + 1;
			v3 = v0_2 * 117;
			v1 = v0_2;
		}

		v0_1 = v6.toByteArray();
		v6.close();
		return v0_1;
	}
	
	public static String getErrorMsg() {
        String v0_2;
        int v0 = 0;
        try {
            byte[] v1 = AndroidBase64.decode("PxE=", 0);
            int v2 = v1.length;
            byte[] v3 = "dLCCU".getBytes();
            int v4 = v3.length;
            while(v0 < v2) {
                v1[v0] = ((byte)(v1[v0] ^ v3[v0 % v4]));
                ++v0;
            }

            v0_2 = new String(v1, "utf-8");
        }
        catch(Exception v0_1) {
            v0_2 = null;
        }

        return v0_2;
    }
}
