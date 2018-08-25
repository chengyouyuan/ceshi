package com.winhxd.b2c.common.constant;

/**
 * 订单相关 通知文案
 * @author wangbin
 * @date  2018年8月3日 下午5:18:19
 * @version 
 */
public class OrderNotifyMsg {
    
    public static final String DATE_TIME_PARTTEN = "yyyy-MM-dd HH:mm:ss";
    
    public static final String NEW_ORDER_NOTIFY_MSG_4_STORE = "您有一笔新的惠小店订单";
    
    public static final String WAIT_PICKUP_ORDER_NOTIFY_MSG_4_STORE = "【待自提】手机尾号{0}订单已付款待自提";
    
    public static final String WAIT_PICKUP_ORDER_NOTIFY_MSG_4_CUSTOMER = "门店已确认接收订单，取货码为{0}，到店提货时请将提货码出示给门店";
    
    public static final String WAIT_PICKUP_OFFLINE_PRICE_ORDER_NOTIFY_MSG_4_CUSTOMER = "您的订单已支付完成，取货码为{0}，到店提货时请将提货码出示给门店";
    
    public static final String ONLINE_PRICE_ORDER_PAY_SUCCESS_NOTIFY_MSG_4_CUSTOMER = "您的订单已支付完成，请等待商家确认";
    
    public static final String OFFLINE_PRICE_ORDER_NEED_PAY_NOTIFY_MSG_4_CUSTOMER = "商店已接收并确认了价格，快去支付吧";
    
    public static final String ORDER_COMPLETE_MSG_4_STORE = "【已完成】 手机尾号{0}订单已完成";
    
    public static final String ORDER_COMPLETE_MSG_4_CUSTOMER = "您购买的商品已成功取货";
    
    public static final String ORDER_RECEIVE_TIMEOUT_MSG_4_CUSTOMER = "小店没有接收到订单，再试一次吧";
    
    public static final String ORDER_PAY_TIMEOUT_MSG_4_CUSTOMER = "您未在限定时间内付款，订单已取消";

    public static final String ORDER_PICKUP_ALREADY_PAID_TIMEOUT_MSG_4_CUSTOMER = "您未在限定时间内取货，订单已取消，订单金额已退回至您的付款账户，请注意查收";
    
    public static final String ORDER_ITEM_TITLE_4_PAYMENT = "{0}等{1}件商品";
}
