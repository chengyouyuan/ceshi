package com.winhxd.b2c.pay.config;

import java.math.BigDecimal;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * PayWithDrawalConfig.java
 *
 * @Author zhanghaun
 **/
@Component
@ConfigurationProperties(prefix="pay.withdrawal")
public class PayWithdrawalConfig {
	private BigDecimal cmmsamt;// 银行手续费
	private BigDecimal rate;// 微信费率
	private BigDecimal maxMoney;
	
	public BigDecimal getCmmsamt() {
		return cmmsamt;
	}
	public void setCmmsamt(BigDecimal cmmsamt) {
		this.cmmsamt = cmmsamt;
	}
	public BigDecimal getRate() {
		return rate;
	}
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	public BigDecimal getMaxMoney() {
		return maxMoney;
	}
	public void setMaxMoney(BigDecimal maxMoney) {
		this.maxMoney = maxMoney;
	}
	
}