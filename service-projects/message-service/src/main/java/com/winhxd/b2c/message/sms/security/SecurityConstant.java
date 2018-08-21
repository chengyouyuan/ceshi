package com.winhxd.b2c.message.sms.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 短信发送安全检查常量
 * */
public class SecurityConstant {
    private static Log logger = LogFactory.getLog(SecurityConstant.class);
    private SecurityConstant() {
        
    }
	/**
	 * 普通手机号码每日发送短信数量上限(次)
	 * */
	private static Integer MOBILE_MAX_TIMES = null;
	
	/**
	 * 普通IP地址每日发送短信数量上限(次)
	 * */
	private static Integer IP_MAX_TIMES = null;
	
	/**
	 * 普通手机号码 两次发送短信时间间隔(毫秒)
	 * */
	private static Long MOBILE_TIME_INTERVAL = null;
	
	/**
     * 普通IP两次发送短信时间间隔(毫秒)
     * */
	private static Long IP_TIME_INTERVAL = null;
	
	/**
     * 白名单手机号码 两次发送短信时间间隔(毫秒)
     * */
    private static Long MOBILE_WHITELIST_TIME_INTERVAL = null;
    
    /**
     * 白名单IP两次发送短信时间间隔(毫秒)
     * */
    private static Long IP_WHITELIST_TIME_INTERVAL = null;
	
	/**
	 * 白名单手机每日发送短信次数上限(次)
	 * */
	private static Integer MOBILE_WHITELIST_MAX_TIMES = null;
	
	/**
     * 白名单IP每日发送短信次数上限(次)
     * */
    private static Integer IP_WHITELIST_MAX_TIMES = null;
	
	/**
     * 普通手机号码每日发送短信数量上限默认值(次)
     * */
    private static Integer DEFAULT_MOBILE_MAX_TIMES = 15;
    
    /**
     * 普通IP地址每日发送短信数量上限默认值 (次)
     * */
    private static Integer DEFAULT_IP_MAX_TIMES = 15;
    
    /**
     * 白名单手机每日发送短信次数上限默认值(次)
     * */
    private static Integer DEFAULT_MOBILE_WHITELIST_MAX_TIMES = 100;
    
    /**
     * 白名单IP每日发送短信次数上限默认值(次)
     * */
    private static Integer DEFAULT_IP_WHITELIST_MAX_TIMES = 100;
    
    /**
     * 普通手机号码 两次发送短信时间间隔默认值(毫秒)
     * */
    private static Long DEFAULT_MOBILE_TIME_INTERVAL = 60000L;
    
    /**
     * 普通IP两次发送短信时间间隔默认值(毫秒)
     * */
    private static Long DEFAULT_IP_TIME_INTERVAL = 60000L;
    
    
    /**
     * 白名单手机两次发送短信时间间隔默认值(毫秒)
     * */
    private static Long DEFAULT_MOBILE_WHITELIST_TIME_INTERVAL = 60000L;
    
    /**
     * 白名单IP两次发送短信时间间隔默认值(毫秒)
     * */
    private static Long DEFAULT_IP_WHITELIST_TIME_INTERVAL = 60000L;
	
	/**
	 * 普通手机号码每日发送短信数量上限(次) security.properties 对应的属性
	 * */ 
	private static final String SECURITY_MOBILE_MAXTIMES = "security.mobile.maxtimes";

	/**
	 * #普通手机号码 两次发送短信时间间隔(毫秒) security.properties 对应的属性:security.mobile.timeinterval
	 * */
	private static final String SECURITY_MOBILE_TIMEINTERVAL = "security.mobile.timeinterval";

	/**
	 * #白名单手机内的号码 每日发送短信数量上限(次) security.properties 对应的属性：security.mobile.whitelist.maxtimes
	 * */	
	private static final String SECURITY_MOBILE_WHITELIST_MAXTIMES ="security.mobile.whitelist.maxtimes";

	/**
	 * #白名单手机号码 两次发送短信时间间隔(毫秒) security.properties 对应的属性:security.mobile.whitelist.timeinterval
	 * */	
	private static final String SECURITY_MOBILE_WHITELIST_TIMEINTERVAL ="security.mobile.whitelist.timeinterval";

	/**
	 * #普通IP地址每日发送短信数量上限(次) security.properties 对应的属性：security.ip.maxtimes
	 * */	
	private static final String SECURITY_IP_MAXTIMES ="security.ip.maxtimes";

