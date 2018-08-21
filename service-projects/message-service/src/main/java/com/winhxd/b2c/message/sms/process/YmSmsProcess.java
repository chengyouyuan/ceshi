package com.winhxd.b2c.message.sms.process;


import cn.emay.sdk.client.api.Client;
import com.winhxd.b2c.common.domain.message.model.MessageSmsHistory;
import com.winhxd.b2c.message.sms.common.SmsConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YmSmsProcess extends BaseSmsProcess {

    private final Logger logger = LoggerFactory.getLogger(YmSmsProcess.class);

    @Override
    public int sendMessage(MessageSmsHistory smsSend) {
        try {
            Client sdkclient = new Client(SmsConstant.getSerialNumber(smsSend.getSupplyId()), SmsConstant.getKey(smsSend.getSupplyId()));
            return sdkclient.sendSMS(new String[]{smsSend.getTelephone()}, smsSend.getContent(), smsSend.getSupplyId(), 3);
        } catch (Exception e) {
            logger.error("初始化短信接口失败", e);
            return -1;
        }
    }
}
