package com.winhxd.b2c.common.domain.pay.model;

import java.math.BigDecimal;
import java.util.Date;

public class StoreBankroll {
    private Long id;

    private Long storeId;

    private BigDecimal totalMoeny;

    private BigDecimal presentedMoney;

    private BigDecimal presentedFrozenMoney;

    private BigDecimal settlementSettledMoney;

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