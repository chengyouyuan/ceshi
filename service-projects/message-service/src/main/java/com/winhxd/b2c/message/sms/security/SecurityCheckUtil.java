package com.winhxd.b2c.message.sms.security;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 短信发送安全检查工具类
 * */
public class SecurityCheckUtil 
{

	/**
	 * 手机号码正则表达式
	 * */
	private static final String REG_EXP_MOBILE = "^((1[3-9][0-9]))\\d{8}$";
	
	/**
	 * 手机号码长度
	 * */
	private static final int LENGTH_MOBILE = 11;
	
	
	/**
	 * IP地址最大长度
	 * */
	private static final int IP_MAX_LENTH = 39;
	
	/**
	 * ip地址最小长度
	 * */
	private static final int IP_MIN_LENTH = 7;
	
	/**
	 * 日期格式yyyy-MM-dd
	 * */
	private static final String DATE_FORMAT_YMD = "yyyy-MM-dd";
	
	/**
	 * 日期格式yyyy-MM-dd
	 * */
	private static final String DATE_FORMAT_YMDHMS = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * IPV4正则表达式
	 * */
	private static final Pattern IPV4_PATTERN =  Pattern.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");

	/**
	 * IPV6正则表达式1
	 * */
	private static final Pattern IPV6_STD_PATTERN =  Pattern.compile( "^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");

	/**
	 * IPV6正则表达式2
	 * */
	private static final Pattern IPV6_HEX_COMPRESSED_PATTERN = Pattern.compile("^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");

	/**
	 * 校验是否IPV4正则表达式
	 * */
	public static boolean isIPv4Address(final String input) 
	{
		return IPV4_PATTERN.matcher(input).matches();
	}

	private static boolean isIPv6StdAddress(final String input) 
	{
		return IPV6_STD_PATTERN.matcher(input).matches();
	}

	private static boolean isIPv6HexCompressedAddress(final String input) 
	{
		return IPV6_HEX_COMPRESSED_PATTERN.matcher(input).matches();
	}

	/**
	 * 校验是否IPV6正则表达式
	 * */
	public static boolean isIPv6Address(final String input) 
	{
		return isIPv6StdAddress(input) || isIPv6HexCompressedAddress(input); 
	}
	
	/**
	 * 验证手机号码格式
	 * */
	public static boolean validateMobile (String mobile)
	{
		boolean rs = false;	
		if (validateLength(mobile, LENGTH_MOBILE,LENGTH_MOBILE))
		{
			Pattern pattern = Pattern.compile(REG_EXP_MOBILE);
			
			Matcher matcher = pattern.matcher(mobile);
			rs = matcher.matches();
		}

		return rs;
	}
	
	/**
	 * 验证IP地址格式
	 * */
	public static boolean validateIP (String ipAddress)
	{
		boolean rs = false;	
		if (validateLength(ipAddress, IP_MAX_LENTH,IP_MIN_LENTH))
		{
			rs = true;
		}

		return rs;
	}
	
	
	/**
	 * 验证str是否为length位的数字
	 * */
	public static boolean validateNumber(String str,int length)
	{
		boolean rs = false;	
		if (SecurityCheckUtil.validateLength(str, length, length))
		{
			Pattern pattern = Pattern.compile("[0-9]{"+length+"}");
			Matcher matcher = pattern.matcher(str);
			rs = matcher.matches();
		}
		
		return rs;
	}
	
	/**
	 * 验证str是否为max~min位的数字
	 * */
	public static boolean validateNumber(String str,int max,int min)
	{
		boolean rs = false;	
		if (SecurityCheckUtil.validateLength(str, max, min))
		{
			Pattern pattern = Pattern.compile("[0-9]{"+str.length()+"}");
			Matcher matcher = pattern.matcher(str);
			rs = matcher.matches();
		}
		
		return rs;
	}
	
	/**
	 * 字符串长度校验(包含非空验证)
	 * @param str 
	 * @param max 最大长度
	 * @param min 最小长度
	 * */
	public static boolean validateLength(String str,int max,int min)
	{
		if (max < 0)
		{
			max = 0;
		}
		if (min < 0 )
		{
			min = 0;
		}
		if (str == null || str.length() > max || str.length() < min)
		{
			return false;
		}
		return true;
	}
	
	/**
	 * 格式化日期为yyyy-mm-dd
	 * @param date 需要格式化的日期
	 * @return 格式化后的字符串,如果date为空返回当前时间。
	 * */
	public static String formatDateYMD(Date date)
	{
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_YMD);
		if (date == null )
		{
			date = new Date();
		}
		
		return format.format(date);
	}
	
	/**
	 * 格式化日期为yyyy-MM-dd HH:mm:ss
	 * @param time 需要格式化的日期
	 * @return 格式化后的字符串,如果date为空返回当前时间。
	 * */
	public static String formatDateYMDHMS(String time) {
		SimpleDateFormat formatOld = new SimpleDateFormat("yyyyMMddHHssmm");
		Date oldDate;
		String newTime;
		try {
			oldDate = formatOld.parse(time);
		
			SimpleDateFormat formatNew = new SimpleDateFormat(DATE_FORMAT_YMDHMS);
			newTime = formatNew.format(oldDate);
		}
		catch (ParseException e) {
			newTime = null;
		}
		return newTime;
	}
}
