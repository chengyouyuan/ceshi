package com.winhxd.b2c.common.domain.promotion.model;

public class CouponApplyBrandList {
    private Long id;

    private Long applyBrandId;

    private String brandCode;

    private Short status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApplyBrandId() {
        return applyBrandId;
    }

    public void setApplyBrandId(Long applyBrandId) {
        this.applyBrandId = applyBrandId;
    }

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }
}