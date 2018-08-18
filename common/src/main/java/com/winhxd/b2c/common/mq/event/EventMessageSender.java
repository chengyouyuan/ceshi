package com.winhxd.b2c.common.mq.event;

import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.mq.event.support.EventCorrelationData;
import com.winhxd.b2c.common.mq.event.support.EventMessageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Objects;

/**
 * 事件消息发送类
 *
 * @author lixiaodong
 */
public class EventMessageSender {
    private static final Logger logger = LoggerFactory.getLogger(EventMessageSender.class);

    @Autowired
    @Qualifier("eventRabbitTemplate")
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Cache cache;

    /**
     * 发送事件消息
     *
     * @param eventType
     * @param eventKey 请使用业务相关Id,例如:订单相关事件可用订单编号
     * @param eventObject 例如:订单相关事件可用订单对象
     * @param <T>
     */
    public <T> void send(EventType eventType, String eventKey, T eventObject) {
        Objects.requireNonNull(eventKey, "eventId不能为null");
        Objects.requireNonNull(eventObject, "eventObject不能为null");
        String json = EventMessageHelper.toJson(eventKey, eventObject);
        String idKey = CacheName.EVENT_MESSAGE_ID + eventType.toString();
        String bodyKey = CacheName.EVENT_MESSAGE_BODY + eventType.toString();
        cache.zadd(idKey, System.currentTimeMillis(), eventKey);
        cache.hset(bodyKey, eventKey, json);
        logger.info("事件消息发送开始: {} - {}", eventType, eventKey);
        rabbitTemplate.convertAndSend(eventType.toString(), null,
                json, new EventCorrelationData(eventKey, eventType));
    }
}
