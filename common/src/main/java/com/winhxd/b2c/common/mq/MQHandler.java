package com.winhxd.b2c.common.mq;

/**
 * MQ topic常量
 * 需要支持延时的队列, 构造函数请用 delayed=true
 *
 * @author lixiaodong
 */
public enum MQHandler {
    /**
     *
     */
    NETEASE_MESSAGE_HANDLER(MQDestination.NETEASE_MESSAGE),

    SMS_MESSAGE_HANDLER(MQDestination.SMS_MESSAGE),
    /**
     * 订单超时未接单延时消息
     */
    ORDER_RECEIVE_TIMEOUT_DELAYED_HANDLER(MQDestination.ORDER_RECEIVE_TIMEOUT_DELAYED),
    /**
     * 订单超时未自提延时消息
     */
    ORDER_PICKUP_TIMEOUT_DELAYED_HANDLER(MQDestination.ORDER_PICKUP_TIMEOUT_DELAYED),

    /**
     * C端申请退款订单剩3天未确认延时消息
     */
    ORDER_REFUND_TIMEOUT_3_DAYS_UNCONFIRMED_HANDLER(MQDestination.ORDER_REFUND_TIMEOUT_3_DAYS_UNCONFIRMED),
    /**
     * C端申请退款订单剩1天未确认延时消息
     */
    ORDER_REFUND_TIME_OUT_1_DAY_UNCONFIRMED_HANDLER(MQDestination.ORDER_REFUND_TIME_OUT_1_DAY_UNCONFIRMED),
    /**
     * C端申请退款订单剩1小时未确认延时消息
     */
    ORDER_REFUND_TIME_OUT_1_HOUR_UNCONFIRMED_HANDLER(MQDestination.ORDER_REFUND_TIME_OUT_1_HOUR_UNCONFIRMED);

    private MQDestination destination;

    MQHandler(MQDestination destination) {
        this.destination = destination;
    }

    public MQDestination getDestination() {
        return destination;
    }
}
