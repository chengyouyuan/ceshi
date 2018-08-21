package com.winhxd.b2c.common.mq;

/**
 * MQ topic常量
 * 需要支持延时的队列, 构造函数请用 delayed=true
 *
 * @author lixiaodong
 */
public enum MQDestination {
    /**
     * 云信消息
     */
    NETEASE_MESSAGE,
    /**
     * 短信消息
     */
    SMS_MESSAGE,
    /**
     * 小程序模板消息
     */
    MINI_TEMPLATE_MESSAGE,
    /**
     * 云信延迟消息（后台消息管理中，有定时推送云信消息功能）
     */
    NETEASE_MESSAGE_DELAY(true),
    /**
     * 订单超时未付款延时消息
     */
    ORDER_PAY_TIMEOUT_DELAYED(true),
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
    ORDER_REFUND_TIMEOUT_1_DAY_UNCONFIRMED(true),
    /**
     * C端申请退款订单剩1小时未确认延时消息
     */
    ORDER_REFUND_TIMEOUT_1_HOUR_UNCONFIRMED(true);

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
