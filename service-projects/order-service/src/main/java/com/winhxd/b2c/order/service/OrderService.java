package com.winhxd.b2c.order.service;

import com.winhxd.b2c.common.domain.order.condition.OrderCancelCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderCreateCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderRefundCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderRefundStoreHandleCondition;

/**
 * 订单操作接口，提供包括下单、修改等操作
 *
 * @author wangbin
 * @date 2018年8月2日 下午5:48:57
 */
public interface OrderService {

    /**
     * 订单提交接口
     *
     * @param orderCreateCondition
     * @return 订单号
     * @author wangbin
     * @date 2018年8月2日 下午5:51:46
     */
    String submitOrder(OrderCreateCondition orderCreateCondition);

    /**
     * 订单支付成功通知接口
     *
     * @param orderNo
     * @author wangbin
     * @date 2018年8月6日 上午10:44:23
     */
    void orderPaySuccessNotify(String orderNo);

    /**
     * 订单取消接口
     *
     * @param orderCancelCondition 入参
     * @return true 成功，false不成功
     * @throws InterruptedException
     * @author pangjianhua
     * @date 2018年8月2日 下午5:51:46
     */
    boolean cancelOrder(OrderCancelCondition orderCancelCondition) throws InterruptedException;

    /**
     * 门店处理用户退款订单
     *
     * @param condition 入参
     * @return 是否成功，true成功，false 不成功
     */
    void handleOrderRefundByStore(OrderRefundStoreHandleCondition condition);


    /**
     * C端申请退款
     * @param orderRefundCondition
     */
    void orderRefundByCustomer(OrderRefundCondition orderRefundCondition);
}
