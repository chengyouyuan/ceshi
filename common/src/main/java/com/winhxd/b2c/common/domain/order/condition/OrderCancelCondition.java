package com.winhxd.b2c.common.domain.order.condition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author pangjianhua
 * @date 2018/8/3 14:08
 */
@Data
@ApiModel("取消订单入参")
public class OrderCancelCondition {
    @ApiModelProperty(value = "取消类型 1:门店取消订单;2:顾客取消订单;", required = true,example ="1")
    private short type;
    @ApiModelProperty(value = "订单编号", required = true)
    private String orderNo;
    @ApiModelProperty(value = "取消原因")
    private String cancelReason;
}
