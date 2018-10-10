package com.winhxd.b2c.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;

/**
 * @author  sunwenwu
 * @Date  2018年9月27日
 */
public class DateDealUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(DateDealUtils.class);


    public static Date getStartDate(Date startDate){
        Date date = null;
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            date = calendar.getTime();
        }catch (Exception e){
            LOGGER.info("---开始日期转换异常："+e);
        }
        return date;
    }

    public static Date getEndDate(Date endDate){
        Date date = null;
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endDate);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 59);
            date = calendar.getTime();
        }catch (Exception e){
            LOGGER.info("---结束日期转换异常："+e);
        }
        return date;
    }

    public static Date getEndDate(Date endDate,Integer effectiveDays){
        Date date = null;
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endDate);
            calendar.add(Calendar.DATE, effectiveDays);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 59);
            date = calendar.getTime();
        }catch (Exception e){
            LOGGER.info("---结束日期转换异常："+e);
        }
        return date;
    }
}
