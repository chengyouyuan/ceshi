package com.winhxd.b2c.pay.weixin.base.dto;

/**
 * @author lizhonghua
 * @date  2018年8月14日11:09:35
 * @Description 微信退款
 * @version 
 */
public class PayRefundDTO {

	/**
	 * 公众账号ID
	 */
	private String appid;

	/**
	 * 商户号
	 */
	private String mchId;

	/**
	 * 商户订单号
	 */
	private String outTradeNo;

	/**
	 * 微信订单号
	 */
	private String transactionId;

	/**
	 * 订单金额
	 */
	private String totalFee;

	/**
	 * 退款金额
	 */
	private String refundFee;

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}

	public String getRefundFee() {
		return refundFee;
	}

	public void setRefundFee(String refundFee) {
		this.refundFee = refundFee;
	}
}
