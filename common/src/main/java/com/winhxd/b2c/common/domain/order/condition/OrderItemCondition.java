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

    @ApiModelProperty(value = "商品名称", required=false)
    private String skuName;
    
    @ApiModelProperty(value = "商品url", required=false)
    private String skuUrl;

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

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSkuUrl() {
        return skuUrl;
    }

    public void setSkuUrl(String skuUrl) {
        this.skuUrl = skuUrl;
    }

    @Override
    public String toString() {
        return "OrderItemCondition [skuCode=" + skuCode + ", amount=" + amount + ", price=" + price + ", skuName="
                + skuName + ", skuUrl=" + skuUrl + "]";
    }

}