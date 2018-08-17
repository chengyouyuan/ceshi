package com.winhxd.b2c.message.sms.common;

import com.winhxd.b2c.common.context.support.ContextHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 短信发送常量
 * */
public class SmsConstant {

	private static final Logger LOGGER = LoggerFactory.getLogger(SmsConstant.class);
    
    /**
     * 阅信短信平台参数key 用户名
     * */
    public static final String KEY_NAME_YX = "name";
    
    /**
     * 阅信平台返回值类型的key
     * */
    public static final String KEY_RPTTYPE_YX ="rpttype";

    /**
     * 阅信平台返回值类型 0 代表xml
     * */
    public static final String RPTTYPE_XML ="0";
    
    /**
     * 阅信平台返回值类型1 代表json
     * */
    public static final String RPTTYPE_JSON ="1";
    /**
     * 阅信短信平台参数key 密码
     * */
    public static final String KEY_PSWD_YX = "pwd";
    
    /**
     * 阅信短信平台参数key 短信内容
     * */
    public static final String KEY_CONTENT_YX = "content";
    
    /**
     * 阅信短信平台参数key 手机号
     * */
    public static final String KEY_PHONE_YX = "phone";
    
    /**
     * 阅信短信平台参数key
     * */
    public static final String KEY_MTTIME_YX = "mttime";
    
    /**
     * 阅信短信平台参数key
     * */
    public static final String KEY_SUBID_YX = "subid";
    
	/**
	 * 短信发送url的key
	 * */
	public static final String KEY_URL = "url";
	
	/**
     * 短信发送密码的key
     * */
    public static final String KEY_PSWD = "pswd";
	
	/**
	 * 创蓝验证码通道url的key
	 * */
	public static final String KEY_VERIFICATION_URL= "verification_url";
	
	/**
     * 阅信验证码通道url的key
     * */
    public static final String KEY_VERIFICATION_URL_YX= "verification_url_yx";
	
	/**
	 * 创蓝营销通道url的key
	 * */
	public static final String KEY_MARKETING_URL= "marketing_url";
	/**
	 * 短信发送是否需要返回状态的key
	 * */
	public static final String KEY_NEEDSTATUS = "needstatus";

	/**
	 * 短信发送是否需要返回状态
	 * */
	public static final boolean NEEDSTATUS = true;

	/**
	 * 短信发送账户的key
	 * */
	public static final String KEY_ACCOUNT = "account";

	/**
	 * 短信发送手机号码的key
	 * */
	public static final String KEY_MOBILE = "mobile";

	/**
	 * 短信发送内容的key
	 * */
	public static final String KEY_CONTENT = "msg";

	/**
	 * 日期格式 yyyy-MM-dd hh:mm:ss
	 * */
	public static final String DATE_PATTERN = "yyyy-MM-dd hh:mm:ss";

	/**
	 * 短信发送配置文件相对路径
	 * */
	public static final String SERVER_CONFIG_PATH = "/config/";

	/**
	 * 短信发送配置文件名称
	 * */
	public static final String SERVER_CONFIG_NAME = "sms_server.properties";

	/**
	 * 空字符串
	 * */
	public static final String EMPTY = "";

	/**
	 * 短信发送结果分隔符\n(换行符)
	 * */
	public static final String SPLIT_WRAP = "\n";

	/**
	 * 分隔符‘_’(下划线)
	 * */
	public static final String SPLIT_UNDERLINE = "_";

	/**
	 * HttpBatch
	 * */
	public static final String HTTPBATCH = "HttpBatchSendSM";

	/**
	 * urldecoder
	 * */
	public static final String URLDECODER_UTF8 = ContextHelper.UTF_8;

	/**
	 * 短信内容最大长度536个字符
	 * */
	public static final int CONTENT_MAX_LENGTH = 536;

	/**
	 * 短信发送服务端配置文件名称
	 * */
	public static final String SERVER_PROPERTIES = "sms_server.properties";

	/**
	 * longmessage.class的配置文件key
	 * */
	public static final String KEY_LONGMESSAGE_CLASS = "longmessage.class";

	/**
	 * 消息内容长度达到此值后 使用 longmessage接口
	 * */
	public static final int LONGMESSAGE_LENGTH = 62;