	/**
	 * #普通IP地址 两次发送短信时间间隔(毫秒) security.properties 对应的属性:security.ip.timeinterval
	 * */	
	private static final String SECURITY_IP_TIMEINTERVAL = "security.ip.timeinterval";

	/**
	 * #ip白名单内的号码 每日发送短信数量上限(次) security.properties 对应的属性：security.ip.whitelist.maxtimes
	 * */
	private static final String SECURITY_IP_WHITELIST_MAXTIMES = "security.ip.whitelist.maxtimes";

	/**
	 * #IP地址 白名单两次发送短信时间间隔(毫秒) security.properties 对应的属性:security.ip.timeinterval
	 * */ 
	private static final String SECURITY_IP_WHITELIST_TIMEINTERVAL= "security.ip.whitelist.timeinterval";

	/**
	 * 安全校验配置文件相对路径
	 * */ 
	public static final String SECURITY_CONFIG_PATH = "/config/";
    
	/**
     * 安全校验配置文件名称
     * */ 
	public static final String SECURITY_CONFIG_NAME = "sms_security.properties";
	
	/**
	 * 分隔符,(逗号)
	 * */
	public static final String SPLIT = ",";
	
	/**
     * 手机号短信发送次数信息key
     * */
    public static final String KEY_MOBILE_TIMES = "REDIS_KEY_SMS_CLIENT_TLE_TIMES_";
    
    /**
     * IP地址信发送次数信息key
     * */
    public static final String KEY_IP_TIMES = "REDIS_KEY_SMS_CLIENT_IP_TIMES_";
    
    /**
     * 手机号码白名单key
     * */
    public static final String KEY_MOBILE_WHITELIST = "REDIS_KEY_SMS_CLIENT_MOBILE_WHITELIST";
    
    /**
     * ip地址白名单key
     * */
    public static final String KEY_IP_WHITELIST = "REDIS_KEY_SMS_CLIENT_IP_WHITELIST";
    
    /**
     * 首次发送短信值
     * */
    public static Integer FIRSR_TIMES = 1;
	
	/**
	 *  获取普通手机每日发送次数上限(次)
	 * */
    public static Integer getMobileMaxTimes() 
    {
        if (MOBILE_MAX_TIMES == null) {
            setMobileMaxTimes();
        }
        return MOBILE_MAX_TIMES;
    }
    
    /**
     *  获取白名单手机每日发送短信次数上限(次)
     * */
    public static Integer getMobileWhitelistMaxTimes() {
        
        if (MOBILE_WHITELIST_MAX_TIMES == null){
            setMobileWhitelistMaxTimes();
        }
        return MOBILE_WHITELIST_MAX_TIMES;
    }
    
    /**
     *  获取普通手机发送短信时间间隔(毫秒)
     * */
    public static Long getMobileTimeInterval() {
        
        if (MOBILE_TIME_INTERVAL == null) {
            setMobileTimeInterval();
        }
        return MOBILE_TIME_INTERVAL;
    }
    
    /**
     *  获取白名单手机发送短信时间间隔(毫秒)
     * */
    public static Long getMobileWhitelistTimeInterval() {
        
        if (MOBILE_WHITELIST_TIME_INTERVAL == null){
            setMobileWhitelistTimeInterval();
        }
        return MOBILE_WHITELIST_TIME_INTERVAL;
    }

    
    
    /**
     *  获取普通IP每日发送次数上限
     * */
    public static Integer getIpMaxTimes() 
    {
        if (IP_MAX_TIMES == null){
            setIpMaxTimes();
        }
        return IP_MAX_TIMES;
    }

    /**
     *  获取普通IP地址发送短信时间间隔
     * */
    public static Long getIpTimeInterval() {
        
        if (IP_TIME_INTERVAL == null){
            setIpTimeInterval();
        }
        return IP_TIME_INTERVAL;   
    }

    /**
     * 获取白名单Ip每日发送短信次数上限
     * */
    public static Integer getIPWhitelistMaxTimes(){
        if (IP_WHITELIST_MAX_TIMES == null){
            setIpWhitelistMaxTimes();
        }
        return IP_WHITELIST_MAX_TIMES;
    }
    
    /**
     * 获取白名单Ip每日发送短信次数上限
     * */
    public static Long getIPWhitelistTimeInterval(){
        if (IP_WHITELIST_TIME_INTERVAL == null){
            setIpWhitelistTimeInterval();
        }
        return IP_WHITELIST_TIME_INTERVAL;
    }
    
