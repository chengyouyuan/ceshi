package com.winhxd.b2c.pay.weixin.model;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 微信对账单
 * @author yuluyuan
 *
 * 2018年8月18日
 */
public class PayStatement {
	
	/**
	 * 主键
	 */
    private Long id;

	/**
	 * 交易时间
	 */
    private Date payTime;

	/**
	 * 公众号ID
	 */
    private String appid;

	/**
	 * 商户号
	 */
    private String mchId;

	/**
	 * 子商户号
	 */
    private String subMchId;

	/**
	 * 设备号
	 */
    private String deviceId;

	/**
	 * 微信订单号
	 */
    private String wxOrderNo;

	/**
	 * 商户订单号
	 */
    private String outOrderNo;

	/**
	 * 用户标识
	 */
    private String userIdentity;

	/**
	 * 交易类型
	 */
    private String payType;

	/**
	 * 交易状态
	 */
    private String payStatus;

	/**
	 * 付款银行
	 */
    private String bankType;

	/**
	 * 货币种类
	 */
    private String currencyType;

	/**
	 * 总金额
	 */
    private BigDecimal totalAmount;

	/**
	 * 代金券或立减优惠金额
	 */
    private BigDecimal discountAmount;

	/**
	 * 退款申请时间
	 */
    private Date refundStartTime;

	/**
	 * 退款成功时间
	 */
    private Date refundSuccessTime;

	/**
	 * 微信退款单号
	 */
    private String refundWxOrderNo;

	/**
	 * 商户退款单号
	 */
    private String refundOutOrderNo;

	/**
	 * 退款金额
	 */
    private BigDecimal refundAmount;

	/**
	 * 代金券或立减优惠退款金额
	 */
    private BigDecimal refundDiscountAmount;

	/**
	 * 退款类型
	 */
    private String refundType;

	/**
	 * 退款状态
	 */
    private String refundStatus;

	/**
	 * 商品名称
	 */
    private String prodName;

	/**
	 * 商户数据包
	 */
    private String mchData;

	/**
	 * 手续费
	 */
    private BigDecimal fee;

	/**
	 * 费率
	 */
    private Float rate;

	/**
	 * 对账单日期
	 */
    private Date billDate;

	/**
	 * 对账单统计表id
	 */
    private Long statementCountId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

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

    public String getSubMchId() {
        return subMchId;
    }

    public void setSubMchId(String subMchId) {
        this.subMchId = subMchId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getWxOrderNo() {
        return wxOrderNo;
    }

    public void setWxOrderNo(String wxOrderNo) {
        this.wxOrderNo = wxOrderNo;
    }

    public String getOutOrderNo() {
        return outOrderNo;
    }

    public void setOutOrderNo(String outOrderNo) {
        this.outOrderNo = outOrderNo;
    }

    public String getUserIdentity() {
        return userIdentity;
    }

    public void setUserIdentity(String userIdentity) {
        this.userIdentity = userIdentity;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

	public Date getrefundStartTime() {
		return refundStartTime;
	}

	public void setrefundStartTime(Date refundStartTime) {
		this.refundStartTime = refundStartTime;
	}

    public Date getRefundSuccessTime() {
        return refundSuccessTime;
    }

    public void setRefundSuccessTime(Date refundSuccessTime) {
        this.refundSuccessTime = refundSuccessTime;
    }

    public String getRefundWxOrderNo() {
        return refundWxOrderNo;
    }

    public void setRefundWxOrderNo(String refundWxOrderNo) {
        this.refundWxOrderNo = refundWxOrderNo;
    }

    public String getRefundOutOrderNo() {
        return refundOutOrderNo;
    }

    public void setRefundOutOrderNo(String refundOutOrderNo) {
        this.refundOutOrderNo = refundOutOrderNo;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public BigDecimal getRefundDiscountAmount() {
        return refundDiscountAmount;
    }

    public void setRefundDiscountAmount(BigDecimal refundDiscountAmount) {
        this.refundDiscountAmount = refundDiscountAmount;
    }

    public String getRefundType() {
        return refundType;
    }

    public void setRefundType(String refundType) {
        this.refundType = refundType;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getMchData() {
        return mchData;
    }

    public void setMchData(String mchData) {
        this.mchData = mchData;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public Long getStatementCountId() {
        return statementCountId;
    }

    public void setStatementCountId(Long statementCountId) {
        this.statementCountId = statementCountId;
    }
}