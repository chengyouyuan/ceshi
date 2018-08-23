package com.winhxd.b2c.common.mq.event;

import java.util.Objects;

import com.winhxd.b2c.common.domain.order.model.OrderInfo;
import com.winhxd.b2c.common.domain.pay.condition.DownloadStatementCondition;

/**
 * 事件类型
 *
 * @author lixiaodong
 */
public enum EventType {
    /**
     * 事件类型定义
     */
    EVENT_CUSTOMER_ORDER_CREATED(OrderInfo.class),
    
    /**
     * 用户订单支付成功事件
     */
    EVENT_CUSTOMER_ORDER_PAY_SUCCESS(OrderInfo.class),
    
    /**
     * 用户订单完成事件
     */
    EVENT_CUSTOMER_ORDER_FINISHED(OrderInfo.class),

    /**
     * 订单取消事件
     */
    EVENT_CUSTOMER_ORDER_CANCEL(OrderInfo.class),
    
    /**
     * 下载账单事件
     */
    EVENT_DOWNLOAD_BILL(DownloadStatementCondition.class);

    private Class<?> eventObjectClass;

    EventType(Class<?> eventObjectClass) {
        Objects.requireNonNull(eventObjectClass);
        this.eventObjectClass = eventObjectClass;
    }

    public Class<?> getEventObjectClass() {
        return eventObjectClass;
    }
}
