package com.winhxd.b2c.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.winhxd.b2c.common.feign.pay.DownLoadStatementClient;

@Component
public class DownLoadStatementTask {
    private static final Logger logger = LoggerFactory.getLogger(DownLoadStatementTask.class);
 
//    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
 
    @Autowired
    DownLoadStatementClient downLoadStatementClient;
    
//    /**
//     * 每隔5秒执行, 单位：ms。
//     */
//    @Scheduled(fixedRate = 5000)
//    public void testFixRate() {
//        System.out.println("我每隔5秒冒泡一次：" + dateFormat.format(new Date()));
//    }
    
    /**
    "0/5 * *  * * ?"   每5秒触发
    "0 0 12 * * ?"    每天中午十二点触发
    "0 15 10 ? * *"    每天早上10：15触发
    "0 15 10 * * ?"    每天早上10：15触发
    "0 15 10 * * ? *"    每天早上10：15触发
    "0 15 10 * * ? 2005"    2005年的每天早上10：15触发
    "0 * 14 * * ?"    每天从下午2点开始到2点59分每分钟一次触发
    "0 0/5 14 * * ?"    每天从下午2点开始到2：55分结束每5分钟一次触发
    "0 0/5 14,18 * * ?"    每天的下午2点至2：55和6点至6点55分两个时间段内每5分钟一次触发
    "0 0-5 14 * * ?"    每天14:00至14:05每分钟一次触发
    "0 10,44 14 ? 3 WED"    三月的每周三的14：10和14：44触发
    "0 15 10 ? * MON-FRI"    每个周一、周二、周三、周四、周五的10：15触发
    */
//    @Scheduled(cron = "0 0 10 * * ?")    //每天上午10点执行
    @Scheduled(cron = "0 35 10 * * ?")    //每天上午10点执行
    public void downLoadStatement() {
        System.out.println("我每天上午10点开始执行");
        try {
//        	ApiCondition condition = new ApiCondition();
        	String statementResult = downLoadStatementClient.downloadStatement().getData();
        	String fundFlowResult = downLoadStatementClient.downloadFundFlow().getData();
            //定时任务可以做耗时操作，包括做生成数据库报表、文件IO等等需要定时执行的逻辑
            if ("SUCCESS".equals(statementResult)) {
                System.out.println("对账单下载完成！");
            }
            if ("SUCCESS".equals(fundFlowResult)) {
            	System.out.println("资金账单下载完成！");
            }

        } catch (Exception ex) {
        	logger.error("对账单下载失败！()", ex.toString());
        }
    }
    
    
    
    
}