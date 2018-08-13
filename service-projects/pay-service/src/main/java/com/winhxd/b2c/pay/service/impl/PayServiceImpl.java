package com.winhxd.b2c.pay.service.impl;

import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.condition.OrderPayCondition;
import com.winhxd.b2c.common.domain.pay.condition.OrderRefundCondition;
import com.winhxd.b2c.common.domain.pay.condition.UpdateOrderCondition;
import com.winhxd.b2c.common.domain.pay.vo.OrderPayVO;
import com.winhxd.b2c.common.domain.pay.vo.OrderRefundVO;
import com.winhxd.b2c.pay.service.PayService;

public class PayServiceImpl implements PayService{

	@Override
	public ResponseResult<OrderRefundVO> auditRefund(OrderRefundCondition condition) {
		//todo 调取微信退款接口  
		
		return null;
	}

	@Override
	public ResponseResult<String> getprepayId(OrderPayCondition condition) {
		//todo 调取微信的接口
		return null;
	}

	@Override
	public ResponseResult<OrderPayVO> orderPay(OrderPayCondition condition) {
		//todo 调取微信支付接口  
		return null;
	}

	@Override
	public Integer updateOrder(UpdateOrderCondition condition) {
		
		//todo 验证订单状态
		//todo 更新订单状态  
		
		//todo 更新流水号（根据不同的操作  ，更新支付流水或者退款流水）
		return null;
	}

}
