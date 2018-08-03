package com.winhxd.b2c.common.domain.order.condition;


import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;

/**
 * 订单商品明细
 * @author wangbin
 * @date  2018年8月2日 下午5:44:28
 * @version 
 */
public class OrderItemCondition {
    
    @ApiModelProperty(value = "商品id", required=true)
    private String skuCode;

    @ApiModelProperty(value = "商品数量", required=false)
    private Integer amount;

    @ApiModelProperty(value = "商品价格", required=false)
    private BigDecimal price;

    @Override
    public String toString() {
        return "OrderDetailCondition [skuCode=" + skuCode + ", amount=" + amount + ", price=" + price + "]";
    }


    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }


    public String getSkuCode() {
        return skuCode;
    }


    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

}