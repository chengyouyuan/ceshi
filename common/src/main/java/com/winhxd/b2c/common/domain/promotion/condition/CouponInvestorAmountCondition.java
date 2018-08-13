package com.winhxd.b2c.common.domain.promotion.condition;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @Auther wangxiaoshun
 * @Date 2018/8/13 16 51
 * @Description
 */
public class CouponInvestorAmountCondition {
    @ApiModelProperty(value = "订单号")
    private List<String> orderNos;

    public List<String> getOrderNos() {
        return orderNos;
    }

    public void setOrderNos(List<String> orderNos) {
        this.orderNos = orderNos;
    }
}
