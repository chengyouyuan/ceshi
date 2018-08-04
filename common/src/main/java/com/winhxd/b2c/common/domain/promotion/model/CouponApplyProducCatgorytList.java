package com.winhxd.b2c.common.domain.promotion.model;
/**
 *
 *@Deccription 优惠券适用品类范围
 *@User  wl
 *@Date   2018/8/4 17:45
 */
public class CouponApplyProducCatgorytList {
    /**
     * 主键
     */
    private Integer id;
    /**
     * 优惠券适用对象ID
     */
    private Long applyId;
    /**
     * 品类编码
     */
    private String catCode;
    /**
     *是否有效 0有效 1无效
     */
    private Short status;

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