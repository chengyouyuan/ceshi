package com.winhxd.b2c.order.service;

import com.winhxd.b2c.common.domain.order.condition.OrderCancelCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderConfirmCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderCreateCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderPickupCondition;
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
     * 门店处理用户退款订单
     *
     * @param condition 入参
     * @return 是否成功，true成功，false 不成功
     */
    void handleOrderRefundByStore(OrderRefundStoreHandleCondition condition);


    /**
     * C端申请退款
     *
     * @param orderRefundCondition
     */
    void orderRefundByCustomer(OrderRefundCondition orderRefundCondition);

    /**
     * 门店接单
     *
     * @param condition
     * @author wangbin
     * @date 2018年8月7日 下午4:41:43
     */
    void orderConfirm4Store(OrderConfirmCondition condition);

    /**
     * 订单超时未接单接口
     *
     * @param orderNo 入参
     * @author wangbin
     * @date 2018年8月2日 下午5:51:46
     */
    void orderReceiveTimeOut(String orderNo);

    /**
     * 订单超时未自提接口
     *
     * @param orderNo 入参
     * @author wangbin
     * @date 2018年8月2日 下午5:51:46
     */
    void orderPickupTimeOut(String orderNo);

    /**
     * 订单提货
     *
     * @param condition
     * @author wangbin
     * @date 2018年8月8日 下午5:24:30
     */
    void orderPickup4Store(OrderPickupCondition condition);

    /**
     * B端取消订单
     *
     * @param orderCancelCondition 入参
     */
    void cancelOrderByStore(OrderCancelCondition orderCancelCondition);

    /**
     * C端取消订单
     *
     * @param orderCancelCondition 入参
     */
    void cancelOrderByCustomer(OrderCancelCondition orderCancelCondition);

    /**
     * C端申请退款订单3天未确认
     *
     * @param orderNo 订单号
     */
    void orderRefundTimeOut3DaysUnconfirmed(String orderNo);

    /**
     * C端申请退款订单1天未确认
     *
     * @param orderNo 订单号
     */
    void orderRefundTimeOut1DayUnconfirmed(String orderNo);

    /**
     * C端申请退款订单1小时未确认
     *
     * @param orderNo 订单号
     */
    void orderRefundTimeOut1HourUnconfirmed(String orderNo);
}
