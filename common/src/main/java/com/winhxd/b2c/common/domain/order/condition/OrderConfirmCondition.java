package com.winhxd.b2c.common.domain.order.condition;

import java.math.BigDecimal;

import com.winhxd.b2c.common.domain.common.ApiCondition;
import io.swagger.annotations.ApiModelProperty;

public class OrderConfirmCondition extends ApiCondition {

    @ApiModelProperty(value = "订单编号", required = true)
    private String orderNo;
    
    @ApiModelProperty(value = "订单金额", required = false)
    private BigDecimal orderTotal;
    
    @ApiModelProperty(value = "门店Id，不需要传", required = false)
    private Long storeId;

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

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    @Override
    public String toString() {
        return "OrderConfirmCondition [orderNo=" + orderNo + ", orderTotal=" + orderTotal + ", storeId=" + storeId
                + "]";
    }
}
