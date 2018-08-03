package com.winhxd.b2c.order.service;

import com.winhxd.b2c.common.domain.order.condition.OrderCreateCondition;

/**
 * 订单操作接口，提供包括下单、修改等操作
 * @author wangbin
 * @date  2018年8月2日 下午5:48:57
 * @version 
 */
public interface OrderService {

    /**
     * 订单提交接口
     * @author wangbin
     * @date  2018年8月2日 下午5:51:46
     * @param orderCreateCondition
     * @return 订单号
     */
    String submitOrder(OrderCreateCondition orderCreateCondition);
}
