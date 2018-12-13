package com.atao.base.util;

public class StringUtils {

	public static final String SPACE = " ";

	public static final String EMPTY = "";

	public static boolean isBlank(final CharSequence cs) {
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(cs.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static boolean isNotBlank(final CharSequence cs) {
		return !isBlank(cs);
	}

	public static boolean isEmpty(final CharSequence cs) {
		return cs == null || cs.length() == 0;
	}

	public static boolean isNotEmpty(final CharSequence cs) {
		return !isEmpty(cs);
	}

	/**
	 * 大写加下划线命名，转为驼峰式命名
	 * 
	 * @param str
	 * @return
	 */
	public static String capitalizeAll(String str) {
		if (null == str || str.isEmpty()) {
			return str;
		}
		StringBuilder s = new StringBuilder(str.length());
		boolean isUpper = true;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if ('_' == c) {
				isUpper = true;
				continue;
			}
			if (isUpper) {
				s.append(Character.toUpperCase(c));
				isUpper = false;
			} else {
				s.append(Character.toLowerCase(c));
			}
		}
		return s.toString();
	}

	/**
	 * 首字母，小写转大写
	 * 
	 * @param str
	 * @return
	 */
	public static String uncapitalize(final String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}

		char firstChar = str.charAt(0);
		if (Character.isLowerCase(firstChar)) {
			// already uncapitalized
			return str;
		}

		return new StringBuilder(strLen).append(Character.toLowerCase(firstChar)).append(str.substring(1)).toString();
	}

	public static String toString(final Object obj) {
		return obj == null ? StringUtils.EMPTY : obj.toString();
	}

	public static String toString(final Object obj, final String nullStr) {
		return obj == null ? nullStr : obj.toString();
	}
}
