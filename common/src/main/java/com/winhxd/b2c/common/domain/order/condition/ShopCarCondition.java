package com.winhxd.b2c.common.domain.order.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: wangbaokuo
 * @date: 2018/8/10 09:31
 */
@ApiModel("加购")
@Data
public class ShopCarCondition extends ApiCondition {
    @ApiModelProperty(value = "门店ID")
    private Long storeId;

    @ApiModelProperty(value = "商品sku")
    private String skuCode;

    @ApiModelProperty(value = "商品数量")
    private Integer skuNum;

    @ApiModelProperty(value = "商品单价")
    private BigDecimal price;
}
