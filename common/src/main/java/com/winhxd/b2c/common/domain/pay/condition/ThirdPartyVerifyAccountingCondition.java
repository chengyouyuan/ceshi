package com.winhxd.b2c.common.domain.pay.condition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("支付平台与惠下单结算回调条件")
@Data
public class ThirdPartyVerifyAccountingCondition {

    @ApiModelProperty("订单号")
    private String orderNo;
}
