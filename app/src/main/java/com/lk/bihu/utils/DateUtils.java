package com.lk.bihu.utils;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Pattern;

public class DateUtils {

	public static Date StringToDate(String s) {
		java.util.Date d = new java.util.Date();

		Date date;

		if (s == null)
			return null;

		d = new java.util.Date(s);

		date = new Date(d.getTime());

		return date;
	}

	public static Date StringToDate1(String time) {
		java.util.Date d = new java.util.Date();
		Date date;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			d = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		date = new Date(d.getTime());

		return date;
	}
	public static Date StringToDate2(String time) {
		java.util.Date d = new java.util.Date();
		Date date;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			d = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		date = new Date(d.getTime());

		return date;
	}

	public static int getSysYear() {
		java.util.Date d = new java.util.Date();

		Date date = new Date(d.getTime());

		return date.getYear();
	}

	public static int getSysMonth() {
		java.util.Date d = new java.util.Date();

		Date date = new Date(d.getTime());

		return date.getMonth();
	}

	public static int getSysDay() {
		java.util.Date d = new java.util.Date();

		Date date = new Date(d.getTime());

		return date.getDay();
	}

	public static String getSysDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new java.util.Date());
	}

	public static String getSysTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new java.util.Date());
	}
	/**得到20170713格式的系统日期*/
	public static String getSysTime2() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(new java.util.Date());
	}
	/**得到一天的前一天，传入和返回格式都是20170713*/
	public static String getBeforeDay(String day){
		java.util.Date date = StringToDate2(day);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		date = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(date);
	}
	/**
	 * 取得当前时间的日期字符串
	 * 
	 * @return
	 */
	public static String getDateNow() {
		Calendar cal = Calendar.getInstance();
		String retStr = "";
		retStr = cal.get(cal.YEAR) + "-" + (cal.get(cal.MONTH) + 1) + "-"
				+ cal.get(cal.DAY_OF_MONTH);
		return retStr;
	}

	public static String getDateSerial() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSSSSS");
		return sdf.format(new java.util.Date());
	}

	public static String getDateShortSerial() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSSS");
		return sdf.format(new java.util.Date());
	}

	public static String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new java.util.Date());
	}

	public static String formatDateYMD(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(StringToDate(str));
	}

	public static String formatDate(java.util.Date date) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.format(date);
		} else {
			return "";
		}
	}


	public static String getDateTOString(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new java.util.Date());
	}

	public static String getDateYear(String ds) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date d = new java.util.Date();
		try {
			d = sdf.parse(ds);
			calendar.setTime(d);
		} catch (ParseException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		return String.valueOf(calendar.get(Calendar.YEAR));
	}

	public static String getDateMonth(String ds) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date d = new java.util.Date();
		try {
			d = sdf.parse(ds);
			calendar.setTime(d);
		} catch (ParseException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		return String.valueOf(calendar.get(Calendar.MONTH) + 1);
	}

	public static String getDateDay(String ds) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date d = new java.util.Date();
		try {
			d = sdf.parse(ds);
			calendar.setTime(d);
		} catch (ParseException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		return String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
	}

	public static String getDateHour(String ds) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
		java.util.Date d = new java.util.Date();
		try {
			d = sdf.parse(ds);
			calendar.setTime(d);
		} catch (ParseException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		return String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
	}

	public static String formatDateTime(String ds) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat sdf2 = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		java.util.Date d = new java.util.Date();
		String df = null;
		try {
			d = sdf2.parse(ds);
			df = sdf.format(d);
		} catch (ParseException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		return df;
	}

	public static String formatDate(String ds) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date d = new java.util.Date();
		String df = null;

		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		try {
			d = sdf2.parse(ds);
			df = sdf.format(d);
		} catch (ParseException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		return df;
	}

	public static String formatDate() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date d = new java.util.Date();
		String df = null;

		df = sdf.format(d);
		return df;
	}

	public static String formatDateYM() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		java.util.Date d = new java.util.Date();
		String df = null;

		df = sdf.format(d);

		return df;
	}

	public static String formatLongTime(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date dt = new Date(Long.parseLong(time));
		String sDateTime = sdf.format(dt);
		return sDateTime;
	}

	public static String formatShortDateTime(String ds) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		SimpleDateFormat sdf2 = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		java.util.Date d = new java.util.Date();
		String df = null;
		try {
			d = sdf2.parse(ds);
			df = sdf.format(d);
		} catch (ParseException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		return df;
	}

	public static String formatStringDateTime(String ds) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date d = new java.util.Date();
		String df = null;
		try {
			d = sdf2.parse(ds);
			df = sdf.format(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return df;
	}

	/**
	 * 添加(或减小)时间
	 * 
	 * @param date
	 * @param field
	 *            要添加(或减小)的字段(年或月或日或...)
	 * @param amount
	 *            要添加(或减小)的数量，amount为正数时，是添加，为负数时是减小
	 * @return 日期
	 */
	public static Date add(Date date, int field, int amount) {
		java.util.Date uDate = convert2JavaDate(date);
		Calendar ca = Calendar.getInstance();
		ca.setTime(uDate);
		ca.add(field, amount);
		java.util.Date uNewDate = ca.getTime();
		Date newDate = convert2SqlDate(uNewDate);
		return newDate;
	}

	/**
	 * 添加(或减小)时间
	 * 
	 * @param date
	 * @param field
	 *            要添加(或减小)的字段(年或月或日或...)
	 * @param amount
	 *            要添加(或减小)的数量，amount为正数时，是添加，为负数时是减小
	 * @param pattern
	 *            格式化模式
	 * @return 格式化后的日期字符串
	 */
	public static String add(Date date, int field, int amount, String pattern) {

		java.util.Date uDate = convert2JavaDate(date);
		Calendar ca = Calendar.getInstance();
		ca.setTime(uDate);
		ca.add(field, amount);

		return format(ca.getTime(), pattern);
	}

	/**
	 * 添加(或减小)时间
	 * 
	 * @param date
	 * @param field
	 *            要添加(或减小)的字段(年或月或日或...)
	 * @param amount
	 *            要添加(或减小)的数量，amount为正数时，是添加，为负数时是减小
	 * @param pattern
	 *            格式化模式
	 * @return 格式化后的日期字符串
	 * @throws ParseException
	 */
	public static String add(String date, int field, int amount, String pattern)
			throws ParseException {
		Calendar ca = Calendar.getInstance();
		ca.setTime(parse(date, pattern));
		ca.add(field, amount);

		return format(ca.getTime(), pattern);
	}

	/**
	 * 根据字符串生成日期
	 * 
	 * @param dateStr
	 * @param pattern
	 * @return Date
	 * @throws ParseException
	 */

	public static java.util.Date parse(String dateStr, String pattern)
			throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.parse(dateStr);
	}

	/**
	 * 格式化日期
	 * 
	 * @param date
	 * @param pattern
	 * @return String
	 */
	public static String format(java.util.Date date, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}

	/**
	 * 将java.util.Date转换为java.sql.Date
	 * 
	 * @param javaDate
	 * @return
	 */
	public static Date convert2SqlDate(java.util.Date javaDate) {
		Date sd;

		sd = new Date(javaDate.getTime());
		return sd;

	}

	/**
	 * 将java.sql.Date转换为java.util.Date
	 * 
	 * @param sqlDate
	 * @return
	 */
	public static java.util.Date convert2JavaDate(Date sqlDate) {
		return new Date(sqlDate.getTime());
	}

	public static void main(String[] args) {

		System.out.println(DateUtils.formatDate("2006-8-23"));
		System.out
				.println(DateUtils.formatShortDateTime("2006-08-23 20:12:30"));
		System.out.println(DateUtils.formatDateTime("2006-08-23 20:13:22"));

		String gs = "2002-01-01";

		System.out.println(DateUtils.formatDate());
		System.out.println(DateUtils.formatDateYM());

		System.out.println("revertList[0].kindValue".substring(0,
				"revertList[0].kindValue".indexOf(".") + 1));

	}

	/**
	 * 获取系统时间 精确到毫秒数
	 * 
	 * @return
	 */
	public String getSysDateForServer() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		return sdf.format(new java.util.Date());
	}

	public static String getSqliteDateTime() {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 定义格式，不显示毫秒
		Timestamp now = new Timestamp(System.currentTimeMillis());// 获取系统当前时间
		return df.format(now);

	}

	public static String formatDateToCompare(java.util.Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	/** 得到当前时间44天前的日期 */
	public static java.util.Date getPre44Day() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new java.util.Date());
		cal.add(Calendar.DAY_OF_MONTH, -90); // 第2天，第x天，照加。如果是负数，表示前n天。
		System.out.println("前面的日期:"
				+ new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
		return cal.getTime();
	}

	/** 得到当前时间45天后的日期 */
	public static java.util.Date getNext45Day() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new java.util.Date());
		cal.add(Calendar.DAY_OF_MONTH, 90); // 第2天，第x天，照加。如果是负数，表示前n天。
		System.out.println("后面的日期:"
				+ new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
		return cal.getTime();
	}

	/** 判断该日期是否在当前日期的90天内 */
	public static boolean isInDate(java.util.Date dt) {

		System.out.println("当前的日期:"
				+ new SimpleDateFormat("yyyy-MM-dd").format(dt.getTime()));
		return dt.compareTo(getPre44Day()) >= 0
				&& dt.compareTo(getNext45Day()) <= 0;
	}

	/** 判断该日期是否在当前日期的90天内 */
