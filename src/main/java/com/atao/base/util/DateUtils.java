package com.atao.base.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateUtils {
	private static Map<String, SimpleDateFormat> FORMATTER_CACHE = new HashMap<String, SimpleDateFormat>(10);
	public static final String DATE_PATTERN = "yyyy-MM-dd";
	public static final String TIME_PATTERN = "HH:mm:ss";
	public static final String DATE_TIME_PATTERN = DATE_PATTERN + " " + TIME_PATTERN;

	private synchronized static SimpleDateFormat getFormatter(String pattern) {
		SimpleDateFormat formatter = FORMATTER_CACHE.get(pattern);
		if (formatter == null) {
			formatter = new SimpleDateFormat(pattern);
			FORMATTER_CACHE.put(pattern, formatter);
		}
		return formatter;
	}

	public static String formatDate(Date date, String pattern) {
		if (date == null)
			return null;
		if (pattern == null)
			pattern = DATE_TIME_PATTERN;
		return DateUtils.getFormatter(pattern).format(date);
	}

	public static Date parseDate(String strDate, String pattern) {
		if (strDate == null)
			return null;
		if (pattern == null)
			pattern = DATE_TIME_PATTERN;
		try {
			return DateUtils.getFormatter(pattern).parse(strDate);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 得到现在时间
	 * 
	 * @return currentDateTime
	 */
	public static Date getNow() {
		return new Date(System.currentTimeMillis());
	}

	/**
	 * 得到当前时间的年份
	 * 
	 * @return int year
	 */
	public static int getCurrentYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	public static int getYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.YEAR);
	}

	public static int getMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MONTH);
	}

	public static int getDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DATE);
	}

	public static int getHour(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.HOUR_OF_DAY);
	}

	public static int getMinute(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MINUTE);
	}

	public static int getSecond(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.SECOND);
	}

	public static boolean isWorkday(Date date) {
		return !DateUtils.isWeekend(date);
	}

	public static boolean isWeekend(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int day = c.get(Calendar.DAY_OF_WEEK);
		return (day == Calendar.SATURDAY || day == Calendar.SUNDAY);
	}

	/**
	 * 只检查年月日 date1 == date2
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean dateEqual(Date date1, Date date2) {
		Calendar c = Calendar.getInstance();
		c.setTime(date1);
		int y1 = c.get(Calendar.YEAR);
		int m1 = c.get(Calendar.MONTH);
		int d1 = c.get(Calendar.DATE);
		c.setTime(date2);
		int y2 = c.get(Calendar.YEAR);
		int m2 = c.get(Calendar.MONTH);
		int d2 = c.get(Calendar.DATE);
		return (y1 == y2 && m1 == m2 && d1 == d2);
	}

	/**
	 * 只检查年月日 date1 < date2
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean dateBefore(Date date1, Date date2) {
		Calendar c = Calendar.getInstance();
		c.setTime(date1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		Date d1 = c.getTime();
		c.setTime(date2);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		Date d2 = c.getTime();
		return d1.before(d2);
	}

	/**
	 * 只检查年月日 date1 <= date2
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean dateBeforeOrEqual(Date date1, Date date2) {
		return (DateUtils.dateBefore(date1, date2) || DateUtils.dateEqual(date1, date2));
	}

	/**
	 * 对日期+-天数
	 * 
	 * @param date
	 *            原日期
	 * @param day
	 *            天数(支持负数)
	 * @return 新日期
	 */
	public static Date addDay(Date date, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, day);
		return calendar.getTime();
	}

	public static Date add(Date date, int field, int howMany) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(field, howMany);
		return calendar.getTime();
	}

	/**
	 * 只检查时分秒 time1 < time2
	 * 
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static boolean timeBefore(Date time1, Date time2) {
		Calendar c = Calendar.getInstance();
		int sps = 0;
		int spe = 0;
		c.setTime(time1);
		sps += 3600 * c.get(Calendar.HOUR_OF_DAY);
		sps += 60 * c.get(Calendar.MINUTE);
		sps += c.get(Calendar.SECOND);
		c.setTime(time2);
		spe += 3600 * c.get(Calendar.HOUR_OF_DAY);
		spe += 60 * c.get(Calendar.MINUTE);
		spe += c.get(Calendar.SECOND);
		if (spe == 0)
			spe = 24 * 3600;
		return sps < spe;
	}

	/**
	 * 只检查时分秒 time1 == time2
	 * 
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static boolean timeEqual(Date time1, Date time2) {
		Calendar c = Calendar.getInstance();
		int sps = 0;
		int spe = 0;
		c.setTime(time1);
		sps += 3600 * c.get(Calendar.HOUR_OF_DAY);
		sps += 60 * c.get(Calendar.MINUTE);
		sps += c.get(Calendar.SECOND);
		c.setTime(time2);
		spe += 3600 * c.get(Calendar.HOUR_OF_DAY);
		spe += 60 * c.get(Calendar.MINUTE);
		spe += c.get(Calendar.SECOND);
		if (spe == 0)
			spe = 24 * 3600;
		return sps == spe;
	}

	/**
	 * 只检查时分秒 time1<=time2
	 * 
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static boolean timeBeforeOrEqual(Date time1, Date time2) {
		Calendar c = Calendar.getInstance();
		int sps = 0;
		int spe = 0;
		c.setTime(time1);
		sps += 3600 * c.get(Calendar.HOUR_OF_DAY);
		sps += 60 * c.get(Calendar.MINUTE);
		sps += c.get(Calendar.SECOND);
		c.setTime(time2);
		spe += 3600 * c.get(Calendar.HOUR_OF_DAY);
		spe += 60 * c.get(Calendar.MINUTE);
		spe += c.get(Calendar.SECOND);
		if (spe == 0)
			spe = 24 * 3600;
		return sps <= spe;
	}

	public static Date getDateEndTime(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		return c.getTime();
	}

	public static Date getDateStartTime(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

	public static Date getYesterday() {
		return DateUtils.addDay(DateUtils.getNow(), -1);
	}

	public static Date getTomorrow() {
		return DateUtils.addDay(DateUtils.getNow(), 1);
	}

}