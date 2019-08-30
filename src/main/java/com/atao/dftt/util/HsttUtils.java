package com.atao.dftt.util;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Random;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atao.base.util.StringUtils;
import com.atao.dftt.model.HsttUser;
import com.atao.dftt.model.JkdttUser;
import com.atao.dftt.util.hstt.AES;
import com.atao.dftt.util.hstt.MobUtils;

public class HsttUtils {
	public static Random random = new Random();

	public static int appversioncode = 1221;
	public static String appversion = "1.2.2.1";
	public static String appkey = "25e07d9e5b85a";

	public static void main(String[] args) throws IOException {
		JkdttUser user = new JkdttUser();
		user.setOpenId("a84b215f392644b282a31aab020be1c3");
		user.setX("EfSVvMxVcNwqoE7agij");
		System.out.println(getSensor());
	}

	public static String getRandomNumber(int num) {
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < num; i++) {
			s.append(random.nextInt(9) + 1);
		}
		return s.toString();
	}

	public static String getSensor() {
		JSONObject sensor = new JSONObject(true);
		JSONObject Accelerometer = new JSONObject(true);
		String x = "0." + getRandomNumber(8);
		String y = "-0." + getRandomNumber(7);
		String z = "9." + getRandomNumber(5);
		Accelerometer.fluentPut("x", x);
		Accelerometer.fluentPut("y", y);
		Accelerometer.fluentPut("z", z);
		JSONObject Gyroscope = new JSONObject(true);
		x = "-1." + getRandomNumber(7) + "E-4";
		y = "-3." + getRandomNumber(7) + "E-4";
		z = "9." + getRandomNumber(7) + "E-4";
		Gyroscope.fluentPut("x", x);
		Gyroscope.fluentPut("y", y);
		Gyroscope.fluentPut("z", z);
		sensor.fluentPut("Accelerometer", Accelerometer);
		sensor.fluentPut("Gyroscope", Gyroscope);
		return sensor.toJSONString();
	}

	public static String decode(String content) {
		return AES.c(content);
	}

	public static String getToken(String arg6) {
		if (StringUtils.isBlank(((CharSequence) arg6))) {
			return "";
		}
		try {
			byte[] v6_1 = MessageDigest.getInstance("MD5").digest(arg6.getBytes());
			StringBuffer v0 = new StringBuffer(100);
			int v1 = v6_1.length;
			int v2;
			for (v2 = 0; v2 < v1; ++v2) {
				String v3 = Integer.toHexString(v6_1[v2] & 255);
				if (v3.length() == 1) {
					v3 = "0" + v3;
				}

				v0.append(v3);
			}
			return v0.toString();
		} catch (Exception v6) {
			v6.printStackTrace();
			return "";
		}
	}

	/**
	 * mob 日志头部信息
	 * 
	 * @param user
	 * @return
	 */
	public static String getUserIdentity(HsttUser user) {
		return "APP/com.xcm.huasheng;" + HsttUtils.appversion + " SYS/Android;" + user.getSdk() + " SDI/"
				+ user.getDevice() + " FM/" + user.getVendor() + ";" + CommonUtils.encode(user.getModel())
				+ " NE/wifi;46011 Lang/zh_CN CLV/20181128 SDK/SHARESDK;3020101 DC/2";
	}

	public static String mobEncode(String s) {
		try {
			return MobUtils.encode(s);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getMobRunM(HsttUser user) {
		String m = "[RUN]:" + System.currentTimeMillis() + "|" + user.getDevice() + "|" + HsttUtils.appkey
				+ "|com.xcm.huasheng|15|3020101|1|wifi|" + user.getMobKey();
		return CommonUtils.encode(m);
	}

	public static String getMobExtM(HsttUser user, long time) {
		String m = "[EXT]:" + System.currentTimeMillis() + "|" + user.getDevice() + "|" + HsttUtils.appkey
				+ "|com.xcm.huasheng|15|3020101|1|wifi|" + user.getMobKey() + "|" + time;
		return CommonUtils.encode(m);
	}

	public static JSONObject initMobLog(HsttUser user, JSONArray datas) {
		JSONObject mob = new JSONObject(true);
		mob.fluentPut("networktype", "wifi").fluentPut("device", user.getDevice()).fluentPut("datas", datas)
				.fluentPut("imei", user.getImei()).fluentPut("mac", user.getMac().toLowerCase())
				.fluentPut("serialno", user.getSerialno()).fluentPut("plat", 1).fluentPut("duid", user.getDuid())
				.fluentPut("model", user.getModel());
		return mob;
	}

	public static JSONArray startApp(int runtimes) {
		JSONArray data = new JSONArray();
		JSONObject backInfo = backInfo(runtimes);
		JSONObject bsInfo = bsInfo();
		JSONObject pv = pv();
		data.add(backInfo);
		try {
			Thread.sleep(new Random().nextInt(1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		data.add(bsInfo);
		try {
			Thread.sleep(new Random().nextInt(500));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		data.add(pv);
		return data;
	}

	public static JSONArray wifiScan() {
		JSONArray data = new JSONArray();
		JSONObject wifiScanList = wifiScanList();
		data.add(wifiScanList);
		return data;
	}

	private static JSONObject wifiScanList() {
		JSONObject wifiScanList = new JSONObject(true);
		JSONArray list = JSONArray.parseArray(
				"[{\"SSID\":\"TP-LINK_huihe\",\"___curConn\":true,\"level\":-34,\"frequency\":2412,\"BSSID\":\"78:a1:06:b8:e0:02\"},{\"level\":-53,\"frequency\":2412,\"SSID\":\"ChinaNet-KaiG\",\"BSSID\":\"04:c1:b9:70:0c:47\"},{\"level\":-56,\"frequency\":2412,\"SSID\":\"FAST_C8B812\",\"BSSID\":\"c0:61:18:c8:b8:12\"},{\"level\":-61,\"frequency\":2437,\"SSID\":\"ChinaNet-L7HA\",\"BSSID\":\"04:c1:b9:72:9f:a3\"},{\"level\":-72,\"frequency\":2462,\"SSID\":\"LZGL\",\"BSSID\":\"b0:95:8e:60:c6:0d\"},{\"level\":-66,\"frequency\":2437,\"SSID\":\"ChinaNet-d9VG\",\"BSSID\":\"b8:c7:16:1a:b6:54\"},{\"level\":-87,\"frequency\":2437,\"SSID\":\"ChinaNet-KSUX\",\"BSSID\":\"b8:c7:16:1a:6d:4d\"},{\"level\":-75,\"frequency\":2412,\"SSID\":\"ChinaNet-CBAa\",\"BSSID\":\"04:c1:b9:6e:67:b9\"}]");
		wifiScanList.fluentPut("list", list).fluentPut("datetime", System.currentTimeMillis() + "")
				.fluentPut("type", "WIFI_SCAN_LIST").fluentPut("appkey", HsttUtils.appkey)
				.fluentPut("appver", HsttUtils.appversion).fluentPut("apppkg", "com.xcm.huasheng");
		return wifiScanList;
	}

	private static JSONObject backInfo(int runtimes) {
		JSONObject wifiScanList = new JSONObject(true);
		JSONObject data = new JSONObject(true);
		data.fluentPut("runtimes", runtimes);
		wifiScanList.fluentPut("datetime", System.currentTimeMillis() + "").fluentPut("type", "BACK_INFO")
				.fluentPut("appkey", HsttUtils.appkey).fluentPut("appver", HsttUtils.appversion)
				.fluentPut("apppkg", "com.xcm.huasheng").fluentPut("data", data);
		return wifiScanList;
	}

	private static JSONObject bsInfo() {
		JSONObject bsInfo = new JSONObject(true);
		JSONObject data = new JSONObject(true);
		data.fluentPut("nid", 10).fluentPut("sid", 14151).fluentPut("simopname", "中国电信").fluentPut("bid", 2147483647)
				.fluentPut("carrier", 46011);
		bsInfo.fluentPut("datetime", System.currentTimeMillis() + "").fluentPut("type", "BSINFO")
				.fluentPut("appkey", HsttUtils.appkey).fluentPut("appver", HsttUtils.appversion)
				.fluentPut("apppkg", "com.xcm.huasheng").fluentPut("data", data);
		return bsInfo;
	}

	private static JSONObject pv() {
		JSONObject pv = new JSONObject(true);
		pv.fluentPut("datetime", System.currentTimeMillis() + "").fluentPut("type", "PV")
				.fluentPut("appkey", HsttUtils.appkey).fluentPut("appver", HsttUtils.appversion)
				.fluentPut("apppkg", "com.xcm.huasheng");
		return pv;
	}
}
