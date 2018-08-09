package com.winhxd.b2c.common.constant;

/**
 * @author lixiaodong
 * 缓存前缀常量
 */
public class CacheName {

    public static final String CACHE_KEY_USER_TOKEN = "USER:TOKEN:";
    /**
     * 订单门店提货码锁KEY
     */
    public static final String CACHE_KEY_STORE_PICK_UP_CODE_GENERATE = "CACHE:KEY:STORE:PICK:UP:CODE:GENERATE:";
    /**
     * 订单门店提货码队列KEY
     */
    public static final String CACHE_KEY_STORE_PICK_UP_CODE_QUEUE = "CACHE:KEY:STORE:PICK:UP:CODE:QUEUE:";
    /**
     * 订单修改锁KEY 全局唯一，订单修改统一用这个
     */
    public static final String CACHE_KEY_MODIFY_ORDER = "CACHE:KEY:MODIFY:ORDER:";
    /**
     * 订单修改锁KEY 全局唯一，订单修改统一用这个
     */
    public static final String CACHE_KEY_STORE_ORDER_SALESSUMMARY = "STORE:SALESSUMMARY:";
    /**
     * B端用户登录key
     */
    public static final String STORE_USER_INFO_TOKEN = "STORE_USER_INFO_TOKEN:";
    /**
     * C端用户登录key
     */
    public static final String CUSTOMER_USER_INFO_TOKEN = "CUSTOMER_USER_INFO_TOKEN:";
    /**
     * 用户确认订单防频繁操作
     */
    public static final String CACHE_KEY_CUSTOMER_ORDER_REPEAT = "CACHE_KEY_CUSTOMER_ORDER_REPEAT";
    /**
     * C端用户验证码key
     */
    public static final String CUSTOMER_USER_SEND_VERIFICATION_CODE = "CUSTOMER:USER:SEND:VERIFICATION:CODE:";
    /**
     * B端用户验证码key
     */
    public static final String STORE_USER_SEND_VERIFICATION_CODE = "STORE:USER:SEND:VERIFICATION:CODE:";
    /**
     * B端C端发送验证码请求key
     */
    public static final String SEND_VERIFICATION_CODE_REQUEST_TIME = "SEND:VERIFICATION:CODE:REQUEST:TIME:";
    /**
     * B端购买过商品sku key
     */
    public static final String STORE_BUYED_HXDPROD_SKU = "STORE:BUYED:HXDPROD:SKU:";
}
