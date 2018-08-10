package com.winhxd.b2c.common.mq;

import com.winhxd.b2c.common.util.JsonUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author lixiaodong
 */
public class StringMessageSender {
    @Autowired
    private AmqpTemplate amqpTemplate;

    public void send(MQDestination destination, Object message) {
        send(destination, message, null);
    }

    public void send(MQDestination destination, Object message, Integer delayMilliseconds) {
        String msgBody;
        if (message instanceof String) {
            msgBody = (String) message;
        } else {
            msgBody = JsonUtil.toJSONString(message);
        }

        MessagePostProcessor processor = message1 -> {
            message1.getMessageProperties().setDelay(delayMilliseconds);
            return message1;
        };
        amqpTemplate.convertAndSend(destination.toString(), null, msgBody, processor);
    }
}


