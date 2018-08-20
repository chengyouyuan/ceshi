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
    EVENT_CUSTOMER_ORDER_CREATED_HANDLER(EventType.EVENT_CUSTOMER_ORDER_CREATED),

    /**
     * 订单支付成功，保存费用明细
     */
    ACCOUNTING_DETAIL_SAVE_HANDLER(EventType.EVENT_CUSTOMER_ORDER_PAY_SUCCESS),

    /**
     * 订单闭环，标记费用入账
     */
    ACCOUNTING_DETAIL_RECORDED_HANDLER(EventType.EVENT_CUSTOMER_ORDER_FINISHED),
    /**
     * 订单退优惠券
     */
    EVENT_ORDER_UNTREAD_COUPON_HANDLER(EventType.EVENT_ORDER_UNTREAD_COUPON);

    private EventType eventType;

    EventTypeHandler(EventType eventType) {
        this.eventType = eventType;
    }

    public EventType getEventType() {
        return eventType;
    }
}
