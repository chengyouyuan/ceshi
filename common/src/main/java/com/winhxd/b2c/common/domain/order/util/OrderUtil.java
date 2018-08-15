package com.winhxd.b2c.common.domain.order.util;

import java.text.MessageFormat;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;

import com.winhxd.b2c.common.constant.CacheName;
import org.apache.commons.lang3.StringUtils;

public class OrderUtil {
    
    private OrderUtil(){
        
    }

    public static final String getStoreOrderSalesSummaryKey(long storeId) {
        String storeSalesSummary = CacheName.CACHE_KEY_STORE_ORDER_SALESSUMMARY + "{0}";
        return MessageFormat.format(storeSalesSummary, storeId);
    }
    
    public static final String getStoreOrderSalesSummaryField(long storeId, Date startDateTime, Date endDateTime) {
        if (startDateTime == null) {
            startDateTime = new Date();
        }
        if (endDateTime == null) {
            endDateTime = new Date();
        }
        String storeSalesSummaryField = "{0}:{1}:{2}";
        String pattern = "yyyyMMddHHmmss";
        return MessageFormat.format(storeSalesSummaryField, storeId, DateFormatUtils.format(startDateTime, pattern), DateFormatUtils.format(endDateTime, pattern));
    }

    /**
     * 获取手机号后四位，如果未空或者小于4位，返回空字符串
     *
     * @param mobile
     * @return
     */
    public static String getLast4Mobile(String mobile) {
        String mobileStr = "";
        if (StringUtils.isNotBlank(mobile) && mobile.length() > 4) {
            mobileStr = mobile.substring(mobile.length() - 4, mobile.length());
        }
        return mobileStr;
    }
}
