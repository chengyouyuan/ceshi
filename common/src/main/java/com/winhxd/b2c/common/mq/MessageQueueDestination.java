package com.winhxd.b2c.common.mq;

/**
 * MQ topic常量
 * 需要支持延时的队列, 构造函数请用 delayed=true
 *
 * @author lixiaodong
 */
public enum MessageQueueDestination {
    /**
     *
     */
    NETEASE_MESSAGE,
    NETEASE_MESSAGE_DELAYED(true),
    SMS_MESSAGE;

    private boolean delayed;

    MessageQueueDestination() {
        this(false);
    }

    MessageQueueDestination(boolean delayed) {
        this.delayed = delayed;
    }

    public boolean isDelayed() {
        return delayed;
    }
}
