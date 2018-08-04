package com.winhxd.b2c.common.domain.promotion.model;
/**
 *
 *@Deccription  优惠券适用品牌范围
 *@User  wl
 *@Date   2018/8/4 17:48
 */
public class CouponApplyBrandList {
    /**
     * 主键
     */
    private Integer id;
    /**
     * 优惠券适用对象ID
     */
    private Integer applyId;
    /**
     * 品牌编码
     */
    private String brandCode;
    /**
     * 是否有效 0有效1无效
     */
    private Short status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getApplyId() {
        return applyId;
    }

    public void setApplyId(Integer applyId) {
        this.applyId = applyId;
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