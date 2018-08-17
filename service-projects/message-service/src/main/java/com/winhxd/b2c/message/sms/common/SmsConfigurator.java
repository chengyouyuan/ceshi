package com.winhxd.b2c.message.sms.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 短信发送配置文件加载
 * 
 */
public class SmsConfigurator {
	private static SmsConfigurator configurator = new SmsConfigurator();
	private final Logger logger = LoggerFactory.getLogger(SmsConfigurator.class);
	Properties props = new Properties();

	private SmsConfigurator() {
		InputStream securityConfig = null;
		try {

			securityConfig = SmsConfigurator.class.getResourceAsStream(SmsConstant.SERVER_CONFIG_PATH + SmsConstant.SERVER_CONFIG_NAME);
			props.load(securityConfig);
		}
		catch (Exception e) {
			logger.error("加载配置文件异常.");
		}
		finally {
			if (securityConfig != null) {
				try {
					securityConfig.close();
				}
				catch (IOException e) {
					logger.error("关闭配置文件异常.");
				}
			}
		}
	}

	public static SmsConfigurator getInstance() {
		return configurator;
	}

	public String getValueByKey(String key) {
		return props.getProperty(key);
	}
}
