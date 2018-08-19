package com.winhxd.b2c.common.domain.pay.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PayRefundPayment implements Serializable {
    private Long id;

    private String orderNo;

    private String orderTransactionNo;

    private String refundNo;

    private String refundTransactionNo;

    private Date callbackDate;

    private Short callbackStatus;

    private String refundDesc;

    private String buyerId;

    private String transactionId;

    private BigDecimal refundFee;

    private BigDecimal callbackMoney;

    private Short payType;

    private BigDecimal cmmsAmt;

    private BigDecimal rate;

    private Date created;

    private Date updated;

    private String appid;

    private String mchId;

    private String nonceStr;

    private BigDecimal totalAmount;

    private String callbackErrorCode;

    private String callbackErrorMessage;

    private Date callbackSuccessTime;

    private String callbackRefundRecvAccout;

    private String callbackRefundAccount;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public String getOrderTransactionNo() {
        return orderTransactionNo;
    }

    public void setOrderTransactionNo(String orderTransactionNo) {
        this.orderTransactionNo = orderTransactionNo == null ? null : orderTransactionNo.trim();
    }

    public String getRefundNo() {
        return refundNo;
    }

    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo == null ? null : refundNo.trim();
    }

    public String getRefundTransactionNo() {
        return refundTransactionNo;
    }

    public void setRefundTransactionNo(String refundTransactionNo) {
        this.refundTransactionNo = refundTransactionNo == null ? null : refundTransactionNo.trim();
    }

    public Date getCallbackDate() {
        return callbackDate;
    }

    public void setCallbackDate(Date callbackDate) {
        this.callbackDate = callbackDate;
    }

    public Short getCallbackStatus() {
        return callbackStatus;
    }

    public void setCallbackStatus(Short callbackStatus) {
        this.callbackStatus = callbackStatus;
    }

    public String getRefundDesc() {
        return refundDesc;
    }

    public void setRefundDesc(String refundDesc) {
        this.refundDesc = refundDesc == null ? null : refundDesc.trim();
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId == null ? null : buyerId.trim();
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId == null ? null : transactionId.trim();
    }

    public BigDecimal getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(BigDecimal refundFee) {
        this.refundFee = refundFee;
    }

    public BigDecimal getCallbackMoney() {
        return callbackMoney;
    }

    public void setCallbackMoney(BigDecimal callbackMoney) {
        this.callbackMoney = callbackMoney;
    }

    public Short getPayType() {
        return payType;
    }

    public void setPayType(Short payType) {
        this.payType = payType;
    }

    public BigDecimal getCmmsAmt() {
        return cmmsAmt;
    }

    public void setCmmsAmt(BigDecimal cmmsAmt) {
        this.cmmsAmt = cmmsAmt;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid == null ? null : appid.trim();
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId == null ? null : mchId.trim();
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr == null ? null : nonceStr.trim();
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCallbackErrorCode() {
        return callbackErrorCode;
    }

    public void setCallbackErrorCode(String callbackErrorCode) {
        this.callbackErrorCode = callbackErrorCode == null ? null : callbackErrorCode.trim();
    }

    public String getCallbackErrorMessage() {
        return callbackErrorMessage;
    }

    public void setCallbackErrorMessage(String callbackErrorMessage) {
        this.callbackErrorMessage = callbackErrorMessage == null ? null : callbackErrorMessage.trim();
    }

    public Date getCallbackSuccessTime() {
        return callbackSuccessTime;
    }

    public void setCallbackSuccessTime(Date callbackSuccessTime) {
        this.callbackSuccessTime = callbackSuccessTime;
    }

    public String getCallbackRefundRecvAccout() {
        return callbackRefundRecvAccout;
    }

    public void setCallbackRefundRecvAccout(String callbackRefundRecvAccout) {
        this.callbackRefundRecvAccout = callbackRefundRecvAccout == null ? null : callbackRefundRecvAccout.trim();
    }

    public String getCallbackRefundAccount() {
        return callbackRefundAccount;
    }

    public void setCallbackRefundAccount(String callbackRefundAccount) {
        this.callbackRefundAccount = callbackRefundAccount == null ? null : callbackRefundAccount.trim();
    }
}