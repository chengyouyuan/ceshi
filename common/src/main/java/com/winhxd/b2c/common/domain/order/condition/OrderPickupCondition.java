package com.winhxd.b2c.common.domain.order.condition;


import com.winhxd.b2c.common.domain.common.ApiCondition;
import io.swagger.annotations.ApiModelProperty;

public class OrderPickupCondition extends ApiCondition {

    @ApiModelProperty(value = "订单编号", required = true)
    private String orderNo;
    
    @ApiModelProperty(value = "订单自提码", required = true)
    private String pickupCode;
    
    @ApiModelProperty(value = "门店编码，前端不需要传入", required = true)
    private Long storeId;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPickupCode() {
        return pickupCode;
    }

    public void setPickupCode(String pickupCode) {
        this.pickupCode = pickupCode;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    @Override
    public String toString() {
        return "OrderPickupCondition [orderNo=" + orderNo + ", pickupCode=" + pickupCode + ", storeId=" + storeId + "]";
    }
}
