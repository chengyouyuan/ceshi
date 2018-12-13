package com.winhxd.b2c.common.domain.store.model;

import java.util.Date;

public class StoreCustomerRelationLog {
    private Long id;

    private Long customerId;

    private Long storeId;

    private Long changeStoreId;

    private Date logTime;

    private Integer bindStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getChangeStoreId() {
        return changeStoreId;
    }

    public void setChangeStoreId(Long changeStoreId) {
        this.changeStoreId = changeStoreId;
    }

    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    public Integer getBindStatus() {
        return bindStatus;
    }

    public void setBindStatus(Integer bindStatus) {
        this.bindStatus = bindStatus;
    }
}