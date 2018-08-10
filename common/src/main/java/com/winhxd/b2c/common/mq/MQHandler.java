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
    NETEASE_MESSAGE_DELAYED_HANDLER(MQDestination.NETEASE_MESSAGE_DELAYED),

    SMS_MESSAGE_HANDLER(MQDestination.SMS_MESSAGE);

    private MQDestination destination;

    MQHandler(MQDestination destination) {
        this.destination = destination;
    }

    public MQDestination getDestination() {
        return destination;
    }
}