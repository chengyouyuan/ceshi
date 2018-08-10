package com.winhxd.b2c.common.mq;

import org.springframework.amqp.core.Queue;

/**
 * MQ topic常量
 * 需要支持延时的队列, 构造函数请用 delayed=true
 *
 * @author lixiaodong
 */
public enum MessageQueueHandler {
    /**
     *
     */
    NETEASE_MESSAGE_HANDLER(MessageQueueDestination.NETEASE_MESSAGE),
    NETEASE_MESSAGE_DELAYED_HANDLER(MessageQueueDestination.NETEASE_MESSAGE_DELAYED),

    SMS_MESSAGE_HANDLER(MessageQueueDestination.SMS_MESSAGE);

    private MessageQueueDestination destination;

    MessageQueueHandler(MessageQueueDestination destination) {
        this.destination = destination;
    }

    public MessageQueueDestination getDestination() {
        return destination;
    }

    public Queue getQueue() {
        return new Queue(this.toString(), true, false, false);
    }
}
