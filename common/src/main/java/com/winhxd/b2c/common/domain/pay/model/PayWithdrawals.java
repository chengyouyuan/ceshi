package com.winhxd.b2c.common.domain.pay.model;

import java.math.BigDecimal;
import java.util.Date;

public class PayWithdrawals {
    private Long id;

    private Long storeId;

    private String withdrawalsNo;

    private String withdrawalsTransactionNo;

    private BigDecimal totalFee;

    private BigDecimal realFee;

    private BigDecimal cmmsAmt;

    private BigDecimal rate;

    private Short auditStatus;

    private String auditDesc;

    private Short callbackStatus;

    private String callbackReason;

    private String errorMessage;

    private BigDecimal callbackCmmsAmt;

    private Short flowDirectionType;

    private String flowDirectionName;

    private String swiftCode;

    private String name;

    private String mobile;

    private String paymentAccount;

    private Date created;

    private Long createdBy;

    private String createdByName;

    private Long updatedBy;

    private String updatedByName;

    private Date updated;

    private String buyerId;

    private String transactionId;

    private BigDecimal callbackMoney;

    private Date timeEnd;

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

    public Short getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Short auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getAuditDesc() {
        return auditDesc;
    }

    public void setAuditDesc(String auditDesc) {
        this.auditDesc = auditDesc;
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

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public BigDecimal getCallbackCmmsAmt() {
        return callbackCmmsAmt;
    }

    public void setCallbackCmmsAmt(BigDecimal callbackCmmsAmt) {
        this.callbackCmmsAmt = callbackCmmsAmt;
    }

    public Short getFlowDirectionType() {
        return flowDirectionType;
    }

    public void setFlowDirectionType(Short flowDirectionType) {
        this.flowDirectionType = flowDirectionType;
    }

    public String getFlowDirectionName() {
        return flowDirectionName;
    }

    public void setFlowDirectionName(String flowDirectionName) {
        this.flowDirectionName = flowDirectionName;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPaymentAccount() {
        return paymentAccount;
    }

    public void setPaymentAccount(String paymentAccount) {
        this.paymentAccount = paymentAccount;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedByName() {
        return updatedByName;
    }

    public void setUpdatedByName(String updatedByName) {
        this.updatedByName = updatedByName;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
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

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }
}