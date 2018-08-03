package com.winhxd.b2c.common.domain.order.condition;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author pangjianhua
 * @date 2018/8/3 14:08
 */
@Data
public class OrderCancelCondition {
    @ApiModelProperty(value = "取消类型 1:为门店取消订单;2:未顾客取消订单;", required = true)
    private short type;
    @ApiModelProperty(value = "订单编号", required = true)
    private String orderNO;
}
