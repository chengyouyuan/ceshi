package com.winhxd.b2c.common.mq.event;

import com.winhxd.b2c.common.domain.order.model.OrderInfo;

/**
 * 事件类型
 *
 * @author lixiaodong
 */
public enum EventType {
    /**
     * 事件类型定义
     */
    CUSTOMER_ORDER_CREATED(OrderInfo.class);

    private Class<?> eventObjectClass;

    EventType(Class<?> eventObjectClass) {
        this.eventObjectClass = eventObjectClass;
    }

    public Class<?> getEventObjectClass() {
        return eventObjectClass;
    }
}
