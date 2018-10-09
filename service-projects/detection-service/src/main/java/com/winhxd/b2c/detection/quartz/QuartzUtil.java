package com.winhxd.b2c.detection.quartz;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.impl.triggers.CronTriggerImpl;

import java.text.ParseException;
import java.util.Date;

public class QuartzUtil {

    private static Logger logger = LogManager.getLogger(QuartzUtil.class);

    /**     
     * 判断cron时间表达式正确性     
     * @param cronExpression     
     * @return      
     */     
    public static boolean isValidExpression(final String cronExpression) {
        CronTriggerImpl trigger = new CronTriggerImpl();
        try {
            trigger.setCronExpression(cronExpression);
            Date date = trigger.computeFirstFireTime(null);
            return date !=null && date.after(new Date());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /*     
     * 任务运行状态     
     */     
    public enum QUARTZ_STATE{
        NONE("NONE","未知"),     
        NORMAL("NORMAL", "正常运行"),     
        PAUSED("PAUSED", "暂停状态"),      
        COMPLETE("COMPLETE",""),     
        ERROR("ERROR","错误状态"),     
        BLOCKED("BLOCKED","锁定状态"); 

        private String index;       
        private String name;       

        private QUARTZ_STATE(String index, String name) {
            this.name = name;        
            this.index = index; 
        }

        public String getIndex() {
            return index;
        }

        public String getName() {
            return name;
        }
    }

}