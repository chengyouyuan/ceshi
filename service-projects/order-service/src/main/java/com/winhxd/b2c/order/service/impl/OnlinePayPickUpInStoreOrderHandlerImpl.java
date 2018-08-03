package com.winhxd.b2c.order.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.winhxd.b2c.common.domain.order.condition.OrderCreateCondition;
import com.winhxd.b2c.common.domain.order.enums.OrderStatusEnum;
import com.winhxd.b2c.common.domain.order.model.OrderInfo;
import com.winhxd.b2c.order.service.OrderHandler;

/**
 * 在线支付自提订单处理接口
 * @author wangbin
 * @date  2018年8月3日 上午9:36:33
 * @version 
 */
@Service("OnlinePayPickUpInStoreOrderHandler")
public class OnlinePayPickUpInStoreOrderHandlerImpl implements OrderHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(OnlinePayPickUpInStoreOrderHandlerImpl.class);

    @Override
    public void orderInfoBeforeCreateProcess(OrderInfo orderInfo) {
        if (orderInfo == null) {
            throw new NullPointerException("orderInfo不能为空");
        }
        orderInfo.setOrderStatus(OrderStatusEnum.SUBMITTED.getStatusCode());
    }

    @Override
    public void orderInfoAfterCreateProcess(OrderInfo orderInfo) {
        logger.info("在线支付自提订单 创建后无处理逻辑");
    }

}
