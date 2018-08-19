package com.winhxd.b2c.pay.weixin.base.dto;

import java.io.Serializable;

/**
 * @author lizhonghua
 * @date  2018年8月14日11:09:35
 * @Description 微信退款
 * @version 
 */
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

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getOutRefundNo() {
		return outRefundNo;
	}

	public void setOutRefundNo(String outRefundNo) {
		this.outRefundNo = outRefundNo;
	}

	public Integer getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Integer totalFee) {
		this.totalFee = totalFee;
	}

	public Integer getRefundFee() {
		return refundFee;
	}

	public void setRefundFee(Integer refundFee) {
		this.refundFee = refundFee;
	}

	public String getRefundFeeType() {
		return refundFeeType;
	}

	public void setRefundFeeType(String refundFeeType) {
		this.refundFeeType = refundFeeType;
	}

	public String getRefundDesc() {
		return refundDesc;
	}

	public void setRefundDesc(String refundDesc) {
		this.refundDesc = refundDesc;
	}

	public String getRefundAccount() {
		return refundAccount;
	}

	public void setRefundAccount(String refundAccount) {
		this.refundAccount = refundAccount;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
}