//	public static boolean isInDate(DateVO dateVO) {
//		java.util.Date dt = parseDateVO(dateVO);
//
//		if (dt == null) {
//			return false;
//		}
//
//		System.out.println("当前的日期:"
//				+ new SimpleDateFormat("yyyy-MM-dd").format(dt.getTime()));
//		return dt.compareTo(getPre44Day()) >= 0
//				&& dt.compareTo(getNext45Day()) <= 0;
//	}

	/**
	 * @param DateStr1
	 * @param DateStr2
	 * @return 如果dateTime1大于dateTime2,则返回1; 如果dateTime1等于dateTime2,则返回0;
	 *         如果dateTime1小于dateTime2,则返回-1;
	 */
	public static int compareToDate(java.util.Date DateStr1, String DateStr2) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date dateTime2 = null;
		try {
			dateTime2 = dateFormat.parse(DateStr2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return DateStr1.compareTo(dateTime2);
	}

	/**
	 * 判断日期是否有效,包括闰年的情况
	 * 
	 * @param date
	 *            YYYY-mm-dd
	 * @return
	 */
	public static boolean isDate(String date) {
		StringBuffer reg = new StringBuffer(
				"^((\\d{2}(([02468][048])|([13579][26]))-?((((0?");
		reg.append("[13578])|(1[02]))-?((0?[1-9])|([1-2][0-9])|(3[01])))");
		reg.append("|(((0?[469])|(11))-?((0?[1-9])|([1-2][0-9])|(30)))|");
		reg.append("(0?2-?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][12");
		reg.append("35679])|([13579][01345789]))-?((((0?[13578])|(1[02]))");
		reg.append("-?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))");
		reg.append("-?((0?[1-9])|([1-2][0-9])|(30)))|(0?2-?((0?[");
		reg.append("1-9])|(1[0-9])|(2[0-8]))))))");
		Pattern p = Pattern.compile(reg.toString());
		return p.matcher(date).matches();
	}

}
