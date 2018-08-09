package com.winhxd.b2c.common.domain.promotion.condition;

import com.winhxd.b2c.common.domain.base.condition.BaseCondition;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Auther wangxiaoshun
 * @Date 2018/8/9 15 48
 * @Description
 */
public class OrderCouponCondition{

    @ApiModelProperty(value = "订单号", required=true)
    private String orderNo;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
