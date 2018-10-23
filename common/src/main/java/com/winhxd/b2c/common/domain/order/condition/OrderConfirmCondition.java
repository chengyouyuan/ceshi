package com.winhxd.b2c.common.domain.order.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderConfirmCondition extends ApiCondition {

    @ApiModelProperty(value = "订单编号", required = true)
    private String orderNo;

    @ApiModelProperty(value = "订单金额", required = false)
    private BigDecimal orderTotal;

    @ApiModelProperty(value = "门店Id，不需要传", required = false)
    private Long storeId;
    
}
