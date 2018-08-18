package com.winhxd.b2c.pay.weixin.service;

import com.winhxd.b2c.common.domain.pay.condition.PayPreOrderCondition;
import com.winhxd.b2c.common.domain.pay.vo.PayPreOrderVO;

/**
 * 支付网关微信统一下单API
 * @author mahongliang
 * @date  2018年8月15日 下午2:10:12
 * @Description 
 * @version
 */
public interface WXUnifiedOrderService {
	
	PayPreOrderVO unifiedOrder(PayPreOrderCondition condition);

}
