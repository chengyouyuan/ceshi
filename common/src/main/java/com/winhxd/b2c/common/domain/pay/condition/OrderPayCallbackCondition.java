package com.winhxd.b2c.common.domain.pay.condition;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderPayCallbackCondition {
	
	@ApiModelProperty("订单号")
	private String orderNo;//订单号
	

}
