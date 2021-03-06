package com.winhxd.b2c.common.domain.pay.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author wl
 * @Date 2018/8/15 09:54
 * @Description  新建提现记录
 **/
public class PayStoreWithDrawCreateCondition extends ApiCondition {
    @ApiModelProperty("门店id")
    private Long storeId;
    @ApiModelProperty("订单号/提现单号")
    private String orderNo;
    @ApiModelProperty("交易类型 1订单入账 2提现")
    private Short type;
    @ApiModelProperty("交易金额")
    private BigDecimal money;
    @ApiModelProperty("手续费（按照千6计算）")
    private BigDecimal cmmsAmt;
    @ApiModelProperty("费率")
    private BigDecimal rate;
    @ApiModelProperty("真正的手续费")
    private BigDecimal realCmmsAmt;
    @ApiModelProperty("真正的费率")
    private BigDecimal realRate;


    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getCmmsAmt() {
        return cmmsAmt;
    }

    public void setCmmsAmt(BigDecimal cmmsAmt) {
        this.cmmsAmt = cmmsAmt;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getRealCmmsAmt() {
        return realCmmsAmt;
    }

    public void setRealCmmsAmt(BigDecimal realCmmsAmt) {
        this.realCmmsAmt = realCmmsAmt;
    }

    public BigDecimal getRealRate() {
        return realRate;
    }

    public void setRealRate(BigDecimal realRate) {
        this.realRate = realRate;
    }
}
