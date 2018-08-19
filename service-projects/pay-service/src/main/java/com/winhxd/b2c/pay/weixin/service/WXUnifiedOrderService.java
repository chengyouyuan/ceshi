package com.winhxd.b2c.pay.weixin.service;

import com.winhxd.b2c.common.domain.pay.condition.PayPreOrderCondition;
import com.winhxd.b2c.common.domain.pay.vo.PayPreOrderVO;
import com.winhxd.b2c.pay.weixin.base.dto.PayPreOrderCallbackDTO;
import com.winhxd.b2c.pay.weixin.model.PayBill;

/**
 * 支付网关微信统一下单API
 * @author mahongliang
 * @date  2018年8月15日 下午2:10:12
 * @Description 
 * @version
 */
public interface WXUnifiedOrderService {
	
	/**
	 * 支付网关微信统一下单API
	 * @author mahongliang
	 * @date  2018年8月19日 下午4:18:40
	 * @Description 
	 * @param condition
	 * @return
	 */
	PayPreOrderVO unifiedOrder(PayPreOrderCondition condition);

	/**
	 * 回调更新流水
	 * @author mahongliang
	 * @date  2018年8月19日 下午4:19:07
	 * @Description 
	 * @param payPreOrderCallbackDTO
	 * @return
	 */
	PayBill updatePayBillByOutTradeNo(PayPreOrderCallbackDTO payPreOrderCallbackDTO);
	
}
