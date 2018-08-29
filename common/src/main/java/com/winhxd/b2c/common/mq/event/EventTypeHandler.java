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
     * 订单取消，删除订单费用明细(物理删除)
     */
    ACCOUNTING_DETAIL_CANCEL_HANDLER(EventType.EVENT_CUSTOMER_ORDER_CANCEL),
    
    /**
     * 订单闭环统计商品销售量信息
     */
    EVENT_STATISTICS_PROD_HANDLER(EventType.EVENT_CUSTOMER_ORDER_FINISHED),

    /**
     * 订单闭环，计算门店资金
     */
    PAY_STORE_BANKROLL_HANDLER(EventType.EVENT_CUSTOMER_ORDER_FINISHED),

    /**
     * 订单取消退优惠券
     */
    EVENT_CUSTOMER_ORDER_UNTREAD_COUPON_HANDLER(EventType.EVENT_CUSTOMER_ORDER_CANCEL),

    /**
     * 订单取消退款
     */
    EVENT_CUSTOMER_ORDER_REFUND_HANDLER(EventType.EVENT_CUSTOMER_ORDER_CANCEL),
    
    /**
     * 订单取消退款订单销售信息汇总计算处理
     */
    EVENT_CUSTOMER_ORDER_REFUND_SALES_SUMMERY_HANDLER(EventType.EVENT_CUSTOMER_ORDER_CANCEL),
    
    /**
     * 下载对账单
     */
    EVENT_DOWNLOAD_STATEMENT_HANDLER(EventType.EVENT_DOWNLOAD_BILL),
    
    /**
     * 下载资金账单
     */
    EVENT_DOWNLOAD_FINANCIAL_BILL_HANDLER(EventType.EVENT_DOWNLOAD_BILL);
   

    private EventType eventType;

    EventTypeHandler(EventType eventType) {
        this.eventType = eventType;
    }

    public EventType getEventType() {
        return eventType;
    }
}
