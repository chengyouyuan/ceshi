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
    @ApiModelProperty(value = "门店ID", required = true)
    private Long storeId;

    @ApiModelProperty(value = "商品sku", required = true)
    private String skuCode;

    @ApiModelProperty(value = "商品数量", required = true)
    private Integer amount;

    @ApiModelProperty(value = "商品单价", required = false)
    private BigDecimal price;
}
