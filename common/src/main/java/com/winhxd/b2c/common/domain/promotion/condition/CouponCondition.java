package com.winhxd.b2c.common.domain.promotion.condition;

import com.winhxd.b2c.common.domain.base.condition.BaseCondition;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Auther wangxiaoshun
 * @Date 2018/8/6 10 56
 * @Description
 */
public class CouponCondition  extends BaseCondition {
    @ApiModelProperty(value = "用户id", required=true)
    private Long customerId;
    @ApiModelProperty(value = "优惠券使用状态 1 已使用 2 未使用 3 无效 4 已过期 5退回", required=true)
    private Integer useStatus;
    @ApiModelProperty(value = "优惠券发放id", required=true)
    private List<Long> sendIds;
    @ApiModelProperty(value = "订单号", required=true)
    private String orderNo;
    @ApiModelProperty(value = "优惠金额", required=true)
    private BigDecimal couponPrice;
    @ApiModelProperty(value = "订单总额(优惠之前)", required=true)
    private BigDecimal orderPrice;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Integer getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(Integer useStatus) {
        this.useStatus = useStatus;
    }

    public List<Long> getSendIds() {
        return sendIds;
    }

    public void setSendIds(List<Long> sendIds) {
        this.sendIds = sendIds;
    }

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
}
