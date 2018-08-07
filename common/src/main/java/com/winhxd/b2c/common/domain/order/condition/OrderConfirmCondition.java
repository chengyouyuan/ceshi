package com.winhxd.b2c.common.domain.order.condition;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;

public class OrderConfirmCondition {

    @ApiModelProperty(value = "订单编号", required = true)
    private String orderNo;
    
    @ApiModelProperty(value = "订单金额", required = false)
    private BigDecimal orderTotal;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public BigDecimal getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(BigDecimal orderTotal) {
        this.orderTotal = orderTotal;
    }

    @Override
    public String toString() {
        return "OrderConfirmCondition [orderNo=" + orderNo + ", orderTotal=" + orderTotal + "]";
    }
}
