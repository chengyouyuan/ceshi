package com.winhxd.b2c.pay.weixin.base.dto;

/**
 * @author lizhonghua
 * @date  2018年8月14日11:09:35
 * @Description 微信退款
 * @version 
 */
public class PayRefundDTO {

	/**
	 * 公众号ID
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
	 * 回调微信退款单号
	 */
	private String refundId;

	/**
	 * 回调退款金额：分/单位
	 */
	private Integer refundFee;

	/**
	 * 回调应结退款总金额：分/单位
	 */
	private Integer settlementRefundFee;

	/**
	 * 回调订单总金额：分/单位
	 */
	private Integer totalFee;

	/**
	 * 回调应结订单总金额：分/单位
	 */
	private Integer settlementTotalFee;

	/**
	 * 回调退款货币种类
	 */
	private String feeType;

	/**
	 * 回调现金支付金额：分/单位
	 */
	private Integer cashFee;

	/**
	 * 回调现金支付币种
	 */
	private String cashFeeType;

	/**
	 * 回调现金退款金额：分/单位
	 */
	private Integer cashRefundFee;

	/**
	 * 退款状态 0退款异常 1退款成功 2退款关闭
	 */
	private String refundStatus;

	/**
	 * 资金退款至用户帐号的时间 YYYY-MM-DD hh:mm:ss
	 */
	private String successTime;

	/**
	 * 退款入账账户   1)退回银行卡:{银行名称}{卡类型}{卡尾号}
	 *               2)退回支付用户零钱:支付用户零钱
	 *               3)退还商户:商户基本账户
	 *               4)退回支付用户零钱通:支付用户零钱通
	 */
	private String refundRecvAccout;

	/**
	 * 1:REFUND_SOURCE_RECHARGE_FUNDS 可用余额退款/基本账户
	 * 2:REFUND_SOURCE_UNSETTLED_FUNDS 未结算资金退款
	 */
	private String refundAccount;

	/**
	 * 1:API接口
	 * 2:VENDOR_PLATFORM商户平台
	 */
	private String refundRequestSource;

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

	public String getRefundId() {
		return refundId;
	}

	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}

	public Integer getRefundFee() {
		return refundFee;
	}

	public void setRefundFee(Integer refundFee) {
		this.refundFee = refundFee;
	}

	public Integer getSettlementRefundFee() {
		return settlementRefundFee;
	}

	public void setSettlementRefundFee(Integer settlementRefundFee) {
		this.settlementRefundFee = settlementRefundFee;
	}

	public Integer getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Integer totalFee) {
		this.totalFee = totalFee;
	}

	public Integer getSettlementTotalFee() {
		return settlementTotalFee;
	}

	public void setSettlementTotalFee(Integer settlementTotalFee) {
		this.settlementTotalFee = settlementTotalFee;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public Integer getCashFee() {
		return cashFee;
	}

	public void setCashFee(Integer cashFee) {
		this.cashFee = cashFee;
	}

	public String getCashFeeType() {
		return cashFeeType;
	}

	public void setCashFeeType(String cashFeeType) {
		this.cashFeeType = cashFeeType;
	}

	public Integer getCashRefundFee() {
		return cashRefundFee;
	}

	public void setCashRefundFee(Integer cashRefundFee) {
		this.cashRefundFee = cashRefundFee;
	}

	public String getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}

	public String getSuccessTime() {
		return successTime;
	}

	public void setSuccessTime(String successTime) {
		this.successTime = successTime;
	}

	public String getRefundRecvAccout() {
		return refundRecvAccout;
	}

	public void setRefundRecvAccout(String refundRecvAccout) {
		this.refundRecvAccout = refundRecvAccout;
	}

	public String getRefundAccount() {
		return refundAccount;
	}

	public void setRefundAccount(String refundAccount) {
		this.refundAccount = refundAccount;
	}

	public String getRefundRequestSource() {
		return refundRequestSource;
	}

	public void setRefundRequestSource(String refundRequestSource) {
		this.refundRequestSource = refundRequestSource;
	}
}
