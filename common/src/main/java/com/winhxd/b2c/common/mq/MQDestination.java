package com.winhxd.b2c.common.mq;

/**
 * MQ topic常量
 * 需要支持延时的队列, 构造函数请用 delayed=true
 *
 * @author lixiaodong
 */
public enum MQDestination {
    /**
     *
     */
    NETEASE_MESSAGE,
    SMS_MESSAGE,
    /**
     * 订单超时未接单延时消息
     */
    ORDER_RECEIVE_TIMEOUT_DELAYED(true),
    /**
     * 订单超时未自提延时消息
     */
    ORDER_PICKUP_TIMEOUT_DELAYED(true),
    /**
     * C端申请退款订单剩3天未确认延时消息
     */
    ORDER_REFUND_TIMEOUT_3_DAYS_UNCONFIRMED(true),
    /**
     * C端申请退款订单剩1天未确认延时消息
     */
    ORDER_REFUND_TIME_OUT_1_DAY_UNCONFIRMED(true),
    /**
     * C端申请退款订单剩1小时未确认延时消息
     */
    ORDER_REFUND_TIME_OUT_1_HOUR_UNCONFIRMED(true);

    private boolean delayed;

    MQDestination() {
        this(false);
    }

    MQDestination(boolean delayed) {
        this.delayed = delayed;
    }

    public boolean isDelayed() {
        return delayed;
    }
}
