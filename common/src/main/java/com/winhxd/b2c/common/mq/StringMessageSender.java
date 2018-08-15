package com.winhxd.b2c.common.mq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author lixiaodong
 */
public class StringMessageSender {
    @Autowired
    @Qualifier("normalRabbitTemplate")
    private AmqpTemplate amqpTemplate;

    public void send(MQDestination destination, String message) {
        send(destination, message, null);
    }

    public void send(MQDestination destination, String message, Integer delayMilliseconds) {
        MessagePostProcessor processor = message1 -> {
            message1.getMessageProperties().setDelay(delayMilliseconds);
            return message1;
        };
        amqpTemplate.convertAndSend(destination.toString(), null, message, processor);
    }
}


