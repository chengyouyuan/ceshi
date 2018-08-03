package com.winhxd.b2c.common.domain.order.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author: wangbaokuo
 * @date: 2018/8/2 19:16
 * @description: 购物车
 */
@ApiModel("购物车")
@Data
public class ShopCar {
    @ApiModelProperty(value = "购物车ID")
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long customerId;

    @ApiModelProperty(value = "门店ID")
    private Long storeId;

    @ApiModelProperty(value = "商品sku")
    private String skuCode;

    @ApiModelProperty(value = "商品数量")
    private Integer prodNum;

    @ApiModelProperty(value = "创建时间")
    private Date created;

    @ApiModelProperty(value = "修改时间")
    private Date updated;

    @ApiModelProperty(value = "创建人")
    private Long createdby;

    @ApiModelProperty(value = "更新人")
    private Long updatedby;

}