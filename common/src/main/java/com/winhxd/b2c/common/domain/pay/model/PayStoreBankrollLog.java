package com.winhxd.b2c.common.domain.pay.model;

import java.math.BigDecimal;
import java.util.Date;

public class PayStoreBankrollLog {
    private Long id;

    private Long storeId;

    private String orderNo;

    private String withdrawalsNo;

    private BigDecimal totalMoeny;

    private BigDecimal presentedMoney;

    private BigDecimal presentedFrozenMoney;

    private BigDecimal settlementSettledMoney;

    private Short status;

    private String remarks;

    private Long createdBy;

    private String createdByName;

    private Date created;

    private Long updatedBy;

    private String updatedByName;

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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getWithdrawalsNo() {
        return withdrawalsNo;
    }

    public void setWithdrawalsNo(String withdrawalsNo) {
        this.withdrawalsNo = withdrawalsNo;
    }

    public BigDecimal getTotalMoeny() {
        return totalMoeny;
    }

    public void setTotalMoeny(BigDecimal totalMoeny) {
        this.totalMoeny = totalMoeny;
    }

    public BigDecimal getPresentedMoney() {
        return presentedMoney;
    }

    public void setPresentedMoney(BigDecimal presentedMoney) {
        this.presentedMoney = presentedMoney;
    }

    public BigDecimal getPresentedFrozenMoney() {
        return presentedFrozenMoney;
    }

    public void setPresentedFrozenMoney(BigDecimal presentedFrozenMoney) {
        this.presentedFrozenMoney = presentedFrozenMoney;
    }

    public BigDecimal getSettlementSettledMoney() {
        return settlementSettledMoney;
    }

    public void setSettlementSettledMoney(BigDecimal settlementSettledMoney) {
        this.settlementSettledMoney = settlementSettledMoney;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
        this.updatedByName = updatedByName;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}