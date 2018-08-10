package com.winhxd.b2c.common.domain.pay.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OutInAccountDetail implements Serializable {
    private Long id;

    private String orderNo;

    private String tradeNo;

    private BigDecimal money;

    private Long storeId;

    private String regionCode;

    private String regionName;

    private Short type;

    private Short outType;

    private Short wechatAccountStatus;

    private Short status;

    private Short dataStatus;

    private Short flowDirectionType;

    private String flowDirectionName;

    private String paymentAccount;

    private BigDecimal cmmsAmt;

    private BigDecimal rate;

    private Date outInDate;

    private Long createdBy;

    private String createdByName;

    private Date created;

    private Long updatedBy;

    private String updatedByName;

    private Date updated;

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

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo == null ? null : tradeNo.trim();
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode == null ? null : regionCode.trim();
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName == null ? null : regionName.trim();
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public Short getOutType() {
        return outType;
    }

    public void setOutType(Short outType) {
        this.outType = outType;
    }

    public Short getWechatAccountStatus() {
        return wechatAccountStatus;
    }

    public void setWechatAccountStatus(Short wechatAccountStatus) {
        this.wechatAccountStatus = wechatAccountStatus;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Short getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Short dataStatus) {
        this.dataStatus = dataStatus;
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
        this.flowDirectionName = flowDirectionName == null ? null : flowDirectionName.trim();
    }

    public String getPaymentAccount() {
        return paymentAccount;
    }

    public void setPaymentAccount(String paymentAccount) {
        this.paymentAccount = paymentAccount == null ? null : paymentAccount.trim();
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

    public Date getOutInDate() {
        return outInDate;
    }

    public void setOutInDate(Date outInDate) {
        this.outInDate = outInDate;
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
        this.createdByName = createdByName == null ? null : createdByName.trim();
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
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
        this.updatedByName = updatedByName == null ? null : updatedByName.trim();
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}