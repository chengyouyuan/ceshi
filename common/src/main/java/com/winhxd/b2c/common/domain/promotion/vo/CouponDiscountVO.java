package com.winhxd.b2c.common.domain.promotion.vo;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * @Auther wangxiaoshun
 * @Date 2018/8/9 20 31
 * @Description
 */
public class CouponDiscountVO {

    @ApiModelProperty(value = "折扣总额", required=true)
    private BigDecimal discountAmount;
    @ApiModelProperty(value = "优惠券标题", required=true)
    private String couponTitle;

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getCouponTitle() {
        return couponTitle;
    }

    public void setCouponTitle(String couponTitle) {
        this.couponTitle = couponTitle;
    }
}
