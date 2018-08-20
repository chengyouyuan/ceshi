package com.winhxd.b2c.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.winhxd.b2c.common.domain.pay.model.PayStatementDownloadRecord;
import com.winhxd.b2c.common.feign.pay.DownLoadStatementClient;

@Component
public class DownLoadStatementTask {
    private static final Logger logger = LoggerFactory.getLogger(DownLoadStatementTask.class);
 
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
 
    @Autowired
    DownLoadStatementClient downLoadStatementClient;
    
    /**
     * 每隔5秒执行, 单位：ms。
     */
    @Scheduled(fixedRate = 5000)
    public void testFixRate() {
        System.out.println("我每隔5秒冒泡一次：" + dateFormat.format(new Date()));
    }
    
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
    @Scheduled(cron = "0 52 15 * * ?")    //每天上午10点执行
    public void downLoadStatement() {
        System.out.println("我每天上午10点开始执行");
        try {
        	String statementResult = downLoadStatementClient.downloadStatement(null).getData();
        	String fundFlowResult = downLoadStatementClient.downloadFundFlow(null).getData();
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
    
    @Scheduled(cron = "0 52 15 * * ?")    //每天上午10点执行
    public void ReDownLoadStatement() {
    	System.out.println("啥时候下载之前失败过的账单呢");
    	try {
    		PayStatementDownloadRecord record = new PayStatementDownloadRecord();
    		record.setStatus(PayStatementDownloadRecord.RecordStatus.FAIL.getCode());
			List<PayStatementDownloadRecord> list = downLoadStatementClient.findDownloadRecord(record).getData();
			if (CollectionUtils.isNotEmpty(list)) {
				for (int i = 0; i < list.size(); i++) {
					Date billDate = list.get(i).getBillDate();
					String statementResult = "";
					String fundFlowResult = "";
					if (PayStatementDownloadRecord.BillType.STATEMENT.getCode() == list.get(i).getBillType()) {
						
						statementResult = downLoadStatementClient.downloadStatement(billDate).getData();
						
					}else if(PayStatementDownloadRecord.BillType.FINANCIAL_BILL.getCode() == list.get(i).getBillType()){
						
						fundFlowResult = downLoadStatementClient.downloadFundFlow(billDate).getData();
						
					}
					//定时任务可以做耗时操作，包括做生成数据库报表、文件IO等等需要定时执行的逻辑
					if ("SUCCESS".equals(statementResult)) {
						logger.info("{}对账单下载完成！", billDate);
					}
					if ("SUCCESS".equals(fundFlowResult)) {
						logger.info("{}资金账单下载完成！", billDate);
					}
					
				}
			}
    	} catch (Exception ex) {
    		logger.error("对账单下载失败！()", ex.toString());
    	}
    }

}