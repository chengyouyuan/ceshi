package com.winhxd.b2c.pay.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.winhxd.b2c.common.domain.order.model.OrderInfo;
import com.winhxd.b2c.common.mq.event.EventMessageListener;
import com.winhxd.b2c.common.mq.event.EventTypeHandler;
import com.winhxd.b2c.pay.service.PayService;

public class MessageSendMqHandler {
	
	@Autowired
	private PayService payService;
	
    /**
     * @author liuhanning
     * @date  2018年8月23日 下午5:40:23
     * @Description 订单闭环，计算门店资金
     * @param orderNo
     * @param orderInfo
     */
    @EventMessageListener(value = EventTypeHandler.PAY_STORE_BANKROLL_HANDLER, concurrency = "3-6")
    public void orderFinishHandler(String orderNo, OrderInfo orderInfo) {
        payService.orderFinishHandler(orderNo,orderInfo);
    }
    /**
     * 
     * @author liuhanning
     * @date  2018年8月20日 下午4:37:38
     * @Description 退款
     * @param orderNo
     * @param order
     * @return
     */
	@EventMessageListener(value = EventTypeHandler.EVENT_CUSTOMER_ORDER_REFUND_HANDLER)
	public void refundOrder(String orderNo, OrderInfo order)  {
		payService.refundOrder(orderNo,order);
	}

}