/**
 * 
 */
package com.cmit.mvne.billing.creditcontrol.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;


/**
 * @author jiangxm
 * 处理各种日期类
 */
public class DateUtil {
	
	/**
	 * 获取当前时间的yyyy-MM-dd HH:mm:ss字符串格式的当前时间
	 * @return 返回字符串结果
	 */
	public static String getYYYYMMDDHHmmssDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(new Date());
	}
	
	/**
	 * 获取当前时间的yyyyMMddHHmmss字符串格式的当前时间
	 * @return 返回字符串结果
	 */
	public static String getYYYYMMDDHHmmssDate_1() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		return dateFormat.format(new Date());
	}
	
	/**
	 * 获取当前月上一个月的第一天
	 * @return 返回yyyy-MM-dd HH:mm:ss字符串格式的日期值
	 */
	public static String getLastMonthFirstDay() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime previousMonth = now.minusMonths(1);
		LocalDateTime firstDay = previousMonth.with(TemporalAdjusters.firstDayOfMonth());
		LocalDateTime firstDayTime = LocalDateTime.of(firstDay.toLocalDate(), LocalTime.MIN);
		String firstDayString = firstDayTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		return firstDayString;
	}
	
	/**
	 * 获取当前月上一个月的最后一天
	 * @return 返回yyyy-MM-dd HH:mm:ss字符串格式的日期值
	 */
	public static String getLastMonthLastDay() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime previousMonth = now.minusMonths(1);
		LocalDateTime lastDay = previousMonth.with(TemporalAdjusters.lastDayOfMonth());
		LocalDateTime lastDayTime = LocalDateTime.of(lastDay.toLocalDate(), LocalTime.MAX);
		String lastDayString = lastDayTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		return lastDayString;
	}
	
	public static String getLastMonth() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime previousMonth = now.minusMonths(1);
		String lastMonthString = previousMonth.format(DateTimeFormatter.ofPattern("yyyyMM"));
//		System.out.println(lastMonthString);
		return lastMonthString;
	}
	
	/**
	 * @Description 获取当前时间的13个月之前的时间字符串
	 * @return
	 */
	public static String getDeleteUserMonth() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime deleteUserMonth = now.minusMonths(13);
		String deleteUserMonthString = deleteUserMonth.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		return deleteUserMonthString;
	}
	
	public static void main(String[] args) throws ParseException {
//		getLastMonth();
//		Date date = new Date();
//		date.setTime(1579003001862l);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String string=sdf.format(date);
		System.out.println(System.currentTimeMillis());
		String dateString = "2029-12-31 16:17:25";
		
		System.out.println(sdf.parse(dateString).getTime());
	}
}
