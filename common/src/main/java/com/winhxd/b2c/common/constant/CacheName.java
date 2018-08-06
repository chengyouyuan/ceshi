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
     * 门店提货码锁KEY
     */
    public static final String CACHE_KEY_STORE_PICK_UP_CODE_GENERATE = "CACHE:KEY:STORE:PICK:UP:CODE:GENERATE:";
    /**
     * 门店提货码队列KEY
     */
    public static final String CACHE_KEY_STORE_PICK_UP_CODE_QUEUE = "CACHE:KEY:STORE:PICK:UP:CODE:QUEUE:";

    public static final String getStoreOrderSalesSummaryKey(long storeId, Date startDateTime, Date endDateTime) {
        if (startDateTime == null) {
            startDateTime = new Date();
        }
        if (endDateTime == null) {
            endDateTime = new Date();
        }
        String storeSalesSummary = "STORE:SALESSUMMARY:{0}:{1}:{2}";
        String pattern = "yyyyMMddHHmmss";
        return MessageFormat.format(storeSalesSummary, storeId, DateFormatUtils.format(startDateTime, pattern), DateFormatUtils.format(endDateTime, pattern));
    }
}
