package com.winhxd.b2c.common.domain.shopcar.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: wangbaokuo
 * @date: 2018/8/3 10:12
 * @description:
 */
@ApiModel("购物车")
@Data
public class ShopCarVO {

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

    @ApiModelProperty(value = "库存状态(0无货,1有货)")
    private Byte prodRepertory;

    @ApiModelProperty(value = "上下架状态(0下架,1上架)")
    private Byte prodStatus;

    @ApiModelProperty(value = "商品图片")
    private String prodImg;

    @ApiModelProperty(value = "商品名称")
    private String prodName;

    @ApiModelProperty(value = "商品单价")
    private BigDecimal prodPrice;
}
