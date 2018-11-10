package com.winhxd.b2c.common.domain.order.condition;

import com.winhxd.b2c.common.domain.common.PagedCondition;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author wangbin
 * @date 2018/8/3 17:12
 */
public class OrderQuery4StoreCondition extends PagedCondition {
    
    @ApiModelProperty(value = "订单状态 2:待付款;3:待接单;9:待自提;10:待送货;11:待顾客确认;22:已完成;99:已取消;77:已退款;33:待退款;")
    private Short orderStatus;
    
    @ApiModelProperty(value = "订单提货码")
    private String pickupCode;

    public Short getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Short orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPickupCode() {
        return pickupCode;
    }

    public void setPickupCode(String pickupCode) {
        this.pickupCode = pickupCode;
    }

    @Override
    public String toString() {
        return "OrderQuery4StoreCondition [orderStatus=" + orderStatus + ", pickupCode=" + pickupCode + "]";
    }
    
    
}
