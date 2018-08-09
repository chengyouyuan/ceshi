package com.winhxd.b2c.common.domain.promotion.condition;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Auther wangxiaoshun
 * @Date 2018/8/9 15 03
 * @Description
 */
public class OrderUseCouponCondition {

    @ApiModelProperty(value = "订单号", required=true)
    private String orderNo;
    @ApiModelProperty(value = "优惠金额", required=true)
    private BigDecimal couponPrice;
    @ApiModelProperty(value = "订单总额(优惠之前)", required=true)
    private BigDecimal orderPrice;
    @ApiModelProperty(value = "优惠券发放id", required=true)
    private List<Long> sendIds;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public BigDecimal getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(BigDecimal couponPrice) {
        this.couponPrice = couponPrice;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public List<Long> getSendIds() {
        return sendIds;
    }

    public void setSendIds(List<Long> sendIds) {
        this.sendIds = sendIds;
    }
}
