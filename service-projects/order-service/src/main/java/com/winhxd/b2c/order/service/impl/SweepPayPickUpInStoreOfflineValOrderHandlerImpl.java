package com.winhxd.b2c.order.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.winhxd.b2c.common.domain.order.condition.OrderCreateCondition;
import com.winhxd.b2c.common.domain.order.model.OrderInfo;
import com.winhxd.b2c.order.service.OrderHandler;

/**
 * 扫码支付线下计价自提订单处理接口
 * @author wangbin
 * @date  2018年8月3日 上午9:36:33
 * @version 
 */
@Service("SweepPayPickUpInStoreOfflineValOrderHandler")
public class SweepPayPickUpInStoreOfflineValOrderHandlerImpl implements OrderHandler {

    private static final Logger logger = LoggerFactory.getLogger(SweepPayPickUpInStoreOfflineValOrderHandlerImpl.class);
    
    @Override
    public void orderInfoBeforeCreateProcess(OrderInfo orderInfo) {
        if (orderInfo == null) {
            throw new NullPointerException("orderInfo不能为空");
        }
    }

    @Override
    public void orderInfoAfterCreateProcess(OrderInfo orderInfo) {
        logger.info("");
        
    }

}
