package com.winhxd.b2c.common.domain.order.condition;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author pangjianhua
 * @date 2018/8/3 14:29
 */
@Data
public class OrderRefundCondition {
    @ApiModelProperty(value = "订单编号")
    private String orderNo;
    @ApiModelProperty(value = "退款原因")
    private String cancelReason;
}
