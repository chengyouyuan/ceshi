package com.winhxd.b2c.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * PayWithDrawalConfig.java
 *
 * @Author zhanghuan
 **/
@Component
@ConfigurationProperties(prefix="pay.withdrawal")
@Data
public class PayWithdrawalConfig {
	/**
	 * 银行手续费
	 */
	private BigDecimal cmmsamt;
	/**
	 * 微信费率
	 */
	private BigDecimal rate;
	private BigDecimal maxMoney;
	private BigDecimal minMoney;
	private int maxCount;
	
}