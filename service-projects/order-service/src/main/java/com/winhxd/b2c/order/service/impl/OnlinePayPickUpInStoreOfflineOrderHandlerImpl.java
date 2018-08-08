package com.winhxd.b2c.order.service.impl;

import java.text.MessageFormat;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.OrderNotifyMsg;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.enums.OrderStatusEnum;
import com.winhxd.b2c.common.domain.order.model.OrderInfo;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.store.StoreServiceClient;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.order.dao.OrderInfoMapper;
import com.winhxd.b2c.order.service.OrderChangeLogService;
import com.winhxd.b2c.order.service.OrderChangeLogService.MainPointEnum;
import com.winhxd.b2c.order.service.OrderHandler;
import com.winhxd.b2c.order.service.OrderQueryService;

/**
 * 在线支付线下计价自提订单处理接口
 * @author wangbin
 * @date  2018年8月3日 上午9:36:33
 * @version 
 */
@Service("OnlinePayPickUpInStoreOfflineOrderHandler")
public class OnlinePayPickUpInStoreOfflineOrderHandlerImpl implements OrderHandler {

    private static final String ORDER_INFO_EMPTY = "orderInfo不能为空";

    private static final Logger logger = LoggerFactory.getLogger(OnlinePayPickUpInStoreOfflineOrderHandlerImpl.class);
    
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    
    @Autowired
    private OrderQueryService orderQueryService;
    
    @Autowired
    private StoreServiceClient storeServiceClient;

