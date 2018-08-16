package com.winhxd.b2c.common.mq.event;

import com.winhxd.b2c.common.mq.event.support.EventCorrelationData;
import com.winhxd.b2c.common.mq.event.support.EventMessageHelper;
import com.winhxd.b2c.common.mq.event.support.EventMessageSenderServiceFactory;
import com.winhxd.b2c.common.mq.event.support.EventSenderServiceNotFoundException;
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
    private EventMessageSenderServiceFactory eventMessageSenderServiceFactory;


    /**
     * 发送事件消息,使用该方法,项目中必须实现
     * {@link EventTypeService}
     *
     * @param eventType
     * @param eventId
     * @param eventObject
     * @param <T>
     * @throws EventSenderServiceNotFoundException
     */
    public <T> void send(EventType eventType, String eventId, T eventObject) throws EventSenderServiceNotFoundException {
        EventTypeService<T> service = eventMessageSenderServiceFactory.getService(eventType);
        if (service == null) {
            throw new EventSenderServiceNotFoundException(eventType.toString());
        }
        if (!eventType.getEventObjectClass().isInstance(eventObject)) {
            throw new IllegalArgumentException("eventObject必须为" + eventType.getEventObjectClass().getCanonicalName());
        }
        service.saveEvent(eventId, eventObject);
        String json = EventMessageHelper.toJson(eventId, eventObject);
        rabbitTemplate.convertAndSend(eventType.toString(), null,
                json, new EventCorrelationData(eventId, eventType));
    }
}
