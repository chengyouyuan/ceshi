package com.winhxd.b2c.order.service.impl;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winhxd.b2c.common.constant.OrderNotifyMsg;
import com.winhxd.b2c.common.domain.order.enums.OrderStatusEnum;
import com.winhxd.b2c.common.domain.order.enums.PayTypeEnum;
import com.winhxd.b2c.common.domain.order.model.OrderInfo;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.order.service.OrderChangeLogService;
import com.winhxd.b2c.order.service.OrderChangeLogService.MainPointEnum;
import com.winhxd.b2c.order.service.OrderHandler;

/**
 * 在线线下计价自提订单处理接口
 * @author wangbin
 * @date  2018年8月3日 上午9:36:33
 * @version 
 */
@Service("OnlinePayPickUpInStoreOfflineOrderHandler")
public class OnlinePayPickUpInStoreOfflineOrderHandlerImpl implements OrderHandler {

    private static final String ORDER_INFO_EMPTY = "orderInfo不能为空";

    private static final Logger logger = LoggerFactory.getLogger(OnlinePayPickUpInStoreOfflineOrderHandlerImpl.class);
    
    @Autowired
    private OrderChangeLogService orderChangeLogService;
    
    @Override
    public void orderInfoBeforeCreateProcess(OrderInfo orderInfo) {
        if (orderInfo == null) {
            throw new NullPointerException(ORDER_INFO_EMPTY);
        }
        orderInfo.setOrderStatus(OrderStatusEnum.UNRECEIVED.getStatusCode());
    }

    @Override
    public void orderInfoAfterCreateProcess(OrderInfo orderInfo) {
        if (orderInfo == null) {
            throw new NullPointerException(ORDER_INFO_EMPTY);
        }
        if (orderInfo.getPayType() == null
                || orderInfo.getPayType().shortValue() != PayTypeEnum.WECHAT_SCAN_CODE_PAYMENT.getTypeCode()) {
            throw new UnsupportedOperationException(MessageFormat.format("只支持扫码支付订单：payType={0}, 实际payType={1}",
                    PayTypeEnum.WECHAT_SCAN_CODE_PAYMENT.getTypeCode(), orderInfo.getPayType()));
        }
        logger.info("扫码支付下单后状态直接流转到待接单");
        // 生产订单流转日志
        orderChangeLogService.orderChange(orderInfo.getOrderNo(), null, JsonUtil.toJSONString(orderInfo), orderInfo.getOrderStatus(),
                orderInfo.getOrderStatus(), orderInfo.getCreatedBy(), orderInfo.getCreatedByName(),
                OrderStatusEnum.UNRECEIVED.getStatusDes(), MainPointEnum.MAIN);
    }

    @Override
    public void orderInfoAfterCreateSuccessProcess(OrderInfo orderInfo) {
        if (orderInfo == null) {
            throw new NullPointerException(ORDER_INFO_EMPTY);
        }
        // TODO 发送云信
        String msg = OrderNotifyMsg.NEW_ORDER_NOTIFY_MSG_4_STORE;
    }
    
    @Override
    public void orderFinishPayProcess(OrderInfo orderInfo) {
        // TODO Auto-generated method stub
        
    }

}
