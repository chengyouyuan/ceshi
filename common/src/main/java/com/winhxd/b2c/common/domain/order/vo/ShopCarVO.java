package com.winhxd.b2c.common.domain.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author: wangbaokuo
 * @date: 2018/8/3 10:12
 * @description:
 */
@ApiModel("购物车")
@Data
public class ShopCarVO {
    @ApiModelProperty(value = "0正常, 1购物车有商品信息下架")
    private Byte prodStatus = 0;
    @ApiModelProperty(value = "购物车商品信息")
    private List<ShopCarProdInfoVO> shopCarts;
}
