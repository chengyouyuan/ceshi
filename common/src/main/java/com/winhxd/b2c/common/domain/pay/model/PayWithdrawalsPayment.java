package com.winhxd.b2c.common.domain.pay.model;

import java.math.BigDecimal;
import java.util.Date;

public class PayWithdrawalsPayment {
    private Long id;

    private Long storeId;

    private String withdrawalsNo;

    private String withdrawalsTransactionNo;

    private BigDecimal totalFee;

    private Date timeEnd;

    private Short withdrawStatus;

    private String withdrawStatusDesc;

    private String buyerId;

    private String transactionId;

    private BigDecimal callbackMoney;

    private BigDecimal realFee;

    private BigDecimal cmmsAmt;

    private BigDecimal rate;

    private Date created;

    private Date updated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getWithdrawalsNo() {
        return withdrawalsNo;
    }

    public void setWithdrawalsNo(String withdrawalsNo) {
        this.withdrawalsNo = withdrawalsNo;
    }

    public String getWithdrawalsTransactionNo() {
        return withdrawalsTransactionNo;
    }

    public void setWithdrawalsTransactionNo(String withdrawalsTransactionNo) {
        this.withdrawalsTransactionNo = withdrawalsTransactionNo;
    }

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Short getWithdrawStatus() {
        return withdrawStatus;
    }

    public void setWithdrawStatus(Short withdrawStatus) {
        this.withdrawStatus = withdrawStatus;
    }

    public String getWithdrawStatusDesc() {
        return withdrawStatusDesc;
    }

    public void setWithdrawStatusDesc(String withdrawStatusDesc) {
        this.withdrawStatusDesc = withdrawStatusDesc;
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

    public BigDecimal getCallbackMoney() {
        return callbackMoney;
    }

    public void setCallbackMoney(BigDecimal callbackMoney) {
        this.callbackMoney = callbackMoney;
    }

    public BigDecimal getRealFee() {
        return realFee;
    }

    public void setRealFee(BigDecimal realFee) {
        this.realFee = realFee;
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
}