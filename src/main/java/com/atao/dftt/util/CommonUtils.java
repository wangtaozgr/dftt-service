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
	
	public static String filter(String str) {
		StringBuffer out = new StringBuffer();
		if (str == null || ("".equals(str)))
			return "";
		char[] chars = str.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if ((chars[i] >= 19968 && chars[i] <= 40869) // 中日朝兼容形式的unicode编码范围： U+4E00——U+9FA5
					|| (chars[i] >= 11904 && chars[i] <= 42191)// 中日朝兼容形式扩展
					|| (chars[i] >= 63744 && chars[i] <= 64255)// 中日朝兼容形式扩展
					|| (chars[i] >= 65072 && chars[i] <= 65103)// 中日朝兼容形式扩展
					|| (chars[i] >= 65280 && chars[i] <= 65519)// 全角ASCII、全角中英文标点、半宽片假名、半宽平假名、半宽韩文字母的unicode编码范围：U+FF00——U+FFEF
					|| (chars[i] >= 32 && chars[i] <= 126)// 半角字符的unicode编码范围：U+0020-U+007e
					|| (chars[i] >= 12289 && chars[i] <= 12319)// 全角字符的unicode编码范围：U+3000——U+301F
			) {
				out.append(chars[i]);
			}
		}
		String result = out.toString().trim();
		result = result.replaceAll("\\?", "").replaceAll("\\*", "").replaceAll("<|>", "").replaceAll("\\|", "")
				.replaceAll("/", "");
		return result;
	}
}
