package com.winhxd.b2c.pay.weixin.base.dto;

import lombok.Data;

/**
 * 下载对账单入参
 * 
 * @author yuluyuan
 *
 *         2018年8月19日
 */
@Data
public class PayFinancialBillDTO extends RequestBase {

	/**
	 * 对账单日期
	 */
	private String billDate;

	/**
	 * 资金账户类型
	 */
	private String accountType;

	/**
	 * 压缩账单 非必传参数，固定值：GZIP，返回格式为.gzip的压缩包账单。不传则默认为数据流形式。
	 */
	private String tarType;

}
