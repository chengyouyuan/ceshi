package com.winhxd.b2c.common.domain.promotion.model;

public class CouponActivityStoreCustomer {
    private Long id;

    private Long couponActivityTemplateId;

    private Long storeId;

    private Long customerId;

    private Short status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCouponActivityTemplateId() {
        return couponActivityTemplateId;
    }

    public void setCouponActivityTemplateId(Long couponActivityTemplateId) {
        this.couponActivityTemplateId = couponActivityTemplateId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }
}