package com.winhxd.b2c.common.mq;

/**
 * MQ topic常量
 * 需要支持延时的队列, 构造函数请用 delayed=true
 *
 * @author lixiaodong
 */
public enum MQHandler {
    /**
     * 云信消息
     */
    NETEASE_MESSAGE_HANDLER(MQDestination.NETEASE_MESSAGE),

    /**
     * 短信
     */
    SMS_MESSAGE_HANDLER(MQDestination.SMS_MESSAGE),
    /**
     * 小程序模板消息
     */
    MINI_TEMPLATE_MESSAGE_HANDLER(MQDestination.MINI_TEMPLATE_MESSAGE),
    /**
     * 云信延迟消息（后台消息管理中，有定时推送云信消息功能）
     */
    NETEASE_MESSAGE_DELAY_HANDLER(MQDestination.NETEASE_MESSAGE_DELAY),
    /**
     * 订单超时未支付
     */
    ORDER_PAY_TIMEOUT_DELAYED_HANDLER(MQDestination.ORDER_PAY_TIMEOUT_DELAYED),
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
    ORDER_REFUND_TIMEOUT_1_DAY_UNCONFIRMED_HANDLER(MQDestination.ORDER_REFUND_TIMEOUT_1_DAY_UNCONFIRMED),
    /**
     * C端申请退款订单剩1小时未确认延时消息
     */
    ORDER_REFUND_TIMEOUT_1_HOUR_UNCONFIRMED_HANDLER(MQDestination.ORDER_REFUND_TIMEOUT_1_HOUR_UNCONFIRMED);

    private MQDestination destination;

    MQHandler(MQDestination destination) {
        this.destination = destination;
    }

    public MQDestination getDestination() {
        return destination;
    }
}
