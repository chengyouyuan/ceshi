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
    NETEASE_MESSAGE_DELAYED(true),
    SMS_MESSAGE;

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