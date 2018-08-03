package com.winhxd.b2c.order.service;

import com.winhxd.b2c.common.domain.order.condition.OrderCreateCondition;
import com.winhxd.b2c.common.domain.order.model.OrderInfo;

public interface OrderHandler {

    OrderInfo generateOrderInfo(OrderCreateCondition orderCreateCondition);
}
