package com.winhxd.b2c.common.domain.pay.model;

import java.math.BigDecimal;
import java.util.Date;

public class OrderPayment {
    private Long id;

    private String orderNo;

    private String orderPaymentNo;

    private Date created;

    private Date updated;

    private Date callbackDate;

    private String timeEnd;

    private Short callbackStatus;

    private String callbackReason;

    private String buyerId;

    private String transactionId;

    private BigDecimal realPaymentMoney;

    private BigDecimal callbackMoney;

    private Short payType;

    private BigDecimal cmmsAmt;

    private BigDecimal rate;

    private Short operationType;

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
        this.orderNo = orderNo;
    }

    public String getOrderPaymentNo() {
        return orderPaymentNo;
    }

    public void setOrderPaymentNo(String orderPaymentNo) {
        this.orderPaymentNo = orderPaymentNo;
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

    public Date getCallbackDate() {
        return callbackDate;
    }

    public void setCallbackDate(Date callbackDate) {
        this.callbackDate = callbackDate;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Short getCallbackStatus() {
        return callbackStatus;
    }

    public void setCallbackStatus(Short callbackStatus) {
        this.callbackStatus = callbackStatus;
    }

    public String getCallbackReason() {
        return callbackReason;
    }

    public void setCallbackReason(String callbackReason) {
        this.callbackReason = callbackReason;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public BigDecimal getRealPaymentMoney() {
        return realPaymentMoney;
    }

    public void setRealPaymentMoney(BigDecimal realPaymentMoney) {
        this.realPaymentMoney = realPaymentMoney;
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

    public Short getOperationType() {
        return operationType;
    }

    public void setOperationType(Short operationType) {
        this.operationType = operationType;
    }
}