    private static final String ORDER_TYPE_DESC = "在线支付线下计价自提订单";
    
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
        logger.info("{},orderNo={} 创建后逻辑处理开始", ORDER_TYPE_DESC, orderInfo.getOrderNo());
        // 生产订单流转日志
        orderChangeLogService.orderChange(orderInfo.getOrderNo(), null, JsonUtil.toJSONString(orderInfo), orderInfo.getOrderStatus(),
                orderInfo.getOrderStatus(), orderInfo.getCreatedBy(), orderInfo.getCreatedByName(),
                OrderStatusEnum.UNRECEIVED.getStatusDes(), MainPointEnum.MAIN);
        logger.info("{},orderNo={} 创建后逻辑处理结", ORDER_TYPE_DESC, orderInfo.getOrderNo());
    }

    @Override
    public void orderInfoAfterCreateSuccessProcess(OrderInfo orderInfo) {
        if (orderInfo == null) {
            throw new NullPointerException(ORDER_INFO_EMPTY);
        }
        ResponseResult<Void> result = storeServiceClient.bindCustomer(orderInfo.getCustomerId(), orderInfo.getStoreId());
        if (result == null || result.getCode() != BusinessCode.CODE_OK) {
            logger.error("门店storeId={}，客户customerId={} 绑定关系失败", orderInfo.getStoreId(), orderInfo.getCustomerId());
        }
        // TODO 发送云信
        String msg = OrderNotifyMsg.NEW_ORDER_NOTIFY_MSG_4_STORE;
        // TODO 发送延时MQ信息，处理超时取消操作
    }
    
    @Override
    @Transactional(rollbackFor=Exception.class)
    public void orderFinishPayProcess(OrderInfo orderInfo) {
        if (orderInfo == null) {
            throw new NullPointerException(ORDER_INFO_EMPTY);
        }
        String oldOrderJson = JsonUtil.toJSONString(orderInfo);
        logger.info("{},orderNo={} 支付成功后业务处理开始", ORDER_TYPE_DESC, orderInfo.getOrderNo());
        orderStatusChange(orderInfo, OrderStatusEnum.WAIT_PAY.getStatusCode(), OrderStatusEnum.WAIT_SELF_LIFTING.getStatusCode());
        String pickUpCode = orderQueryService.getPickUpCode(orderInfo.getStoreId());
        if (StringUtils.isBlank(pickUpCode)) {
            throw new BusinessException(BusinessCode.ORDER_PICK_UP_CODE_WRONG);
        }
        int changeNum = orderInfoMapper.updateOrderPickupCode(pickUpCode, orderInfo.getId());
        if (changeNum != 1) {
            throw new BusinessException(BusinessCode.ORDER_STATUS_CHANGE_FAILURE,
                    MessageFormat.format("订单orderNo={0}, 提货码更新失败", orderInfo.getOrderNo()));
        }
        orderInfo.setPickupCode(pickUpCode);
        orderInfo.setOrderStatus(OrderStatusEnum.WAIT_SELF_LIFTING.getStatusCode());
        String newOrderJson = JsonUtil.toJSONString(orderInfo);
        // 生成订单流转日志
        orderChangeLogService.orderChange(orderInfo.getOrderNo(), oldOrderJson, newOrderJson, OrderStatusEnum.WAIT_PAY.getStatusCode(),
                OrderStatusEnum.WAIT_SELF_LIFTING.getStatusCode(), orderInfo.getCreatedBy(), orderInfo.getCreatedByName(),
                MessageFormat.format(OrderStatusEnum.WAIT_SELF_LIFTING.getStatusDes(), pickUpCode), MainPointEnum.MAIN);
        logger.info("{},orderNo={} 支付成功后业务处理结束", ORDER_TYPE_DESC, orderInfo.getOrderNo());
        
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void orderInfoConfirmProcess(OrderInfo orderInfo) {
        if (orderInfo == null) {
            throw new NullPointerException(ORDER_INFO_EMPTY);
        }
        String oldOrderJson = JsonUtil.toJSONString(orderInfo);
        logger.info("{},orderNo={} 订单确认后业务处理开始", ORDER_TYPE_DESC, orderInfo.getOrderNo());
        orderInfo.setOrderStatus(OrderStatusEnum.ALREADY_VALUATION.getStatusCode());
        String newOrderJson = JsonUtil.toJSONString(orderInfo);
        // 生成订单流转日志
        orderChangeLogService.orderChange(orderInfo.getOrderNo(), oldOrderJson, newOrderJson, OrderStatusEnum.UNRECEIVED.getStatusCode(),
                OrderStatusEnum.ALREADY_VALUATION.getStatusCode(), orderInfo.getCreatedBy(), orderInfo.getCreatedByName(),
                OrderStatusEnum.ALREADY_VALUATION.getStatusDes(), MainPointEnum.MAIN);
        orderInfo.setOrderStatus(OrderStatusEnum.WAIT_PAY.getStatusCode());
        String newOrderJson1 = JsonUtil.toJSONString(orderInfo);
        //确认 后状态流转到待付款
        orderStatusChange(orderInfo, OrderStatusEnum.UNRECEIVED.getStatusCode(), OrderStatusEnum.WAIT_PAY.getStatusCode());
        orderChangeLogService.orderChange(orderInfo.getOrderNo(), newOrderJson, newOrderJson1, OrderStatusEnum.ALREADY_VALUATION.getStatusCode(),
                OrderStatusEnum.WAIT_PAY.getStatusCode(), orderInfo.getStoreId(), null,
                OrderStatusEnum.WAIT_PAY.getStatusDes(), MainPointEnum.MAIN);
        logger.info("{},orderNo={} 订单确认后业务处理结束", ORDER_TYPE_DESC, orderInfo.getOrderNo());
    }

    @Override
    public void orderInfoAfterPaySuccessProcess(OrderInfo orderInfo) {
        if (orderInfo == null) {
            throw new NullPointerException(ORDER_INFO_EMPTY);
        }
        // TODO 发送云信
        String last4MobileNums = null;
        String msg = MessageFormat.format(OrderNotifyMsg.WAIT_PICKUP_ORDER_NOTIFY_MSG_4_STORE, last4MobileNums);
    }
    
    /**
     * 订单状态变更
     * @author wangbin
     * @date  2018年8月8日 上午11:32:20
     * @param orderInfo
     * @param expectedStatusCode
     * @param newStatusCode
     */
    private void orderStatusChange(OrderInfo orderInfo, Short expectedStatusCode, Short newStatusCode) {
        if (orderInfo.getOrderStatus() == null || orderInfo.getOrderStatus().shortValue() != expectedStatusCode) {
            throw new UnsupportedOperationException(
                    MessageFormat.format("订单orderNo={0},支付成功业务逻辑处理,状态错误：期望当前订单状态：{1}，实际订单状态：{2}",
                            orderInfo.getOrderNo(), expectedStatusCode, orderInfo.getOrderStatus()));
        }
        // 在线支付后，订单状态流转到待接单
        int changeNum = orderInfoMapper.updateOrderStatus(expectedStatusCode, newStatusCode, orderInfo.getId());
        if (changeNum != 1) {
            throw new BusinessException(BusinessCode.ORDER_STATUS_CHANGE_FAILURE,
                    MessageFormat.format("订单orderNo={0}, 订单状态修改失败", orderInfo.getOrderNo()));
        }
    }

}
