package com.winhxd.b2c.common.constant;

import java.text.MessageFormat;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;

/**
 * 缓存前缀常量
 */
public class CacheName {

    public static final String CACHE_KEY_USER_TOKEN = "USER:TOKEN:";

    
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
