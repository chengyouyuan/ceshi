package com.winhxd.b2c.common.domain.pay.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("订单支付条件对象")
public class OrderPayCondition extends ApiCondition {
  
	@ApiModelProperty("订单号")
	private String orderNo;
	
}