	/**
	 * 国际短信号码前缀
	 * */
	public static final String INTERNATIONAL_PREFIX = "00";

	/**
	 * 短信发送供应商class的配置文件key
	 * */
	public static final String KEY_CLASS = ".class";

	/**
	 * 短信发送供应商replace的配置文件key
	 * */
	public static final String KEY_REPLACE = ".replace";

	/**
	 * 短信发送返回给客户端结果 成功
	 * */
	public static final boolean RETURN_SUCCESS = true;

	/**
	 * 短信发送返回给客户端结果 失败
	 * */
	public static final boolean RETURN_FAIL = false;

	/**
	 * 短信发送返回给客户端结果的key
	 * */
	public static final String KEY_RETURN = "result";
	
	/**
	 * 验证码短信标识
	 * */
	public static final String FLAG_VERIFICATION = "验证码";
	
	/**
     * 营销短信后缀
     * */
	public static final String SUFFIX_MARKETING = "退订回复TD。";

	/**
	 * 连接/读取 超时时间 300毫秒
	 * */
	public static final int TIMEOUT = 300;
	
	/**
	 * 创蓝混用通道的url
	 * */
	private static String url = null;

	/**
     * 创蓝验证码通道url
     * */
	private static String verificationUrl = null;
	
    /**
     * 创蓝营销通道url
     * */
    private static String marketingUrl = null;
    
    /**
     * 阅信平台验证码通道url
     * */
    private static String verificationUrl_yx = null;
    
    private SmsConstant() {
    }

    public static String getURL() {
        if (url == null) {
            setURL();
        }
        return url;
    }
	    
    public static synchronized void setURL() {
        try {
            url = SmsConfigurator.getInstance().getValueByKey(KEY_URL);
        }
        catch (Exception e) {
            url = "";
			LOGGER.error("获取混用url异常");
        }
    }

    public static String getVerificationUrl() {
        if (verificationUrl == null) {
            setVerificationUrl();
        }
        return verificationUrl;
    }

    public static synchronized void setVerificationUrl() {
        try {
            verificationUrl = SmsConfigurator.getInstance().getValueByKey(KEY_VERIFICATION_URL);
        }
        catch (Exception e) {
            verificationUrl = "";
			LOGGER.error("获取验证码通道url异常",e);
        }
    }

    public static String getMarketingUrl() {
        if (marketingUrl == null) {
            setMarketingUrl();
        }
        return marketingUrl;
    }

    public static synchronized void setMarketingUrl() {
        try {
            marketingUrl = SmsConfigurator.getInstance().getValueByKey(KEY_MARKETING_URL);
        }
        catch (Exception e) {
            marketingUrl = "";
			LOGGER.error("获取营销通道url异常",e);
        }
    }
    
    /**
     * 获取阅信平台验证码通道url
     * */
    public static String getYXVerificationUrl() {
        if (verificationUrl_yx == null) {
            setYXVerificationUrl();
        }
        return verificationUrl_yx;
    }

    public static synchronized void setYXVerificationUrl() {
        try {
            verificationUrl_yx = SmsConfigurator.getInstance().getValueByKey(KEY_VERIFICATION_URL_YX);
        }
        catch (Exception e) {
            verificationUrl_yx = "";
			LOGGER.error("获取验证码通道url异常",e);
        }
    }

	private static Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();

	public static Map<String, Map<String, String>> getAll() {
		return map;
	}

	public static String getSerialNumber(String flag) {
		Map<String, String> param = map.get(flag);
		if (param != null) {
			String serialNumber = param.get("serialNumber");
			return serialNumber;
		} else {
			return null;
		}
	}

	public static String getKey(String flag) {
		Map<String, String> param = map.get(flag);
		if (param != null) {
			String key = param.get("key");
			return key;
		} else {
			return null;
		}
	}

	public static String getSerialpass(String flag) {
		Map<String, String> param = map.get(flag);
		if (param != null) {
			String serialpass = param.get("serialpass");
			return serialpass;
		} else {
			return null;
		}
	}

	protected final static void putMap(String flag, String serialNumber, String key, String serialpass) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("serialNumber", serialNumber);
		param.put("key", key);
		param.put("serialpass", serialpass);
		map.put(flag, param);
	}
}
