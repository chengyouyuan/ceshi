package com.winhxd.b2c.common.domain.order.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author pangjianhua
 * @date 2018/8/13 16:28
 */
@Data
public class OrderListItemForCustomerVO {
    @ApiModelProperty(value = "商品图片URL", required = true)
    private String skuUrl;
    @ApiModelProperty(value = "商品名称", required = true)
    private String skuDesc;
}