    /**
     * 设置普通手机每日发送次数上限值(从配置文件获取)
     * */
	private static synchronized void setMobileMaxTimes()
	{
	    try {
	        MOBILE_MAX_TIMES = Integer.parseInt(SecurityConfigurator.getInstance().getValueByKey(SECURITY_MOBILE_MAXTIMES)) ;
        }
        catch (Exception e) {
            MOBILE_MAX_TIMES = DEFAULT_MOBILE_MAX_TIMES;
            logger.error("获取手机每日发送次数上限值异常",e);
        }
	}
	
	 /**
     * 设置普通IP每日发送次数上限值(从配置文件获取)
     * */
    private static synchronized void setIpMaxTimes()
    {
        try {
            IP_MAX_TIMES = Integer.parseInt(SecurityConfigurator.getInstance().getValueByKey(SECURITY_IP_MAXTIMES)) ;
        }
        catch (Exception e) {
            IP_MAX_TIMES = DEFAULT_IP_MAX_TIMES;
            logger.error("获取IP每日发送次数上限值异常",e);
        }
    }
	
    /**
     *  设置普通手机发送短信时间间隔(从配置文件获取)
     * */
    private static synchronized void setMobileTimeInterval()
    {
        try {
            MOBILE_TIME_INTERVAL = Long.parseLong(SecurityConfigurator.getInstance().getValueByKey(SECURITY_MOBILE_TIMEINTERVAL)) ;
        }
        catch (Exception e) {
            MOBILE_TIME_INTERVAL = DEFAULT_MOBILE_TIME_INTERVAL;
            logger.error("获取手机发送短信时间间隔",e);
        }
    }
    
    /**
     *  设置普通IP发送短信时间间隔(从配置文件获取)
     * */
    private static synchronized void setIpTimeInterval()
    {
        try {
            IP_TIME_INTERVAL = Long.parseLong(SecurityConfigurator.getInstance().getValueByKey(SECURITY_IP_TIMEINTERVAL)) ;
        }
        catch (Exception e) {
            IP_TIME_INTERVAL = DEFAULT_IP_TIME_INTERVAL;
            logger.error("获取IP发送短信时间间隔",e);
        }
    }
    
    /**
     *  设置白名单手机号发送短信次数上限(从配置文件获取)
     * */
    private static synchronized void setMobileWhitelistMaxTimes()
    {
        try {
            MOBILE_WHITELIST_MAX_TIMES = Integer.parseInt(SecurityConfigurator.getInstance().getValueByKey(SECURITY_MOBILE_WHITELIST_MAXTIMES)) ;
        }
        catch (Exception e) {
            MOBILE_WHITELIST_MAX_TIMES = DEFAULT_MOBILE_WHITELIST_MAX_TIMES;
            logger.error("设置白名单手机号发送短信次数上限",e);
        }
    }

    /**
     *  设置白名单IP发送短信次数上限(从配置文件获取)
     * */
    private static synchronized void setIpWhitelistMaxTimes()
    {
        try {
            IP_WHITELIST_MAX_TIMES = Integer.parseInt(SecurityConfigurator.getInstance().getValueByKey(SECURITY_IP_WHITELIST_MAXTIMES)) ;
        }
        catch (Exception e) {
            IP_WHITELIST_MAX_TIMES = DEFAULT_IP_WHITELIST_MAX_TIMES;
            logger.error("设置白名单手机号发送短信次数上限",e);
        }
    }
    
    /**
     *  设置白名单手机发送短信时间间隔(从配置文件获取)
     * */
    private static synchronized void setMobileWhitelistTimeInterval()
    {
        try {
            MOBILE_WHITELIST_TIME_INTERVAL = Long.parseLong(SecurityConfigurator.getInstance().getValueByKey(SECURITY_MOBILE_WHITELIST_TIMEINTERVAL)) ;
        }
        catch (Exception e) {
            MOBILE_WHITELIST_TIME_INTERVAL = DEFAULT_MOBILE_WHITELIST_TIME_INTERVAL;
            logger.error("设置白名单IP发送短信时间间隔",e);
        }
    }
    
    /**
     *  设置白名单IP发送短信时间间隔(从配置文件获取)
     * */
    private static synchronized void setIpWhitelistTimeInterval(){
        try {
            IP_WHITELIST_TIME_INTERVAL = Long.parseLong(SecurityConfigurator.getInstance().getValueByKey(SECURITY_IP_WHITELIST_TIMEINTERVAL)) ;
        }
        catch (Exception e) {
            IP_WHITELIST_TIME_INTERVAL = DEFAULT_IP_WHITELIST_TIME_INTERVAL;
            logger.error("设置白名单IP发送短信时间间隔",e);
        }
    }
    
}
