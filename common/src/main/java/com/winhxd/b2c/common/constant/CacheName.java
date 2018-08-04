package com.winhxd.b2c.common.constant;

import java.text.MessageFormat;
import java.util.Date;

/**
 * 缓存前缀常量
 */
public class CacheName {

    public static final String CACHE_KEY_USER_TOKEN = "USER:TOKEN:";

    
    public static final String getStoreOrderSalesSummaryKey(long storeId, Date startDateTime, Date endDateTime) {
        String storeSalesSummary = "STORE:SALESSUMMARY:{0}:{1}:{2}";
        return MessageFormat.format(storeSalesSummary, storeId, startDateTime, endDateTime);
    }
}
