package com.winhxd.b2c.common.domain.promotion.model;

public class CouponApplyBrandList {
    private Integer id;

    private Integer applyBrandId;

    private String brandCode;

    private Short status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getApplyBrandId() {
        return applyBrandId;
    }

    public void setApplyBrandId(Integer applyBrandId) {
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