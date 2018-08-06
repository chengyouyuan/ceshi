package com.winhxd.b2c.order.service;

import com.winhxd.b2c.common.domain.order.condition.OrderCancelCondition;
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
    
    /**
     * 订单支付成功通知接口
     * @author wangbin
     * @date  2018年8月6日 上午10:44:23
     * @param orderNo
     */
    void orderPaySuccessNotify(String orderNo);

    /**
     * 订单取消接口
     * @author pangjianhua
     * @date  2018年8月2日 下午5:51:46
     * @param orderCancelCondition 入参
     * @return true 成功，false不成功
     */
    boolean cancelOrder(OrderCancelCondition orderCancelCondition);
}
