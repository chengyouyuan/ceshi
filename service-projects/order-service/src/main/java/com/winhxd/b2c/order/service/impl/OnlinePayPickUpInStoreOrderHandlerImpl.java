package com.winhxd.b2c.order.service.impl;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.OrderNotifyMsg;
import com.winhxd.b2c.common.constant.OrderOperateTime;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.customer.vo.CustomerUserInfoVO;
import com.winhxd.b2c.common.domain.message.condition.MiniMsgCondition;
import com.winhxd.b2c.common.domain.message.condition.MiniTemplateData;
import com.winhxd.b2c.common.domain.message.enums.MiniMsgTypeEnum;
import com.winhxd.b2c.common.domain.order.enums.OrderStatusEnum;
import com.winhxd.b2c.common.domain.order.enums.PickUpTypeEnum;
import com.winhxd.b2c.common.domain.order.model.OrderInfo;
import com.winhxd.b2c.common.domain.order.util.OrderUtil;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.customer.CustomerServiceClient;
import com.winhxd.b2c.common.feign.message.MessageServiceClient;
import com.winhxd.b2c.common.feign.store.StoreServiceClient;
import com.winhxd.b2c.common.mq.MQDestination;
import com.winhxd.b2c.common.mq.StringMessageSender;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.order.dao.OrderInfoMapper;
import com.winhxd.b2c.order.service.OrderChangeLogService;
import com.winhxd.b2c.order.service.OrderChangeLogService.MainPointEnum;
import com.winhxd.b2c.order.service.OrderHandler;
import com.winhxd.b2c.order.service.OrderQueryService;

/**
 * 在线支付自提订单处理接口
 * @author wangbin
 * @date  2018年8月3日 上午9:36:33
 * @version 
 */
@Service("OnlinePayPickUpInStoreOrderHandler")
public class OnlinePayPickUpInStoreOrderHandlerImpl implements OrderHandler {
    
    private static final String ORDER_TYPE_DESC = "在线支付线上计价订单";
    
    private static final String ORDER_INFO_EMPTY = "orderInfo不能为空";
    
    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private OrderQueryService orderQueryService;
    
    @Autowired
    private OrderChangeLogService orderChangeLogService;
    
    @Autowired
    private StoreServiceClient storeServiceClient;
    
    @Autowired
    private StringMessageSender stringMessageSender;
    
    @Autowired
    private CustomerServiceClient customerServiceclient;
    
    @Resource
    private Cache cache;
    
    @Resource
    private MessageServiceClient messageServiceClient;
    
    private static final Logger logger = LoggerFactory.getLogger(OnlinePayPickUpInStoreOrderHandlerImpl.class);

