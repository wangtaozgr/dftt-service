package com.atao.dftt.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.alibaba.fastjson.JSONObject;
import com.atao.dftt.model.Wltt;
import com.atao.dftt.util.wltt.AES;

public class WlttUtils {
	private static char[] a = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
			'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
			'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9', '+', '/' };
	private static byte[] b = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1,
			-1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
			10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30,
			31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1 };
	public static String verCode = "803";
	public static String verName = "1.3.1";
	public static String appKey = "91988061";
	public static Map<String, String> init(Wltt wltt) {// 13个初始化参数
		Map<String, String> map = new HashMap<String, String>();
		if (map != null) {
			if (!map.containsKey("app_key")) {
				map.put("app_key", wltt.getAppKey());
			}
					
			if (!map.containsKey("app_ts")) {
				map.put("app_ts", System.currentTimeMillis() + "");
			}

			if (!map.containsKey("uid")) {
				map.put("uid", wltt.getUid());
			}

			if (!map.containsKey("auth_token")) {
				map.put("auth_token", wltt.getAuthToken());
			}

			if (!map.containsKey("devid")) {
				map.put("devid", wltt.getDevid());
			}

			if (!map.containsKey("device_id")) {
				map.put("device_id", wltt.getDeviceId());
			}

			if (!map.containsKey("ver_code")) {
				map.put("ver_code", verCode);
			}

			if (!map.containsKey("ver_name")) {
				map.put("ver_name", verName);
			}

			if (!map.containsKey("channel")) {
				map.put("channel", wltt.getChannel());
			}

			if (!map.containsKey("city_key")) {
				map.put("city_key", wltt.getCityKey());
			}

			if (!map.containsKey("os_version")) {
				map.put("os_version", wltt.getOsVersion());
			}

			if (!map.containsKey("up")) {
				map.put("up", wltt.getUp());
			}

			if (!map.containsKey("device")) {
				map.put("device", wltt.getDevice());
			}
			
			if (!map.containsKey("lon")) {
				map.put("lon", wltt.getLon());
			}
			
			if (!map.containsKey("lat")) {
				map.put("lat", wltt.getLat());
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
			content.append("&" + entry01.getKey() + "=" + entry01.getValue());
		}
		if (content.length() > 0) {
			return content.toString().substring(1);
		} else {
			return "";
		}
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
			content.append(s).append(tree.get(s));
		}
		content.append("616c4ac6d6fd4eea986041a360f4e7b2");
		return WlttUtils.a(content.toString().getBytes());
	}

	public static String a(byte[] arg1) {
		return WlttUtils.c(WlttUtils.d(arg1));
	}

	private static byte[] d(byte[] arg2) {
		MessageDigest v1_1;
		byte[] v0 = null;
		try {
			v1_1 = MessageDigest.getInstance("MD5");
			if (v1_1 != null) {
				v1_1.update(arg2);
				v0 = v1_1.digest();
			}
		} catch (Exception v1) {
			v1.printStackTrace();
		}
		return v0;
	}

	private static String c(byte[] arg4) {
		String v0;
		if (arg4 == null) {
			v0 = null;
		} else {
			String v1 = "0123456789abcdef";
			StringBuilder v2 = new StringBuilder(arg4.length * 2);
			int v0_1;
			for (v0_1 = 0; v0_1 < arg4.length; ++v0_1) {
				v2.append(v1.charAt(arg4[v0_1] >> 4 & 15));
				v2.append(v1.charAt(arg4[v0_1] & 15));
			}

			v0 = v2.toString();
		}

		return v0;
	}

	public static String videoListSign(String arg2) {
		try {
			MessageDigest v0_3 = MessageDigest.getInstance("MD5");
			v0_3.update((arg2 + "f68f6c31421236bd6e5def875b5034bd").getBytes("UTF-8"));
			arg2 = b2(v0_3.digest());
		} catch (Exception v0) {
		}
		return arg2;
	}

	public static String videoSign(String arg2) {
		try {
			MessageDigest v0_3 = MessageDigest.getInstance("MD5");
			v0_3.update((arg2 + "321iklnklcnvanzpiorq90974hcnxnzbvouerqzajxczkljkldjaflija").getBytes("UTF-8"));
			arg2 = b2(v0_3.digest());
		} catch (Exception v0) {
		}
		return arg2;
	}

	private static String b2(byte[] arg6) {
		char[] c = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		StringBuilder v1 = new StringBuilder();
		int v2 = arg6.length;
		int v0;
		for (v0 = 0; v0 < v2; ++v0) {
			int v3 = arg6[v0];
			v1.append(c[(v3 & 240) >> 4]);
			v1.append(c[v3 & 15]);
		}

		return v1.toString();
	}

	public static String collectLog(String s) {
		try {
			WlttCipher c = new WlttCipher();
			s = c.sign(s);
			s = WlttZipUtil.a(s);
			return s;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static String decode(String s) {
		try {
			s = WlttZipUtil.decode(s);
			WlttCipher c = new WlttCipher();
			s = c.decode(s);
			return s;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String qiandaoEncode(String t) {
		String code = "POST&/wltask/api/coin/h5/checkin&" + t;
		String p_result = "";
		try {
			ScriptEngineManager sem = new ScriptEngineManager();
			ScriptEngine engine = sem.getEngineByName("js");
			Resource resource = new ClassPathResource("wltt.js");
			Reader fr = new InputStreamReader(resource.getInputStream());
			engine.eval(fr);
			Invocable inv = (Invocable) engine;
			p_result = inv.invokeFunction("getAppSign", code).toString();
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

	public static String getAuthToken(String device, String acctk) {
		JSONObject v2 = new JSONObject(true);
		v2.put("acctk", acctk);
		v2.put("up", "ANDROID");
		v2.put("device", device);
		return signAuthToken(v2.toJSONString().getBytes());
	}

	public static String signAuthToken(byte[] arg8) {
		StringBuffer v1 = new StringBuffer();
		int v2 = arg8.length;
		int v0 = 0;
		while (v0 < v2) {
			int v3 = v0 + 1;
			int v4 = arg8[v0] & 255;
			if (v3 == v2) {
				v1.append(a[v4 >>> 2]);
				v1.append(a[(v4 & 3) << 4]);
				v1.append("==");
			} else {
				int v5 = v3 + 1;
				v3 = arg8[v3] & 255;
				if (v5 == v2) {
					v1.append(a[v4 >>> 2]);
					v1.append(a[(v4 & 3) << 4 | (v3 & 240) >>> 4]);
					v1.append(a[(v3 & 15) << 2]);
					v1.append("=");
				} else {
					v0 = v5 + 1;
					v5 = arg8[v5] & 255;
					v1.append(a[v4 >>> 2]);
					v1.append(a[(v4 & 3) << 4 | (v3 & 240) >>> 4]);
					v1.append(a[(v3 & 15) << 2 | (v5 & 192) >>> 6]);
					v1.append(a[v5 & 63]);
					continue;
				}
			}
			break;
		}

		return v1.toString();
	}
	
	public static String getAdIv() {
		return AES.genHexIv();
	}

	public static String getAdData(Wltt wltt, String arg11) {
	        String v0;
	        try {
	            JSONObject v1_1 = new JSONObject(true);
	            v1_1.put("pid", wltt.getPid());
	            v1_1.put("ts", System.currentTimeMillis());
	            v1_1.put("debug", 0);
	            v1_1.put("version", "1.0.0");
	            v1_1.put("city_key", wltt.getCityKey());
	            JSONObject v6 = new JSONObject(true);
	            v6.put("app_key", WlttUtils.appKey);
	            v6.put("app_version", WlttUtils.verName);
	            v6.put("app_version_code", WlttUtils.verCode);
	            
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
	            v6.put("bundle", "cn.weli.story");
	            v6.put("channel", wltt.getChannel());
	            v1_1.put("app", v6);
	            JSONObject v5_1 = new JSONObject();
	            v5_1.put("os", "Android");
	            v5_1.put("osv", wltt.getOsv());
	            v5_1.put("carrier", 3);
	            v5_1.put("network", 2);
	            v5_1.put("resolution", "1080*1920");
	            v5_1.put("density", "3.0");
	            v5_1.put("open_udid", "");
	            v5_1.put("aid", wltt.getAndroidid());
	            v5_1.put("imei", wltt.getImei());
	            v5_1.put("imsi", wltt.getImsi());
	            v5_1.put("idfa", "");
	            v5_1.put("idfv", "");
	            v5_1.put("mac", wltt.getMac());
	            v5_1.put("aaid", "");
	            v5_1.put("duid", "");
	            v5_1.put("orientation", 0);
	            v5_1.put("vendor", wltt.getVendor());
	            v5_1.put("model", wltt.getModel());
	            v5_1.put("lan", "zh");
	            v5_1.put("ssid", "");
	            v5_1.put("root", 0);
	            v5_1.put("zone", "+008");
	            v5_1.put("nation", "CN");
	            //v5_1.put("sim_count", this.e());
	            //v5_1.put("dev_debug", this.f());
	            v1_1.put("device", v5_1);
	            JSONObject v2_1 = new JSONObject();
	            v2_1.put("lat", wltt.getLat());
	            v2_1.put("lon", wltt.getLon());
	            v1_1.put("geo", v2_1);
	            //v0 = EcalendarLib.getInstance().doTheAESEncrypt(v1_1.toString(), arg11, 2);
	        }
	        catch(Exception v1) {
	            v1.printStackTrace();
	        }
	        return "";
	    }
	 
	public static void main(String[] args) throws Exception {

		Map<String, String> map = new HashMap<String, String>();
		//device=MI5X99001065551084&city_key=101220101&lon=117.27743&app_ts=1545699941850&devid=d62dcb6f4445d853a54e0b87910543cf
		//&ver_code=793&app_sign=4f9d9de73c0119e9d225ab28e5af8ac3&auth_token=&app_key=91988061&device_id=0cf86601149fe34e9bfe5c3c759e5735&
		//ver_name=1.2.5&uid=2070633781476&os_version=71&lat=31.890524&channel=xiaomi&up=ANDROID
		//4f9d9de73c0119e9d225ab28e5af8ac3
		
		map.put("device", "MI5X99001065551084");
		map.put("city_key", "101220101");
		map.put("lon", "117.27743");
		map.put("app_ts", "1545699941850");
		map.put("devid", "d62dcb6f4445d853a54e0b87910543cf");
		map.put("ver_code", "793");
		map.put("auth_token", "eyJhY2N0ayI6IjAuNTc2OTc4MTQ3MTY3NjI3OTo3OHBpMGtqZmkuMTU0NTY5OTkyNDU2Ny5FVE9VQ0giLCJ1cCI6IkFORFJPSUQiLCJkZXZpY2UiOiJNSTVYOTkwMDEwNjU1NTEwODQifQ==");

		//map.put("auth_token", "eyJhY2N0ayI6IjAuNTc2OTc4MTQ3MTY3NjI3OTo3OHBpMGtqZmkuMTU0NTY5OTkyNDU2Ny5FVE9VQ0giLCJ1cCI6IkFORFJPSUQiLCJkZXZpY2UiOiJNSTVYOTkwMDEwNjU1NTEwODQifQ%3D%3D");
		map.put("device_id", "0cf86601149fe34e9bfe5c3c759e5735");
		map.put("app_key", "91988061");
		map.put("ver_name", "1.2.5");
		map.put("uid", "2070633781476");
		map.put("os_version", "71");
		map.put("lat", "31.890524");
		map.put("channel", "xiaomi");
		map.put("up", "ANDROID");
		//4f9d9de73c0119e9d225ab28e5af8ac3
		//map.put("product_id", "7");

		//String app_sign = WlttUtils.sign(map);
		System.out.println(getAdIv());

		// 7abe8fc69e6f07ad44c5e1e912bbbaae

		// String s = "GET&/wltask/api/coin/h5/checkin/info&1544066234809";
		/// wltask/api/coin/h5/checkin/info
		// 367194b84b2173eeffe54bc5240e4ba9
		// 367194b84b2173eeffe54bc5240e4ba9
		// s = jsEncode(s);
		// System.out.println(s);
		JSONObject v2 = new JSONObject(true);
		v2.put("acctk", "");
		v2.put("up", "ANDROID");
		v2.put("device", "CUN-TL00863199031688357");

		System.out.println(decode(""));
	}
	
	

}
