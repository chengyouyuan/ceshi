package com.winhxd.b2c.pay.weixin.base.dto;

/**
 * @author lizhonghua
 * @date  2018年8月14日11:09:35
 * @Description 微信退款
 * @version 
 */
public class PayRefundDTO {

	/**
	 * 公众号ID/小程序ID
	 */
	private String appid;

	/**
	 * 商户号
	 */
	private String mchId;

	/**
	 * 随机字符串
	 */
	private String nonceStr;

	/**
	 * 签名
	 */
	private String sign;

	/**
	 * (非必填)签名类型
	 */
	private String singType;

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

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSingType() {
		return singType;
	}

	public void setSingType(String singType) {
		this.singType = singType;
	}

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