    @Override
    public void orderInfoBeforeCreateProcess(OrderInfo orderInfo) {
        if (orderInfo == null) {
            throw new NullPointerException(ORDER_INFO_EMPTY);
        }
        orderInfo.setOrderStatus(OrderStatusEnum.SUBMITTED.getStatusCode());
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void orderInfoAfterCreateProcess(OrderInfo orderInfo) {
        if (orderInfo == null) {
            throw new NullPointerException(ORDER_INFO_EMPTY);
        }
        logger.info("{},orderNo={} 创建后逻辑处理开始", ORDER_TYPE_DESC, orderInfo.getOrderNo());
        orderStatusChange(orderInfo, OrderStatusEnum.SUBMITTED.getStatusCode(), OrderStatusEnum.WAIT_PAY.getStatusCode());
        String oldOrderJson = JsonUtil.toJSONString(orderInfo);
        orderInfo.setOrderStatus(OrderStatusEnum.WAIT_PAY.getStatusCode());
        String newOrderJson = JsonUtil.toJSONString(orderInfo);
        // 生成订单流转日志
        orderChangeLogService.orderChange(orderInfo.getOrderNo(), oldOrderJson, newOrderJson, OrderStatusEnum.SUBMITTED.getStatusCode(),
                OrderStatusEnum.WAIT_PAY.getStatusCode(), orderInfo.getCreatedBy(), orderInfo.getCreatedByName(),
                OrderStatusEnum.WAIT_PAY.getStatusDes(), MainPointEnum.MAIN);
        logger.info("{},orderNo={} 支付成功后业务处理结束", ORDER_TYPE_DESC, orderInfo.getOrderNo());
    }

    @Override
    public void orderInfoAfterCreateSuccessProcess(OrderInfo orderInfo) {
        if (orderInfo == null) {
            throw new NullPointerException(ORDER_INFO_EMPTY);
        }
        logger.info("{} 成功提交后处理逻辑开始", ORDER_TYPE_DESC);
        ResponseResult<Void> result = storeServiceClient.bindCustomer(orderInfo.getCustomerId(), orderInfo.getStoreId());
        if (result == null || result.getCode() != BusinessCode.CODE_OK) {
            logger.error("门店storeId={}，客户customerId={} 绑定关系失败", orderInfo.getStoreId(), orderInfo.getCustomerId());
        }
        logger.info("{} 成功提交后处理逻辑结束", ORDER_TYPE_DESC);
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void orderFinishPayProcess(OrderInfo orderInfo) {
        if (orderInfo == null) {
            throw new NullPointerException(ORDER_INFO_EMPTY);
        }
        logger.info("{},orderNo={} 支付成功后业务处理开始", ORDER_TYPE_DESC, orderInfo.getOrderNo());
        orderStatusChange(orderInfo, OrderStatusEnum.WAIT_PAY.getStatusCode(), OrderStatusEnum.UNRECEIVED.getStatusCode());
        String oldOrderJson = JsonUtil.toJSONString(orderInfo);
        orderInfo.setOrderStatus(OrderStatusEnum.UNRECEIVED.getStatusCode());
        String newOrderJson = JsonUtil.toJSONString(orderInfo);
        // 生成订单流转日志
        orderChangeLogService.orderChange(orderInfo.getOrderNo(), oldOrderJson, newOrderJson, OrderStatusEnum.WAIT_PAY.getStatusCode(),
                OrderStatusEnum.UNRECEIVED.getStatusCode(), orderInfo.getCreatedBy(), orderInfo.getCreatedByName(),
                OrderStatusEnum.UNRECEIVED.getStatusDes(), MainPointEnum.MAIN);
        logger.info("{},orderNo={} 支付成功后业务处理结束", ORDER_TYPE_DESC, orderInfo.getOrderNo());
    }

    @Override
    public void orderInfoConfirmProcess(OrderInfo orderInfo) {
        if (orderInfo == null) {
            throw new NullPointerException(ORDER_INFO_EMPTY);
        }
        String oldOrderJson = JsonUtil.toJSONString(orderInfo);
        //设置订单 接单时间
        Date confirmDate = new Date();
        orderInfo.setAcceptOrderDatetime(confirmDate);
        logger.info("{},orderNo={} 确认订单后业务处理开始", ORDER_TYPE_DESC, orderInfo.getOrderNo());
        orderStatusChange(orderInfo, OrderStatusEnum.UNRECEIVED.getStatusCode(), OrderStatusEnum.WAIT_SELF_LIFTING.getStatusCode());
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
        orderChangeLogService.orderChange(orderInfo.getOrderNo(), oldOrderJson, newOrderJson, OrderStatusEnum.UNRECEIVED.getStatusCode(),
                OrderStatusEnum.WAIT_SELF_LIFTING.getStatusCode(), orderInfo.getCreatedBy(), orderInfo.getCreatedByName(),
                MessageFormat.format(OrderStatusEnum.WAIT_SELF_LIFTING.getStatusDes(), pickUpCode), MainPointEnum.MAIN);
        logger.info("{},orderNo={} 确认订单后业务处理结束", ORDER_TYPE_DESC, orderInfo.getOrderNo());
    }
    
    @Override
    public void orderInfoAfterConfirmSuccessProcess(OrderInfo orderInfo) {
        if (orderInfo == null) {
            throw new NullPointerException(ORDER_INFO_EMPTY);
        }
        // 发送延时MQ信息，处理超时未自提 取消操作
        int delayMilliseconds = OrderOperateTime.ORDER_NEED_PICKUP_TIME_BY_MILLISECONDS;
        stringMessageSender.send(MQDestination.ORDER_PICKUP_TIMEOUT_DELAYED, orderInfo.getOrderNo(), delayMilliseconds);
        // 发送消息给用户
        OrderUtil.orderNeedPickupSendMsg2Customer(messageServiceClient, orderInfo.getPickupDateTime(), orderInfo.getPayType(), getCustomerUserInfoVO(orderInfo.getCustomerId()).getOpenid());
    }
    

    @Override
    public void orderInfoAfterPaySuccessProcess(OrderInfo orderInfo) {
        if (orderInfo == null) {
            throw new NullPointerException(ORDER_INFO_EMPTY);
        }
        //支付成功清空门店订单销量统计cache
        cache.del(OrderUtil.getStoreOrderSalesSummaryKey(orderInfo.getStoreId()));
        //给门店发送云信
        OrderUtil.newOrderSendMsg2Store(messageServiceClient, orderInfo.getStoreId());    
        // 发送延时MQ信息，处理超时未确认取消操作
        logger.info("{}, orderNo={} 下单成功发送 超时未确认取消操作MQ延时消息 开始", ORDER_TYPE_DESC, orderInfo.getOrderNo());
        int delayMilliseconds = OrderOperateTime.ORDER_NEED_RECEIVE_TIME_BY_MILLISECONDS;
        stringMessageSender.send(MQDestination.ORDER_RECEIVE_TIMEOUT_DELAYED, orderInfo.getOrderNo(), delayMilliseconds);
        logger.info("{}, orderNo={} 下单成功发送 超时未确认取消操作MQ延时消息 结束", ORDER_TYPE_DESC, orderInfo.getOrderNo());
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
        //如果是确认订单，则更新订单确认时间
        if (expectedStatusCode.shortValue() == OrderStatusEnum.UNRECEIVED.getStatusCode()) {
            orderInfoMapper.updateOrderConfirmDate(orderInfo.getAcceptOrderDatetime(), orderInfo.getId());
        }
        int changeNum = orderInfoMapper.updateOrderStatus(expectedStatusCode, newStatusCode, orderInfo.getId());
        if (changeNum != 1) {
            throw new BusinessException(BusinessCode.ORDER_STATUS_CHANGE_FAILURE,
                    MessageFormat.format("订单orderNo={0}, 订单状态修改失败", orderInfo.getOrderNo()));
        }
    }
    
    private CustomerUserInfoVO getCustomerUserInfoVO(Long customerId) {
        ResponseResult<List<CustomerUserInfoVO>> ret = customerServiceclient.findCustomerUserByIds(Arrays.asList(customerId));
        if (ret == null || ret.getCode() != BusinessCode.CODE_OK || ret.getData() == null || ret.getData().isEmpty()) {
            throw new BusinessException(BusinessCode.WRONG_CUSTOMER_ID);
        }
        logger.info("根据customerId={} 获取用户信息成功，用户信息：{}", ret.getData().get(0));
        return ret.getData().get(0);
    }
}
