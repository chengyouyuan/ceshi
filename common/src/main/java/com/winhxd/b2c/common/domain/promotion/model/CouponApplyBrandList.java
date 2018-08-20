package com.winhxd.b2c.common.domain.promotion.model;

import io.swagger.annotations.ApiModelProperty;

public class CouponApplyBrandList  {
    private Long id;

    private Long applyBrandId;

    private String brandCode;

    private Short status;
    private String companyCode;
    private String companyName;
    private String brandName;

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


    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}