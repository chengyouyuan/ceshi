package com.winhxd.b2c.common.util;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * @author mahongliang
 * @date  2018年8月18日 下午6:55:38
 * @Description 
 * @version
 */
public class DateUtil {
	
	/**
	 * 时间转字符串
	 * @author mahongliang
	 * @date  2018年8月18日 下午6:55:51
	 * @Description 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String format(Date date, String format){
		if(date == null){
			date = new Date();
		}
		if(StringUtils.isEmpty(format)) {
			return null;
		}
		return new SimpleDateFormat(format).format(date);
	}
	
	/**
	 * 时间字符串转日期
	 * @author mahongliang
	 * @date  2018年8月18日 下午8:05:13
	 * @Description 
	 * @param strDate
	 * @param format
	 * @return
	 */
	public static Date toDate(String strDate, String format) {
		Date date = null;
		
		if(StringUtils.isEmpty(format)) {
			return null;
		}
		
		try {
			date = new SimpleDateFormat(format).parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return date;
	}

	public static Date getStartDate(Date startDate){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();
	}

	public static Date getEndDate(Date endDate){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endDate);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 59);

		return calendar.getTime();
	}

	public static Date getEndDate(Date endDate,Integer effectiveDays){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endDate);
		calendar.add(Calendar.DATE, effectiveDays);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 59);

		return calendar.getTime();
	}

}
