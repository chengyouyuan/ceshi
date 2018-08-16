package com.winhxd.b2c.common.mq.event;

import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.mq.event.support.EventCorrelationData;
import com.winhxd.b2c.common.mq.event.support.EventMessageHelper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * 事件消息发送类
 *
 * @author lixiaodong
 */
public class EventMessageSender {
    @Autowired
    @Qualifier("eventRabbitTemplate")
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Cache cache;


    /**
     * 发送事件消息
     *
     * @param eventType
     * @param eventId
     * @param eventObject
     * @param <T>
     */
    public <T> void send(EventType eventType, String eventId, T eventObject) {
        String json = EventMessageHelper.toJson(eventId, eventObject);
        String idKey = CacheName.EVENT_MESSAGE_ID + eventType.toString();
        String bodyKey = CacheName.EVENT_MESSAGE_BODY + eventType.toString();
        cache.zadd(idKey, System.currentTimeMillis(), eventId);
        cache.hset(bodyKey, eventId, json);
        rabbitTemplate.convertAndSend(eventType.toString(), null,
                json, new EventCorrelationData(eventId, eventType));
    }
}
