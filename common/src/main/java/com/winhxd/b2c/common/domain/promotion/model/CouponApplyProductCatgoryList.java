package com.winhxd.b2c.common.domain.promotion.model;

public class CouponApplyProductCatgoryList {
    private Integer id;

    private Long applyProdutCatgoryId;

    private String catCode;

    private Short status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getApplyProdutCatgoryId() {
        return applyProdutCatgoryId;
    }

    public void setApplyProdutCatgoryId(Long applyProdutCatgoryId) {
        this.applyProdutCatgoryId = applyProdutCatgoryId;
    }

    public String getCatCode() {
        return catCode;
    }

    public void setCatCode(String catCode) {
        this.catCode = catCode;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }
}