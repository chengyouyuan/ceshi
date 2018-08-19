package com.winhxd.b2c.common.domain.pay.constant;

import java.math.BigDecimal;

/**
 * @author liuhanning
 * @date  2018年8月19日 下午1:01:32
 * @Description 微信计算
 * @version 
 */
public class WXCalculation {

	/**
	 * 费率
	 */
	public static final BigDecimal FEE_RATE_OF_WX = BigDecimal.valueOf(0.006);
	/**
	 * 手续费计算  小数四舍五入
	 */
	public static final int DECIMAL_CALCULATION = BigDecimal.ROUND_HALF_UP;
	/**
	 * 保留小数位
	 */
	public static final int DECIMAL_NUMBER= 2;
	
	
}
