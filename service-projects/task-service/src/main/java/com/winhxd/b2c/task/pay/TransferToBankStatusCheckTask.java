package com.winhxd.b2c.task.pay;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.feign.pay.PayServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * TransferToBankStatusCheckTask
 *
 * @Author yindanqing
 * @Date 2018/8/22 11:35
 * @Description: 转账到银行卡状态确认
 */
@Component
public class TransferToBankStatusCheckTask {

    private static final Logger logger = LoggerFactory.getLogger(TransferToBankStatusCheckTask.class);

    @Autowired
    private PayServiceClient payServiceClient;

    @Scheduled(cron = "0 1/5 * * * ?")
    public void queryAndConfirmTransferToBankStatus() throws Exception {
        ResponseResult<Integer> result = payServiceClient.confirmTransferToBankStatus();
        if(BusinessCode.CODE_1001 == result.getCode()){
            logger.error("PayServiceClient:confirmTransferToBankStatus called failed.");
        } else {
            logger.info("PayServiceClient:confirmTransferToBankStatus called finished, "
                    + result.getData() + " rows updated.");
        }
    }

}
