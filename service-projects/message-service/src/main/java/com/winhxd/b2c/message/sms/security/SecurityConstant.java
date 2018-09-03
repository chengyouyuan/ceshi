package com.winhxd.b2c.message.sms.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 短信发送安全检查常量
 * */
@Component
public class SecurityConstant {
    private static Log logger = LogFactory.getLog(SecurityConstant.class);
    private SecurityConstant() {
        
    }
	/**
	 * 普通手机号码每日发送短信数量上限(次)
	 * */
	private static Integer MOBILE_MAX_TIMES;

	@Value("${security.mobile.maxtimes}")
	public void setMobileMaxTimes(Integer mobileMaxTimes) {
		MOBILE_MAX_TIMES = mobileMaxTimes;
	}

	/**
	 * 普通IP地址每日发送短信数量上限(次)
	 * */
	private static Integer IP_MAX_TIMES;

	@Value("${security.ip.maxtimes}")
	public void setIP_MAX_TIMES(Integer ipMaxTimes) {
		IP_MAX_TIMES = ipMaxTimes;
	}

	/**
	 * 普通手机号码 两次发送短信时间间隔(毫秒)
	 * */
	private static Long MOBILE_TIME_INTERVAL;

	@Value("${security.mobile.timeinterval}")
	public void setMOBILE_TIME_INTERVAL(Long mobileTimeInterval) {
		MOBILE_TIME_INTERVAL = mobileTimeInterval;
	}

	/**
	 * 普通IP两次发送短信时间间隔(毫秒)
	 * */
	private static Long IP_TIME_INTERVAL;

	@Value("${security.ip.timeinterval}")
	public void setIP_TIME_INTERVAL(Long ipTimeInterval) {
		IP_TIME_INTERVAL = ipTimeInterval;
	}

	/**
	 * 白名单手机号码 两次发送短信时间间隔(毫秒)
	 * */
	private static Long MOBILE_WHITELIST_TIME_INTERVAL;

	@Value("${security.mobile.whitelist.timeinterval}")
	public void setMOBILE_WHITELIST_TIME_INTERVAL(Long mobileWhitelistTimeInterval) {
		MOBILE_WHITELIST_TIME_INTERVAL = mobileWhitelistTimeInterval;
	}

	/**
	 * 白名单IP两次发送短信时间间隔(毫秒)
	 * */
	private static Long IP_WHITELIST_TIME_INTERVAL;

	@Value("${security.ip.whitelist.timeinterval}")
	public void setIP_WHITELIST_TIME_INTERVAL(Long ipWhitelistTimeInterval) {
		IP_WHITELIST_TIME_INTERVAL = ipWhitelistTimeInterval;
	}

	/**
	 * 白名单手机每日发送短信次数上限(次)
	 * */
	private static Integer MOBILE_WHITELIST_MAX_TIMES;

	@Value("${security.mobile.whitelist.maxtimes}")
	public void setMOBILE_WHITELIST_MAX_TIMES(Integer mobileWhitelistMaxTimes) {
		MOBILE_WHITELIST_MAX_TIMES = mobileWhitelistMaxTimes;
	}

	/**
	 * 白名单IP每日发送短信次数上限(次)
	 * */
	private static Integer IP_WHITELIST_MAX_TIMES;

	@Value("${security.ip.whitelist.maxtimes}")
	public void setIP_WHITELIST_MAX_TIMES(Integer ipWhitelistMaxTimes) {
		IP_WHITELIST_MAX_TIMES = ipWhitelistMaxTimes;
	}
	
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
			MOBILE_MAX_TIMES = DEFAULT_MOBILE_MAX_TIMES;
		}
        return MOBILE_MAX_TIMES;
    }
    
    /**
     *  获取白名单手机每日发送短信次数上限(次)
     * */
    public static Integer getMobileWhitelistMaxTimes() {
        if (MOBILE_WHITELIST_MAX_TIMES == null){
			MOBILE_WHITELIST_MAX_TIMES = DEFAULT_MOBILE_WHITELIST_MAX_TIMES;
        }
        return MOBILE_WHITELIST_MAX_TIMES;
    }
    
    /**
     *  获取普通手机发送短信时间间隔(毫秒)
     * */
    public static Long getMobileTimeInterval() {
        
        if (MOBILE_TIME_INTERVAL == null) {
			MOBILE_TIME_INTERVAL = DEFAULT_MOBILE_TIME_INTERVAL;
        }
        return MOBILE_TIME_INTERVAL;
    }
    
    /**
     *  获取白名单手机发送短信时间间隔(毫秒)
     * */
    public static Long getMobileWhitelistTimeInterval() {
        
        if (MOBILE_WHITELIST_TIME_INTERVAL == null){
			MOBILE_WHITELIST_TIME_INTERVAL = DEFAULT_MOBILE_WHITELIST_TIME_INTERVAL;
        }
        return MOBILE_WHITELIST_TIME_INTERVAL;
    }

    /**
     *  获取普通IP每日发送次数上限
     * */
    public static Integer getIpMaxTimes() 
    {
        if (IP_MAX_TIMES == null){
			IP_MAX_TIMES = DEFAULT_IP_MAX_TIMES;
        }
        return IP_MAX_TIMES;
    }

    /**
     *  获取普通IP地址发送短信时间间隔
     * */
    public static Long getIpTimeInterval() {
        
        if (IP_TIME_INTERVAL == null){
			MOBILE_WHITELIST_MAX_TIMES = DEFAULT_MOBILE_WHITELIST_MAX_TIMES;
		}
        return IP_TIME_INTERVAL;   
    }

    /**
     * 获取白名单Ip每日发送短信次数上限
     * */
    public static Integer getIPWhitelistMaxTimes(){
        if (IP_WHITELIST_MAX_TIMES == null){
			IP_WHITELIST_MAX_TIMES = DEFAULT_IP_WHITELIST_MAX_TIMES;
        }
        return IP_WHITELIST_MAX_TIMES;
    }
    
    /**
     * 获取白名单Ip每日发送短信次数上限
     * */
    public static Long getIPWhitelistTimeInterval(){
        if (IP_WHITELIST_TIME_INTERVAL == null){
			IP_WHITELIST_TIME_INTERVAL = DEFAULT_IP_WHITELIST_TIME_INTERVAL;
        }
        return IP_WHITELIST_TIME_INTERVAL;
    }
}
