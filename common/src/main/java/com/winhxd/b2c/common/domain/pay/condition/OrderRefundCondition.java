package com.winhxd.b2c.common.domain.pay.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author liuhanning
 * @date  2018年8月13日 下午12:43:38
 * @Description 订单退款
 * @version 
 */
public class OrderRefundCondition extends ApiCondition {
	@ApiModelProperty("订单号")
	private String orderNo;
}
