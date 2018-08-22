package com.winhxd.b2c.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.winhxd.b2c.common.domain.pay.condition.DownloadStatementCondition;
import com.winhxd.b2c.common.domain.pay.model.PayStatementDownloadRecord;
import com.winhxd.b2c.common.feign.pay.DownLoadStatementClient;
import com.winhxd.b2c.common.mq.event.EventMessageSender;
import com.winhxd.b2c.common.mq.event.EventType;

/**
 * 下载对账单、资金账单定时任务
 * @author yuluyuan
 *
 * 2018年8月20日
 */
@Component
public class DownLoadStatementTask {
    private static final Logger logger = LoggerFactory.getLogger(DownLoadStatementTask.class);
 
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 
    @Autowired
    DownLoadStatementClient downLoadStatementClient;

    @Autowired
    private EventMessageSender eventMessageSender;
    
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
    
    
    ******每天上午10点执行********
    */
    @Scheduled(cron = "0 0 10 * * ?")
	public void downLoadStatement() {
		logger.info("开始下载账单，当前时间={}", sdf.format(new Date()));
		try {
			DownloadStatementCondition condition = new DownloadStatementCondition();
			condition.setBillDate(DateUtils.addDays(new Date(), -1));
			condition.setAccountType(DownloadStatementCondition.SourceType.BASIC.getText());
			condition.setStatementType(DownloadStatementCondition.StatementType.ALL.getText());

			logger.info("发送下载账单事件开始-日期={}", dateFormat.format(condition.getBillDate()));
			eventMessageSender.send(EventType.EVENT_DOWNLOAD_BILL, dateFormat.format(condition.getBillDate()), condition);
			logger.info("发送下载账单事件结束-日期={}", dateFormat.format(condition.getBillDate()));

		} catch (Exception ex) {
			logger.error("发送下载账单事件失败！", ex);
		}
	}
    /**
     * 每小时检查之前失败过的账单，并重新下载
     * @Description 
     * @author yuluyuan
     * @date 2018年8月22日 下午4:27:54
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void reDownLoadStatement() {
		logger.info("开始检查之前失败的账单，当前时间={}", sdf.format(new Date()));
    	try {
    		PayStatementDownloadRecord record = new PayStatementDownloadRecord();
    		//查询7天内所有失败过的账单
    		record.setStatus(PayStatementDownloadRecord.RecordStatus.FAIL.getCode());
    		record.setBillDate1(DateUtils.addDays(new Date(), -7));
			List<PayStatementDownloadRecord> list = downLoadStatementClient.findDownloadRecord(record).getData();
			if (CollectionUtils.isNotEmpty(list)) {
				for (int i = 0; i < list.size(); i++) {
					Date billDate = list.get(i).getBillDate();

					DownloadStatementCondition condition = new DownloadStatementCondition();
					condition.setBillDate(billDate);
					condition.setAccountType(DownloadStatementCondition.SourceType.BASIC.getText());
					condition.setStatementType(DownloadStatementCondition.StatementType.ALL.getText());

					logger.info("发送下载账单事件开始-日期={}", dateFormat.format(condition.getBillDate()));
					eventMessageSender.send(EventType.EVENT_DOWNLOAD_BILL, dateFormat.format(condition.getBillDate()), condition);
					logger.info("发送下载账单事件结束-日期={}", dateFormat.format(condition.getBillDate()));

				}
			}
    	} catch (Exception ex) {
    		logger.error("发送下载账单事件失败！", ex);
    	}
    }

}