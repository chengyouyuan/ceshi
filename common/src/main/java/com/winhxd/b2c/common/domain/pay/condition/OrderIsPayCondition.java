package com.winhxd.b2c.common.domain.pay.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@ApiModel("订单是否支付")
@Data
public class OrderIsPayCondition extends ApiCondition {

	@ApiModelProperty("订单号")
	private String orderNo;
	
	
}
