package com.winhxd.b2c.pay.weixin.base.dto;

import lombok.Data;

/**
 * @author lizhonghua
 * @date  2018年8月14日11:09:35
 * @Description 微信退款
 * @version 
 */
@Data
public class PayRefundDTO extends RequestBase {

	/**
	 * 微信生成的订单号
	 */
	private String transactionId;

	/**
	 * 商户订单号（流水号）
	 */
	private String outTradeNo;

	/**
	 * 商户退款单号（使用流水号）
	 */
	private String outRefundNo;

	/**
	 * 回调订单总金额：分/单位
	 */
	private Integer totalFee;

	/**
	 * 回调退款金额：分/单位
	 */
	private Integer refundFee;

	/**
	 * (非必填)退款货币种类
	 */
	private String refundFeeType;

	/**
	 * (非必填)退款原因
	 */
	private String refundDesc;

	/**
	 * 退款资金来源
	 */
	private String refundAccount;

	/**
	 * 退款结果通知URL
	 */
	private String notifyUrl;

}