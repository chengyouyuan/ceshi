package com.winhxd.b2c.pay.weixin.base.dto;

import java.util.Date;

/**
 * 统一下单入参
 * @author mahongliang
 * @date  2018年8月15日 下午3:29:57
 * @Description 
 * @version
 */
public class PayPreOrderCallbackDTO extends ResponseBase{
	
    /**
     * 支付流水号（商户订单号）
     */
    private String outTradeNo;
    
    /**
     * 订单总金额，单位为分
     */
    private int totalFee;
    
    /**
     * 买家用户标识
     * trade_type=JSAPI时（即公众号支付），此参数必传，此参数为微信用户在商户对应appid下的唯一标识（openid）
     */
    private String openid;
    
    /**
     * 交易类型，小程序取值如下：JSAPI
     */
    private String tradeType;
	
    /**
     * （非必填）附加数据，可作为自定义参数使用
     */
    private String attach;
    
    /**
     * 用户是否关注公众账号，Y-关注，N-未关注，仅在公众账号类型支付有效
     */
    private String isSubscribe;
    
    /**
     * 付款银行
     */
    private String bankType;
    
    /**
     * 应结订单金额，应结订单金额=订单金额-非充值代金券金额，应结订单金额<=订单金额。
     */
    private Integer settlementTotalFee;
    
    /**
     * 货币种类
     */
    private String feeType;
    
    /**
     * 现金支付金额
     */
    private Integer cashFee;
    
    /**
     * 现金支付货币类型
     */
    private String cashFeeType;
    
    /**
     * 总代金券金额
     */
    private Integer couponFee;
    
    /**
     * 代金券使用数量
     */
    private Integer couponCount;
    
    /**
     * 微信支付订单号
     */
    private String transactionId;
    
    /**
     * 支付完成时间
     */
    private Date timeEnd;
    
	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public int getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(int totalFee) {
		this.totalFee = totalFee;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getIsSubscribe() {
		return isSubscribe;
	}

	public void setIsSubscribe(String isSubscribe) {
		this.isSubscribe = isSubscribe;
	}

	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
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

	public Integer getCouponFee() {
		return couponFee;
	}

	public void setCouponFee(Integer couponFee) {
		this.couponFee = couponFee;
	}

	public Integer getCouponCount() {
		return couponCount;
	}

	public void setCouponCount(Integer couponCount) {
		this.couponCount = couponCount;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Date getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(Date timeEnd) {
		this.timeEnd = timeEnd;
	}

}