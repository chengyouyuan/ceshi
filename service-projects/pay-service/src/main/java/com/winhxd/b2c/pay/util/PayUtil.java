package com.winhxd.b2c.pay.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.winhxd.b2c.common.constant.PayNotifyMsg;
import com.winhxd.b2c.common.domain.message.condition.NeteaseMsg;
import com.winhxd.b2c.common.domain.message.condition.NeteaseMsgCondition;
import com.winhxd.b2c.common.domain.message.enums.MsgCategoryEnum;
import com.winhxd.b2c.common.domain.message.enums.MsgPageTypeEnum;
import com.winhxd.b2c.common.util.MessageSendUtils;

/**
 * 提现Util工具类
 * @author zhanghuan
 * @date  2018年8月23日 下午12:54:48
 * @version 
 */
public class PayUtil {

    private static final Logger logger = LoggerFactory.getLogger(PayUtil.class);

    private PayUtil() {

    }
    /**
     * 生成云信消息对象
     *
     * @param storeId
     * @param storeMsg
     * @param createdBy
     * @param expiration
     * @param msgType
     * @param pageType
     * @param audioType
     * @return
     * @author wangbin
     * @date 2018年8月16日 下午5:24:49
     * @Description
     */
    public static NeteaseMsgCondition genNeteaseMsgCondition(Long storeId, String storeMsg, String createdBy, int expiration,
            int msgType, short pageType, short categoryType, int audioType) {
		NeteaseMsgCondition neteaseMsgCondition = new NeteaseMsgCondition();
		neteaseMsgCondition.setCustomerId(storeId);
		NeteaseMsg neteaseMsg = new NeteaseMsg();
		neteaseMsg.setMsgContent(storeMsg);
		neteaseMsg.setAudioType(audioType);
		neteaseMsg.setPageType(pageType);
		neteaseMsg.setMsgCategory(categoryType);
		neteaseMsg.setMsgType(msgType);
		neteaseMsg.setCreatedBy(createdBy);
		neteaseMsg.setExpiration(expiration);
		neteaseMsgCondition.setNeteaseMsg(neteaseMsg);
		return neteaseMsgCondition;
}

    /**
     * 门店提现成功云信消息发放
     *
     * @param messageServiceClient
     * @param storeId
     * @author zhanghuan
     * @date 2018年8月16日 下午6:27:59
     */
    public static void newOrderSendMsg2Pay(MessageSendUtils messageServiceClient, Long storeId) {
        try {
            // 发送云信
            String storeMsg = PayNotifyMsg.STORE_APPLY_WITHDRWAL;
            String createdBy = "";
            int expiration = 0;
            int msgType = 0;
            int audioType = 0;
            short pageType = MsgPageTypeEnum.NOTICE.getPageType();
            short categoryType = MsgCategoryEnum.WITHDRAW_APPLY.getTypeCode();
            NeteaseMsgCondition neteaseMsgCondition = PayUtil.genNeteaseMsgCondition(storeId, storeMsg, createdBy, expiration, msgType,
                    pageType, categoryType, audioType);
            messageServiceClient.sendNeteaseMsg(neteaseMsgCondition);
            messageServiceClient.sendNeteaseMsg(neteaseMsgCondition);
        } catch (Exception e) {
            logger.error("门店提现发送消息:storeId={},发送消息：{} 失败", storeId, PayNotifyMsg.STORE_APPLY_WITHDRWAL);
            logger.error("门店提现发送消息失败：", e);
        }
    }
}
