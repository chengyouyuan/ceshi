package com.winhxd.b2c.pay.weixin.base.dto;

import lombok.Data;

/**
 * 主动查询支付结果参数
 * @author mahongliang
 * @date  2018年8月20日 上午11:15:29
 * @Description 
 * @version
 */
@Data
public class PayOrderQueryDTO extends RequestBase {
	/**
	 * 支付流水号
	 */
	private String outTradeNo;

}
