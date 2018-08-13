package com.winhxd.b2c.common.domain.order.condition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author caiyulong
 */
@ApiModel("获取指定sku的购物车数量")
@Data
public class ShopCartProductCondition {
    @ApiModelProperty(value = "门店ID")
    private Long storeId;

    @ApiModelProperty(value = "商品sku集合")
    private List<String> skuCodes;

    @ApiModelProperty(value = "用户id")
    private Long customerId;
}
