package com.winhxd.b2c.common.domain.order.condition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author pangjianhua
 * @date 2018/8/3 14:29
 */
@Data
@ApiModel("门店处理退款入参")
public class OrderRefundStoreHandleCondition {
    @ApiModelProperty(value = "订单编码", required = true)
    private String orderNo;
    @ApiModelProperty(value = "是否同意 1为同意，0为不同意", required = true)
    private Short agree;
}
