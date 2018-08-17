package com.winhxd.b2c.pay.weixin.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static final Logger logger = LoggerFactory.getLogger(WXUnifiedOrderServiceImpl.class);
	
	@Autowired
	private PayBillMapper payBillMapper;

	@Override
	public OrderPayVO unifiedOrder(PayPreOrderCondition condition) {
		//真实订单号
		String outOrderNo = condition.getOutOrderNo();
		Long paidBill =  payBillMapper.selectPaidByOutOrderNo(outOrderNo);
		if(paidBill != null && paidBill.longValue() > 0) {
			logger.warn("订单{}已支付，请不要重复支付", outOrderNo);
			//throw new BusinessException();
		}
		
		return null;
	}

}
