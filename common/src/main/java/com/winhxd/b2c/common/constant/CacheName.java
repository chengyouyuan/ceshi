package com.winhxd.b2c.common.constant;

/**
 * @author lixiaodong
 * 缓存前缀常量
 */
public class CacheName {
    /**
     * 后台管理用户登录key
     */
    public static final String CACHE_KEY_USER_TOKEN = "TOKEN:USER:";
    /**
     * B端用户登录key
     */
    public static final String STORE_USER_INFO_TOKEN = "TOKEN:STORE:";
    /**
     * C端用户登录key
     */
    public static final String CUSTOMER_USER_INFO_TOKEN = "TOKEN:CUSTOMER:";
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
     * 门店订单汇总信息KEY
     */
    public static final String CACHE_KEY_STORE_ORDER_SALESSUMMARY = "STORE:SALESSUMMARY:";
    /**
     * 订单号生成重复redis验证KEY
     */
    public static final String CACHE_KEY_ORDERNO_CHECK_EXISTS = "ORDERNO:CHECK:";
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
    /**
     * JSON模板文件缓存
     */
    public static final String JSON_TEMPLATES = "JSON_TEMPLATES:";

    /**
     * B端绑定银行卡存入验证码 key
     */
    public static final String PAY_VERIFICATION_CODE = "PAY:MOBILE:VERIFICATION:";

    /**
     * 小程序accesstoken
     */
    public static final String MESSAGE_MINI_ACCESS_TOKEN = "MINIPROGRAM:ACCESSTOKEN:";

    /**
     * 事件消息ID,SortedSet
     */
    public static final String EVENT_MESSAGE_ID = "EVENT:MESSAGE:ID:";
    /**
     * 事件消息内容,Hash
     */
    public static final String EVENT_MESSAGE_BODY = "EVENT:MESSAGE:BODY:";
}
