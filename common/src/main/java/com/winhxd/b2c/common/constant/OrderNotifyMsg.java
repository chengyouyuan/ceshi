package com.winhxd.b2c.common.constant;

/**
 * 订单相关 通知文案
 * @author wangbin
 * @date  2018年8月3日 下午5:18:19
 * @version 
 */
public class OrderNotifyMsg {
    
    public static final String DATE_TIME_PARTTEN = "yyyy-MM-dd HH:mm:ss";
    
    public static final String NEW_ORDER_NOTIFY_MSG_4_STORE = "【新订单】您有一笔新的惠小店订单";
    
    public static final String WAIT_PICKUP_ORDER_NOTIFY_MSG_4_STORE = "【待自提】手机尾号{0}订单已付款待自提";
    
    public static final String WAIT_PICKUP_ORDER_NOTIFY_MSG_4_CUSTOMER = "您要购买的商品店主已经知道啦，正在为您备货，请于{0}到店取货";
    
    public static final String ORDER_COMPLETE_MSG_4_STORE = "【已完成】 手机尾号{0}订单已完成";
    
    public static final String ORDER_COMPLETE_MSG_4_CUSTOMER = "您购买的商品已成功取货";
    
    public static final String ORDER_RECEIVE_TIMEOUT_MSG_4_CUSTOMER = "小店没有接收到订单，再试一次吧";

    public static final String ORDER_PICKUP_ALREADY_PAID_TIMEOUT_MSG_4_CUSTOMER = "您未在限定时间内取货，订单已取消，订单金额已退回至您的付款账户，请注意查收";
    
    public static final String ORDER_PICKUP_UNPAID_TIMEOUT_MSG_4_CUSTOMER = "您未在限定时间内取货，订单已取消";
    
    public static final String ORDER_ITEM_TITLE_4_PAYMENT = "{0}等{1}件商品";
}
