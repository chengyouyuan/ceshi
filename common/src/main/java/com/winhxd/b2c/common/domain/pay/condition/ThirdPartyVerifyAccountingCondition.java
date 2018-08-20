package com.winhxd.b2c.common.domain.pay.condition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel("支付平台与惠下单结算回调条件")
@Data
public class ThirdPartyVerifyAccountingCondition {

    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("订单服务费")
    private BigDecimal serviceFee;
}
