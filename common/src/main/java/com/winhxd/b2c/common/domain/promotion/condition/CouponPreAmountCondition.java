package com.winhxd.b2c.common.domain.promotion.condition;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @Auther wangxiaoshun
 * @Date 2018/8/9 19 49
 * @Description 计算优惠券最终优惠金额
 */
public class CouponPreAmountCondition {
    @ApiModelProperty(value = "优惠券发放id", required=true)
    private List<Long> sendIds;
    @ApiModelProperty(value = "商品信息", required=true)
    private List<CouponProductCondition> products;

    public List<Long> getSendIds() {
        return sendIds;
    }

    public void setSendIds(List<Long> sendIds) {
        this.sendIds = sendIds;
    }

    public List<CouponProductCondition> getProducts() {
        return products;
    }

    public void setProducts(List<CouponProductCondition> products) {
        this.products = products;
    }
}
