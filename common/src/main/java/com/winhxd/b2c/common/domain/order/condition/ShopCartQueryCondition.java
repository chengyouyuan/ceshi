package com.winhxd.b2c.common.domain.order.condition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by wangbaokuo on 2018/8/23 18:28
 */
@ApiModel("查询购物车")
@Data
public class ShopCartQueryCondition {
    @ApiModelProperty(value = "门店ID", required = true)
    private Long storeId;
    @ApiModelProperty(value = "用户ID", required = true)
    private Long customerId;
}
