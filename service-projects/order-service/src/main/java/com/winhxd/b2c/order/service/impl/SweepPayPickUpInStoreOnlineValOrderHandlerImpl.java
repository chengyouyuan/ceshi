package com.winhxd.b2c.order.service.impl;

import org.springframework.stereotype.Service;

import com.winhxd.b2c.common.domain.order.condition.OrderCreateCondition;
import com.winhxd.b2c.common.domain.order.model.OrderInfo;
import com.winhxd.b2c.order.service.OrderHandler;

/**
 * 扫码支付线上计价自提订单处理接口
 * @author wangbin
 * @date  2018年8月3日 上午9:36:33
 * @version 
 */
@Service("SweepPayPickUpInStoreOnlineValOrderHandler")
public class SweepPayPickUpInStoreOnlineValOrderHandlerImpl implements OrderHandler {

    @Override
    public void orderInfoBeforeCreateProcess(OrderInfo orderInfo) {
        if (orderInfo == null) {
            throw new NullPointerException("orderInfo不能为空");
        }
    }

}
