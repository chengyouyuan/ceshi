package com.winhxd.b2c.common.mq.event.support;

import com.winhxd.b2c.common.mq.event.EventType;
import org.springframework.amqp.rabbit.support.CorrelationData;

/**
 * @author lixiaodong
 */
public class EventCorrelationData extends CorrelationData {
    private EventType eventType;

    public EventCorrelationData(String id, EventType eventType) {
        super(id);
        this.eventType = eventType;
    }

    public EventType getEventType() {
        return eventType;
    }
}
