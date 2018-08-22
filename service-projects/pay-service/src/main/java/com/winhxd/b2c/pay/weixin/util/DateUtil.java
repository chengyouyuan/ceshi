package com.winhxd.b2c.pay.weixin.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

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

}
