package com.atao.dftt.util;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class CommonUtils {
	
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

	public static String encode(String s) {
		try {
			return java.net.URLEncoder.encode(s, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String decode(String s) {
		try {
			return java.net.URLDecoder.decode(s, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
