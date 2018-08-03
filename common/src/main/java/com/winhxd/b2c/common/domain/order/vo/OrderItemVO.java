package com.winhxd.b2c.common.domain.order.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author pangjianhua
 * @date 2018/8/3 10:20
 */
@Data
public class OrderItemVO {
    @ApiModelProperty(value = "订单编号", required = true)
    private String orderNo;
    @ApiModelProperty(value = "商品SKU", required = true)
    private String skuCode;
    @ApiModelProperty(value = "商品数量", required = true)
    private Integer amount;
    @ApiModelProperty(value = "商品价格，未计价商品有可能为空")
    private BigDecimal price;
    @ApiModelProperty(value = "商品图片URL")
    private String productPictureUrl;
    @ApiModelProperty(value = "商品名称")
    private String productName;
}
