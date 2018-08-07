package com.winhxd.b2c.common.constant;

import java.text.MessageFormat;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;

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

}
