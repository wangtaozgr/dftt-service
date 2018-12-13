package com.atao.dftt.util;

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
		return java.net.URLEncoder.encode(s);
	}

	public static String decode(String s) {
		return java.net.URLDecoder.decode(s);
	}
}
