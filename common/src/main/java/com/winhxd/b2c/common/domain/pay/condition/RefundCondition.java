package com.winhxd.b2c.common.domain.pay.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("退款条件对象")
public class RefundCondition extends ApiCondition {
  
	@ApiModelProperty("订单号")
	private String orderNo;
	
	
}
