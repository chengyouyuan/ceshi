package com.winhxd.b2c.common.domain.pay.condition;

import lombok.Data;

@Data
public class UpdateOrderCondition {
	
	private String type;// 1订单支付  2订单退款
	
	private String orderNo;//订单号

}
