package com.winhxd.b2c.common.mq.event;

import com.winhxd.b2c.common.domain.order.model.OrderInfo;

import java.util.Objects;

/**
 * 事件类型
 *
 * @author lixiaodong
 */
public enum EventType {
    /**
     * 事件类型定义
     */
    EVENT_CUSTOMER_ORDER_CREATED(OrderInfo.class);

    private Class<?> eventObjectClass;

    EventType(Class<?> eventObjectClass) {
        Objects.requireNonNull(eventObjectClass);
        this.eventObjectClass = eventObjectClass;
    }

    public Class<?> getEventObjectClass() {
        return eventObjectClass;
    }
}