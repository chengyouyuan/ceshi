package com.winhxd.b2c.common.domain.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author caiyulong
 */
@ApiModel("购物车商品数量信息")
@Data
public class ShopCartProductVO {

    @ApiModelProperty(value = "商品SKU")
    private String skuCode;

    @ApiModelProperty(value = "商品数量")
    private Integer skuNum;

    @ApiModelProperty(value = "用户ID")
    private Long customerId;

    @ApiModelProperty(value = "门店ID")
    private Long storeId;

}
