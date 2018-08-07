package com.winhxd.b2c.common.domain.order.util;

import java.text.MessageFormat;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;

import com.winhxd.b2c.common.constant.CacheName;

public class OrderUtil {
    
    private OrderUtil(){
        
    }

    public static final String getStoreOrderSalesSummaryKey(long storeId, Date startDateTime, Date endDateTime) {
        if (startDateTime == null) {
            startDateTime = new Date();
        }
        if (endDateTime == null) {
            endDateTime = new Date();
        }
        String storeSalesSummary = CacheName.CACHE_KEY_STORE_ORDER_SALESSUMMARY + "{0}:{1}:{2}";
        String pattern = "yyyyMMddHHmmss";
        return MessageFormat.format(storeSalesSummary, storeId, DateFormatUtils.format(startDateTime, pattern), DateFormatUtils.format(endDateTime, pattern));
    }
}
