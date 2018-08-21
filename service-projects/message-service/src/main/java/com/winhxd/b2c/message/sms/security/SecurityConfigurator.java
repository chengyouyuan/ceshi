package com.winhxd.b2c.message.sms.security;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 安全检查配置文件加载
 *
 */
public class SecurityConfigurator {

    private static SecurityConfigurator configurator = new SecurityConfigurator();
	Properties props = new Properties();
	private SecurityConfigurator(){
		InputStream securityConfig = null;
		try {

			securityConfig = SecurityConfigurator.class.getResourceAsStream(SecurityConstant.SECURITY_CONFIG_PATH+ SecurityConstant.SECURITY_CONFIG_NAME);
			props.load(securityConfig);					
		} 
		catch (Exception e) {

		}
		finally{
			if(securityConfig!=null) {
				try {
					securityConfig.close();
				}
                catch (IOException e) {
				}
			}
		}
	}
	public static SecurityConfigurator getInstance() {
		return configurator;
	}
	public String getValueByKey(String key) {
		return props.getProperty(key);
	}
}
