package com.winhxd.b2c.order.util;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.winhxd.b2c.common.domain.order.model.OrderItem;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.constant.OrderNotifyMsg;
import com.winhxd.b2c.common.domain.message.condition.MiniMsgCondition;
import com.winhxd.b2c.common.domain.message.condition.MiniTemplateData;
import com.winhxd.b2c.common.domain.message.condition.NeteaseMsg;
import com.winhxd.b2c.common.domain.message.condition.NeteaseMsgCondition;
import com.winhxd.b2c.common.domain.message.enums.MiniMsgTypeEnum;
import com.winhxd.b2c.common.domain.order.enums.PickUpTypeEnum;
import com.winhxd.b2c.common.domain.order.enums.ValuationTypeEnum;
import com.winhxd.b2c.common.util.MessageSendUtils;

public class OrderUtil {

    /**
     * 手机号截取长度
     */
    private static final int MOBILE_SUB_LENGTH = 4;
    private static final Logger logger = LoggerFactory.getLogger(OrderUtil.class);

    private OrderUtil() {

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
     * @author wangbin
     * @date  2018年8月21日 下午3:41:04
     * @Description 
     * @param mobile
     * @return
     */
    public static String getLast4Mobile(String mobile) {
        String mobileStr = "";
        if (StringUtils.isNotBlank(mobile) && mobile.length() > MOBILE_SUB_LENGTH) {
            mobileStr = mobile.substring(mobile.length() - 4, mobile.length());
        }
        return mobileStr;
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

    public static MiniMsgCondition genMiniMsgCondition(String openid, String page, short msgType2C, String emphasisKeyword, MiniTemplateData... datas) {
        List<MiniTemplateData> temData = Arrays.asList(datas);
        MiniMsgCondition miniMsgCondition = new MiniMsgCondition();
        miniMsgCondition.setToUser(openid);
        miniMsgCondition.setPage(page);
        miniMsgCondition.setMsgType(msgType2C);
        miniMsgCondition.setData(temData);
        miniMsgCondition.setEmphasisKeyword(emphasisKeyword);
        return miniMsgCondition;
    }

    public static String genRealPayMoney(BigDecimal money) {
        String result = "￥";
        if (ObjectUtils.allNotNull(money)) {
            result += money.toString();
        } else {
            result += "0";

        }
        return result;
    }

    /**
     * 获取订单详情的描述信息，用于构造消息发送给微信用户
     *
     * @param orderItems
     * @return
     */
    public static String genOrderItemDesc(List<OrderItem> orderItems) {
        String result = "";
        if (CollectionUtils.isNotEmpty(orderItems)) {
            result = orderItems.get(0).getSkuDesc();
            if (orderItems.size() > 1) {
                result += "...";
            }
        }
        return result;
    }

    /**
     * 新订单门店云信消息发放
     *
     * @param messageServiceClient
     * @param storeId
     * @author wangbin
     * @date 2018年8月16日 下午6:27:59
     */
    public static void newOrderSendMsg2Store(MessageSendUtils messageServiceClient, Long storeId) {
        try {
            // 发送云信
            String storeMsg = OrderNotifyMsg.NEW_ORDER_NOTIFY_MSG_4_STORE;
            String createdBy = "";
            int expiration = 0;
            int msgType = 0;
            short pageType = 1;
            int audioType = 1;
            String treeCode = "treeCode";
            NeteaseMsgCondition neteaseMsgCondition = OrderUtil.genNeteaseMsgCondition(storeId, storeMsg, createdBy, expiration, msgType,
                    pageType, audioType, treeCode);
            messageServiceClient.sendNeteaseMsg(neteaseMsgCondition);
        } catch (Exception e) {
            logger.error("客户下单给门店:storeId={},发送消息：{} 失败", storeId, OrderNotifyMsg.NEW_ORDER_NOTIFY_MSG_4_STORE);
            logger.error("客户下单给门店发送消息失败：", e);
        }
    }

    /**
     * 订单需要提货给用户发信息
     *
     * @param messageServiceClient
     * @param pickupDateTime
     * @param valuationType
     * @param openid
     * @author wangbin
     * @date 2018年8月16日 下午6:32:26
     */
    public static void orderNeedPickupSendMsg2Customer(MessageSendUtils messageServiceClient, short valuationType, String openid, String prodTitles, String orderTotal, String realPay, String pickupCode) {
        try {
            String customerMsg;
            short msgType2C;
            String keyword4 = null;
            String emphasisKeyword = null;
            if (valuationType == ValuationTypeEnum.OFFLINE_VALUATION.getTypeCode()) {
                customerMsg = MessageFormat.format(OrderNotifyMsg.WAIT_PICKUP_OFFLINE_PRICE_ORDER_NOTIFY_MSG_4_CUSTOMER, pickupCode);
                msgType2C = MiniMsgTypeEnum.PAY_SUCCESS.getMsgType();
                keyword4 = realPay;
                emphasisKeyword = "keyword4.DATA";
            }else {
                customerMsg = MessageFormat.format(OrderNotifyMsg.WAIT_PICKUP_ORDER_NOTIFY_MSG_4_CUSTOMER, pickupCode);
                msgType2C = MiniMsgTypeEnum.STORE_CONFIRM_ORDER.getMsgType();
                keyword4 = pickupCode;
                emphasisKeyword = "keyword4.DATA";
            }
            String page = null;
            MiniTemplateData data1 = new MiniTemplateData();
            data1.setKeyName("keyword1");
            data1.setValue(prodTitles);
            MiniTemplateData data2 = new MiniTemplateData();
            data2.setKeyName("keyword2");
            data2.setValue(orderTotal);
            MiniTemplateData data3 = new MiniTemplateData();
            data3.setKeyName("keyword3");
            data3.setValue(customerMsg);
            MiniTemplateData data4 = new MiniTemplateData();
            if (StringUtils.isNotBlank(keyword4)) {
                data4.setKeyName("keyword4");
                data4.setValue(keyword4);
            }
            MiniMsgCondition miniMsgCondition = OrderUtil.genMiniMsgCondition(openid, page, msgType2C, emphasisKeyword, data1, data2, data3, data4);
            messageServiceClient.sendMiniTemplateMsg(miniMsgCondition);
        } catch (Exception e) {
            logger.error("提醒用户:openid={},提货发送消息失败", openid);
            logger.error("提醒用户提货发送消息失败：", e);
        }
    }

    /**
     * 订单待提货发信息给门店
     *
     * @param messageServiceClient
     * @param last4MobileNums
     * @param storeId
     * @author wangbin
     * @date 2018年8月16日 下午6:37:26
     */
    public static void orderNeedPickupSendMsg2Store(MessageSendUtils messageServiceClient, String last4MobileNums, Long storeId) {
        String storeMsg = MessageFormat.format(OrderNotifyMsg.WAIT_PICKUP_ORDER_NOTIFY_MSG_4_STORE, last4MobileNums);
        try {
            // 发送云信
            String createdBy = "";
            int expiration = 0;
            int msgType = 0;
            short pageType = 1;
            int audioType = 0;
            String treeCode = "treeCode";
            NeteaseMsgCondition neteaseMsgCondition = OrderUtil.genNeteaseMsgCondition(storeId, storeMsg, createdBy, expiration, msgType,
                    pageType, audioType, treeCode);
            messageServiceClient.sendNeteaseMsg(neteaseMsgCondition);
        } catch (Exception e) {
            logger.error("订单待提货给门店:storeId={},发送消息:{},失败", storeId, storeMsg);
            logger.error("订单待提货给门店发送消息失败：", e);
        }
    }

    /**
     * 订单需要门店确认用户信息通知
     * @author wangbin
     * @date  2018年8月22日 上午10:31:54
     * @Description 
     * @param messageServiceClient
     * @param valuationType
     * @param openid
     * @param prodTitles
     * @param orderTotal
     * @param couponTitles
     * @param couponHxdMoney
     * @param realPay
     */
    public static void orderNeedConfirmSendMsg2Customer(MessageSendUtils messageServiceClient, Short valuationType,
            String openid, String prodTitles, String orderTotal, String couponTitles, String couponHxdMoney, String realPay) {
        try {
            String customerMsg;
            short msgType2C;
            String keyword4 = null;
            String emphasisKeyword = null;
            if (valuationType == ValuationTypeEnum.OFFLINE_VALUATION.getTypeCode()) {
                logger.info("线下计价待门店确认无需发送消息给用户");
                return;
            }else {
                customerMsg = OrderNotifyMsg.ONLINE_PRICE_ORDER_PAY_SUCCESS_NOTIFY_MSG_4_CUSTOMER;
                msgType2C = MiniMsgTypeEnum.PAY_SUCCESS.getMsgType();
                keyword4 = realPay;
                emphasisKeyword = "keyword4.DATA";
            }
            String page = null;
            MiniTemplateData data1 = new MiniTemplateData();
            data1.setKeyName("keyword1");
            data1.setValue(prodTitles);
            MiniTemplateData data2 = new MiniTemplateData();
            data2.setKeyName("keyword2");
            data2.setValue(orderTotal);
            if (StringUtils.isNotBlank(couponTitles)) {
                data2.setValue(orderTotal + "，优惠券抵扣 " + couponHxdMoney);
            }
            MiniTemplateData data3 = new MiniTemplateData();
            data3.setKeyName("keyword3");
            data3.setValue(customerMsg);
            MiniTemplateData data4 = new MiniTemplateData();
            if (StringUtils.isNotBlank(keyword4)) {
                data4.setKeyName("keyword4");
                data4.setValue(keyword4);
            }
            MiniMsgCondition miniMsgCondition = OrderUtil.genMiniMsgCondition(openid, page, msgType2C, emphasisKeyword, data1, data2, data3, data4);
            messageServiceClient.sendMiniTemplateMsg(miniMsgCondition);
        } catch (Exception e) {
            logger.error("提醒用户:openid={},订单待确认发送消息失败", openid);
            logger.error("提醒用户订单待确认发送消息失败：", e);
        }
        
    }

    /**
     * 订单待支付发送给门店信息
     * @author wangbin
     * @date  2018年8月22日 上午10:49:43
     * @Description 
     * @param messageServiceClient
     * @param valuationType
     * @param openid
     * @param prodTitles
     * @param orderTotal
     * @param realPay
     * @param pickupCode
     */
    public static void orderNeedPayMsg2Customer(MessageSendUtils messageServiceClient, Short valuationType,
            String openid, String prodTitles, String orderTotal, String realPay, String pickupCode) {
        try {
            String customerMsg;
            short msgType2C;
            if (valuationType == ValuationTypeEnum.ONLINE_VALUATION.getTypeCode()) {
                logger.info("线上计价待支付无需发送消息给用户");
                return;
            }
            customerMsg = OrderNotifyMsg.OFFLINE_PRICE_ORDER_NEED_PAY_NOTIFY_MSG_4_CUSTOMER;
            msgType2C = MiniMsgTypeEnum.STORE_CONFIRM_ORDER.getMsgType();
            String page = null;
            MiniTemplateData data1 = new MiniTemplateData();
            data1.setKeyName("keyword1");
            data1.setValue(prodTitles);
            MiniTemplateData data2 = new MiniTemplateData();
            data2.setKeyName("keyword2");
            data2.setValue(orderTotal);
            MiniTemplateData data3 = new MiniTemplateData();
            data3.setKeyName("keyword3");
            data3.setValue(customerMsg);
            MiniMsgCondition miniMsgCondition = OrderUtil.genMiniMsgCondition(openid, page, msgType2C, null, data1, data2, data3);
            messageServiceClient.sendMiniTemplateMsg(miniMsgCondition);
        } catch (Exception e) {
            logger.error("提醒用户:openid={},订单待确认发送消息失败", openid);
            logger.error("提醒用户订单待确认发送消息失败：", e);
        }
        
    }
}
