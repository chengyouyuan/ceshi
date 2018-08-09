package com.winhxd.b2c.common.domain.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: wangbaokuo
 * @date: 2018/8/6 17:58
 */
@ApiModel("购物车商品信息")
@Data
public class ShopCarProdInfoVO {
    @ApiModelProperty(value = "商品SKU", required = true)
    private String skuCode;
    @ApiModelProperty(value = "商品数量", required = true)
    private Integer amount;
    @ApiModelProperty(value = "商品价格，未计价商品有可能为空", required = true)
    private BigDecimal price;
    @ApiModelProperty(value = "商品图片URL", required = true)
    private String prodImg;
    @ApiModelProperty(value = "商品名称", required = true)
    private String prodName;
    @ApiModelProperty("商品状态 0下架1上架2已删除")
    private Short prodStatus;
}
