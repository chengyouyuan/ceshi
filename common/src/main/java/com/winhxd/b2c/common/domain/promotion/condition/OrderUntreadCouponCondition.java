package com.winhxd.b2c.common.domain.promotion.condition;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Auther wangxiaoshun
 * @Date 2018/8/9 15 06
 * @Description
 */
public class OrderUntreadCouponCondition {
    @ApiModelProperty(value = "订单号", required=true)
    private String orderNo;
    @ApiModelProperty(value = "状态 1-已使用,4-退回", required=true)
    private String status;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
