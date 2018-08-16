package com.winhxd.b2c.pay.weixin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winhxd.b2c.common.domain.pay.condition.PayPreOrderCondition;
import com.winhxd.b2c.common.domain.pay.vo.OrderPayVO;
import com.winhxd.b2c.pay.weixin.dao.PayBillMapper;
import com.winhxd.b2c.pay.weixin.service.WXUnifiedOrderService;

/**
 * 支付网关微信统一下单API实现
 * @author mahongliang
 * @date  2018年8月15日 下午2:10:57
 * @Description 
 * @version
 */
@Service
public class WXUnifiedOrderServiceImpl implements WXUnifiedOrderService {
	
	@Autowired
	private PayBillMapper payBillMapper;

	@Override
	public OrderPayVO unifiedOrder(PayPreOrderCondition condition) {
		//真实订单号
		String outOrderNo = condition.getOutOrderNo();
		return null;
	}

}
