package com.winhxd.b2c.common.domain.promotion.model;
/**
 *
 *@Deccription 优惠券适用商品范围
 *@User  wl
 *@Date   2018/8/4 17:43
 */
public class CouponApplyProductList {
    /**
     * 主键
     */
    private Integer id;
    /**
     * 优惠券适用对象ID
     */
    private Long applyId;
    /**
     * 品牌编码
     */
    private String brandCode;
    /**
     * 商品SKU
     */
    private String skuCode;
    /**
     * 是否有效 0有效1无效
     */
    private Short status;
    /**
     * 商品ID
     */
    private String prodId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getApplyId() {
        return applyId;
    }

    public void setApplyId(Long applyId) {
        this.applyId = applyId;
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
}