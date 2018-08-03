package com.winhxd.b2c.common.domain.order.condition;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class OrderCreateCondition {

    @ApiModelProperty(value = "门店id", required=true)
    private Long storeId;
    
    @ApiModelProperty(value = "用户id", required=true)
    private Long customerId;
    
    @ApiModelProperty(value = "支付类型", required=true)
    private Short payType;
    
    @ApiModelProperty(value = "优惠券id", required=false)
    private Long[] couponIds;
    
    @ApiModelProperty(value = "自提时间", required=true)
    private Date pickupDateTime;
    
    @ApiModelProperty(value = "备注", required=false)
    private String remark;
    
    @ApiModelProperty(value = "订单商品明细", required=true)
    private List<OrderDetailCondition> orderDetailConditions;

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Short getPayType() {
        return payType;
    }

    public void setPayType(Short payType) {
        this.payType = payType;
    }

    public Long[] getCouponIds() {
        return couponIds;
    }

    public void setCouponIds(Long[] couponIds) {
        this.couponIds = couponIds;
    }

    public Date getPickupDateTime() {
        return pickupDateTime;
    }

    public void setPickupDateTime(Date pickupDateTime) {
        this.pickupDateTime = pickupDateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<OrderDetailCondition> getOrderDetailConditions() {
        return orderDetailConditions;
    }

    public void setOrderDetailConditions(List<OrderDetailCondition> orderDetailConditions) {
        this.orderDetailConditions = orderDetailConditions;
    }

    @Override
    public String toString() {
        return "OrderCreateCondition [storeId=" + storeId + ", customerId=" + customerId + ", payType=" + payType
                + ", couponIds=" + Arrays.toString(couponIds) + ", pickupDateTime=" + pickupDateTime + ", remark="
                + remark + ", orderDetailConditions=" + orderDetailConditions + "]";
    }
}
