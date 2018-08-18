package com.winhxd.b2c.common.domain.promotion.model;

public class CouponApplyProductList {
    private Long id;

    private Long applyProductId;

    private String brandCode;

    private String skuCode;

    private Short status;

    private String prodId;

    private String productName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApplyProductId() {
        return applyProductId;
    }

    public void setApplyProductId(Long applyProductId) {
        this.applyProductId = applyProductId;
    }

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}