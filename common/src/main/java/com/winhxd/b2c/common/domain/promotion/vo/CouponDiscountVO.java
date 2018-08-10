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

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }
}
