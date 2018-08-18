package com.winhxd.b2c.common.domain.order.util;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.constant.OrderNotifyMsg;
import com.winhxd.b2c.common.domain.message.condition.MiniMsgCondition;
import com.winhxd.b2c.common.domain.message.condition.MiniTemplateData;
import com.winhxd.b2c.common.domain.message.condition.NeteaseMsg;
import com.winhxd.b2c.common.domain.message.condition.NeteaseMsgCondition;
import com.winhxd.b2c.common.domain.message.enums.MiniMsgTypeEnum;
import com.winhxd.b2c.common.domain.order.enums.PickUpTypeEnum;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.message.MessageServiceClient;

public class OrderUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderUtil.class);
    
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
    

    /**
     * 生成云信消息对象
     * @author wangbin
     * @date  2018年8月16日 下午5:24:49
     * @Description 
     * @param storeId
     * @param storeMsg
     * @param createdBy
     * @param expiration
     * @param msgType
     * @param pageType
     * @param audioType
     * @return
     */
    public static NeteaseMsgCondition genNeteaseMsgCondition(Long storeId, String storeMsg, String createdBy, int expiration,
            int msgType, short pageType, int audioType, String treeCode) {
        NeteaseMsgCondition neteaseMsgCondition = new NeteaseMsgCondition();
        neteaseMsgCondition.setCustomerId(storeId);
        NeteaseMsg neteaseMsg = new NeteaseMsg();
        neteaseMsg.setMsgContent(storeMsg);
        neteaseMsg.setAudioType(audioType);
        neteaseMsg.setPageType(pageType);
        neteaseMsg.setMsgType(msgType);
        neteaseMsg.setTreeCode(treeCode);
        neteaseMsg.setCreatedBy(createdBy);
        neteaseMsg.setExpiration(expiration);
        neteaseMsgCondition.setNeteaseMsg(neteaseMsg);
        return neteaseMsgCondition;
    }
    
    public static MiniMsgCondition genMiniMsgCondition(String openid, String page, short msgType2C, MiniTemplateData... datas) {
        List<MiniTemplateData> temData = Arrays.asList(datas);
        MiniMsgCondition miniMsgCondition = new MiniMsgCondition();
        miniMsgCondition.setToUser(openid);
        miniMsgCondition.setPage(page);
        miniMsgCondition.setMsgType(msgType2C);
        miniMsgCondition.setData(temData);
        return miniMsgCondition;
    }
    
    /**
     * 新订单门店云信消息发放
     * @author wangbin
     * @date  2018年8月16日 下午6:27:59
     * @param messageServiceClient
     * @param storeId
     */
    public static void newOrderSendMsg2Store(MessageServiceClient messageServiceClient, Long storeId){
        try {
            // 发送云信
            String storeMsg = OrderNotifyMsg.NEW_ORDER_NOTIFY_MSG_4_STORE;
            String createdBy= "";
            int expiration = 0;
            int msgType = 0;
            short pageType = 1;
            int audioType = 1;
            String treeCode = "treeCode";
            NeteaseMsgCondition neteaseMsgCondition = OrderUtil.genNeteaseMsgCondition(storeId, storeMsg, createdBy, expiration, msgType,
                    pageType, audioType, treeCode);
            if (messageServiceClient.sendNeteaseMsg(neteaseMsgCondition).getCode() != BusinessCode.CODE_OK) {
                throw new BusinessException(BusinessCode.CODE_1001);
            }
        } catch (Exception e) {
            logger.error("客户下单给门店:storeId={},发送消息：{} 失败", storeId, OrderNotifyMsg.NEW_ORDER_NOTIFY_MSG_4_STORE);
            logger.error("客户下单给门店发送消息失败：", e);
        }
    }
    
    /**
     * 订单需要提货给用户发信息
     * @author wangbin
     * @date  2018年8月16日 下午6:32:26
     * @param messageServiceClient
     * @param pickupDateTime
     * @param pickupType
     * @param openid
     */
    public static void orderNeedPickupSendMsg2Customer(MessageServiceClient messageServiceClient, Date pickupDateTime, short pickupType, String openid){
        try {
            pickupDateTime = pickupDateTime == null ? new Date() : pickupDateTime;
            String customerMsg = MessageFormat.format(OrderNotifyMsg.WAIT_PICKUP_ORDER_NOTIFY_MSG_4_CUSTOMER, DateFormatUtils.format(pickupDateTime, OrderNotifyMsg.DATE_TIME_PARTTEN));
            if (pickupType == PickUpTypeEnum.SELF_PICK_UP_NOW.getTypeCode()) {
                customerMsg = OrderNotifyMsg.WAIT_PICKUP_ORDER_NOTIFY_MSG_4_CUSTOMER_PICKUP_NOW;
            }
            String page = null;
            MiniTemplateData data = new MiniTemplateData();
            data.setKeyName("keyword1");
            data.setValue(customerMsg);
            short msgType2C = MiniMsgTypeEnum.STORE_CONFIRM_ORDER.getMsgType();
            MiniMsgCondition miniMsgCondition = OrderUtil.genMiniMsgCondition(openid, page, msgType2C);
            if (messageServiceClient.sendMiniMsg(miniMsgCondition).getCode() != BusinessCode.CODE_OK) {
                throw new BusinessException(BusinessCode.CODE_1001);
            }
        } catch (Exception e) {
            logger.error("提醒用户:openid={},提货发送消息失败", openid);
            logger.error("提醒用户提货发送消息失败：", e);
        }
    }
    
    /**
     * 订单待提货发信息给门店
     * @author wangbin
     * @date  2018年8月16日 下午6:37:26
     * @param messageServiceClient
     * @param last4MobileNums
     * @param storeId
     */
    public static void orderNeedPickupSendMsg2Store(MessageServiceClient messageServiceClient, String last4MobileNums, Long storeId){
        String storeMsg = MessageFormat.format(OrderNotifyMsg.WAIT_PICKUP_ORDER_NOTIFY_MSG_4_STORE, last4MobileNums);
        try {
            // 发送云信
            String createdBy= "";
            int expiration = 0;
            int msgType = 0;
            short pageType = 1;
            int audioType = 0;
            String treeCode = "treeCode";
            NeteaseMsgCondition neteaseMsgCondition = OrderUtil.genNeteaseMsgCondition(storeId, storeMsg, createdBy, expiration, msgType,
                    pageType, audioType, treeCode);
            if (messageServiceClient.sendNeteaseMsg(neteaseMsgCondition).getCode() != BusinessCode.CODE_OK) {
                throw new BusinessException(BusinessCode.CODE_1001);
            }
        } catch (Exception e) {
            logger.error("订单待提货给门店:storeId={},发送消息:{},失败", storeId, storeMsg);
            logger.error("订单待提货给门店发送消息失败：", e);
        }
    }
}
