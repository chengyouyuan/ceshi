package com.winhxd.b2c.common.mq.event;

/**
 * 事件处理对象
 *
 * @author lixiaodong
 */
public enum EventTypeHandler {
    /**
     * 事件类型定义
     */
    EVENT_CUSTOMER_ORDER_CREATED_HANDLER(EventType.EVENT_CUSTOMER_ORDER_CREATED);

    private EventType eventType;

    EventTypeHandler(EventType eventType) {
        this.eventType = eventType;
    }

    public EventType getEventType() {
        return eventType;
    }
}
