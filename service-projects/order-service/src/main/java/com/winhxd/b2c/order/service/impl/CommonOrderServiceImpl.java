package com.winhxd.b2c.order.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.cache.Lock;
import com.winhxd.b2c.common.cache.RedisLock;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.constant.OrderNotifyMsg;
import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.context.StoreUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.customer.vo.CustomerUserInfoVO;
import com.winhxd.b2c.common.domain.message.condition.MiniMsgCondition;
import com.winhxd.b2c.common.domain.message.condition.MiniTemplateData;
import com.winhxd.b2c.common.domain.message.condition.NeteaseMsg;
import com.winhxd.b2c.common.domain.message.condition.NeteaseMsgCondition;
import com.winhxd.b2c.common.domain.message.enums.MiniMsgTypeEnum;
import com.winhxd.b2c.common.domain.order.condition.OrderCancelCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderConfirmCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderCreateCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderItemCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderPickupCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderRefundCallbackCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderRefundCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderRefundStoreHandleCondition;
import com.winhxd.b2c.common.domain.order.enums.OrderStatusEnum;
import com.winhxd.b2c.common.domain.order.enums.PayStatusEnum;
import com.winhxd.b2c.common.domain.order.enums.PayTypeEnum;
import com.winhxd.b2c.common.domain.order.enums.PickUpTypeEnum;
import com.winhxd.b2c.common.domain.order.enums.ValuationTypeEnum;
import com.winhxd.b2c.common.domain.order.model.OrderInfo;
import com.winhxd.b2c.common.domain.order.model.OrderItem;
import com.winhxd.b2c.common.domain.order.util.OrderUtil;
import com.winhxd.b2c.common.domain.pay.condition.PayRefundCondition;
import com.winhxd.b2c.common.domain.pay.vo.PayRefundVO;
import com.winhxd.b2c.common.domain.product.condition.ProductCondition;
import com.winhxd.b2c.common.domain.product.enums.SearchSkuCodeEnum;
import com.winhxd.b2c.common.domain.product.vo.ProductSkuVO;
import com.winhxd.b2c.common.domain.promotion.condition.CouponPreAmountCondition;
import com.winhxd.b2c.common.domain.promotion.condition.CouponProductCondition;
import com.winhxd.b2c.common.domain.promotion.condition.OrderUntreadCouponCondition;
import com.winhxd.b2c.common.domain.promotion.condition.OrderUseCouponCondition;
import com.winhxd.b2c.common.domain.promotion.vo.CouponDiscountVO;
import com.winhxd.b2c.common.domain.store.vo.ShopCartProdVO;
import com.winhxd.b2c.common.domain.store.vo.StoreUserInfoVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.customer.CustomerServiceClient;
import com.winhxd.b2c.common.feign.message.MessageServiceClient;
import com.winhxd.b2c.common.feign.pay.PayServiceClient;
import com.winhxd.b2c.common.feign.product.ProductServiceClient;
import com.winhxd.b2c.common.feign.promotion.CouponServiceClient;
import com.winhxd.b2c.common.feign.store.StoreServiceClient;
import com.winhxd.b2c.common.mq.MQDestination;
import com.winhxd.b2c.common.mq.MQHandler;
import com.winhxd.b2c.common.mq.StringMessageListener;
import com.winhxd.b2c.common.mq.StringMessageSender;
import com.winhxd.b2c.common.mq.event.EventMessageSender;
import com.winhxd.b2c.common.mq.event.EventType;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.order.dao.OrderInfoMapper;
import com.winhxd.b2c.order.dao.OrderItemMapper;
import com.winhxd.b2c.order.service.OrderChangeLogService;
import com.winhxd.b2c.order.service.OrderChangeLogService.MainPointEnum;
import com.winhxd.b2c.order.service.OrderHandler;
import com.winhxd.b2c.order.service.OrderService;

/**
 * @author wangbin
 */
@Service
public class CommonOrderServiceImpl implements OrderService {

    private static final String ORDER_BEING_MODIFIED = "订单正在修改中";
    private static final String ORDER_NO_CANNOT_NULL = "订单号不能为空";
    private static final int QUEUE_CAPACITY = 1000;
    private static final int KEEP_ALIVE_TIME = 50;
    private static final int MAXIMUM_POOL_SIZE = 20;
    private static final int CORE_POOL_SIZE = 5;
    private static final int ORDER_MONEY_SCALE = 2;
    private static final int ORDER_UPDATE_LOCK_EXPIRES_TIME = 5000;
    private static final Logger logger = LoggerFactory.getLogger(CommonOrderServiceImpl.class);

    private static final ThreadLocal<Map<String, ProductSkuVO>> SKU_INFO_MAP_THREADLOCAL = new ThreadLocal<>();

    @Autowired
    @Qualifier("OnlinePayPickUpInStoreOrderHandler")
    private OrderHandler onlinePayPickUpInStoreOrderHandler;

    @Autowired
    @Qualifier("OnlinePayPickUpInStoreOfflineOrderHandler")
    private OrderHandler onlinePayPickUpInStoreOfflineOrderHandler;

    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private CouponServiceClient couponServiceClient;
    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderChangeLogService orderChangeLogService;
    @Autowired
    private StoreServiceClient storeServiceClient;
    @Autowired
    private Cache cache;

    @Autowired
    private CustomerServiceClient customerServiceclient;
    @Autowired
    private ProductServiceClient productServiceClient;
    @Autowired
    private MessageServiceClient messageServiceClient;
    @Autowired
    private StringMessageSender stringMessageSender;
    @Autowired
    private EventMessageSender eventMessageSender;
    @Autowired
    private PayServiceClient payServiceClient;


    private ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("order-thread-pool-%d").build();
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, new ArrayBlockingQueue<>(QUEUE_CAPACITY),
            namedThreadFactory);

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public OrderInfo submitOrder(OrderCreateCondition orderCreateCondition) {
        if (orderCreateCondition == null) {
            throw new NullPointerException("orderCreateCondition不能为空");
        }
        validateOrderCreateCondition(orderCreateCondition);
        logger.info("创建订单开始：orderCreateCondition={}", orderCreateCondition);
        // 生成订单主体信息
        OrderInfo orderInfo = assembleOrderInfo(orderCreateCondition);
        //订单创建前相关业务操作
        logger.info("订单orderNo：{} 创建前相关业务操作执行开始", orderInfo.getOrderNo());
        getOrderHandler(orderInfo.getValuationType()).orderInfoBeforeCreateProcess(orderInfo);
        logger.info("订单orderNo：{} 创建前相关业务操作执行结束", orderInfo.getOrderNo());
        orderInfoMapper.insertSelective(orderInfo);
        orderItemMapper.insertItems(orderInfo.getOrderItems());
        // 生产订单流转日志
        orderChangeLogService.orderChange(orderInfo.getOrderNo(), null, JsonUtil.toJSONString(orderInfo), null,
                orderInfo.getOrderStatus(), orderInfo.getCreatedBy(), orderInfo.getCreatedByName(),
                OrderStatusEnum.SUBMITTED.getStatusDes(), MainPointEnum.MAIN);
        //订单创建后相关业务操作
        logger.info("订单orderNo：{} 创建后相关业务操作执行开始", orderInfo.getOrderNo());
        getOrderHandler(orderInfo.getValuationType()).orderInfoAfterCreateProcess(orderInfo);
        logger.info("订单orderNo：{} 创建后相关业务操作执行结束", orderInfo.getOrderNo());
        logger.info("创建订单结束orderNo：{}", orderInfo.getOrderNo());
        //注册订单创建成功事务提交后相关事件
        registerProcessAfterTransSuccess(new SubmitSuccessProcessRunnerble(orderInfo), new SubmitFailureProcessRunnerble(orderInfo, orderCreateCondition.getCouponIds()));
        return orderInfo;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderPaySuccessNotify(String orderNo, String paymentSerialNum) {
        if (StringUtils.isBlank(orderNo)) {
            throw new NullPointerException("订单支付通知orderNo不能为空");
        }
        if (StringUtils.isBlank(paymentSerialNum)) {
            throw new NullPointerException("订单支付流水号paymentSerialNum不能为空");
        }
        OrderInfo orderInfo = orderInfoMapper.selectByOrderNo(orderNo);
        if (orderInfo == null) {
            throw new BusinessException(BusinessCode.WRONG_ORDERNO);
        }
        if (PayStatusEnum.UNPAID.getStatusCode() != orderInfo.getPayStatus()) {
            throw new BusinessException(BusinessCode.ORDER_ALREADY_PAID);
        }
        logger.info("订单orderNo={}，paymentSerialNum={} 支付通知处理开始.", orderNo, paymentSerialNum);
        Date payFinishDateTime = new Date();
        int updNum = orderInfoMapper.updateOrderPayStatus(PayStatusEnum.PAID.getStatusCode(), paymentSerialNum, payFinishDateTime, orderInfo.getId());
        if (updNum != 1) {
            throw new BusinessException(BusinessCode.ORDER_ALREADY_PAID);
        }
        // 生产订单流转日志
        String oldOrderJsonString = JsonUtil.toJSONString(orderInfo);
        orderInfo.setPayStatus(PayStatusEnum.PAID.getStatusCode());
        orderInfo.setPayFinishDateTime(payFinishDateTime);
        String newOrderJsonString = JsonUtil.toJSONString(orderInfo);
        String orderPayMsg = "订单支付";
        orderChangeLogService.orderChange(orderInfo.getOrderNo(), oldOrderJsonString, newOrderJsonString, orderInfo.getOrderStatus(),
                orderInfo.getOrderStatus(), orderInfo.getCreatedBy(), orderInfo.getCreatedByName(),
                orderPayMsg, MainPointEnum.NOT_MAIN);
        logger.info("订单orderNo={}，支付通知处理结束.", orderNo);
        logger.info("订单orderNo：{} 支付后相关业务操作执行开始", orderInfo.getOrderNo());
        getOrderHandler(orderInfo.getValuationType()).orderFinishPayProcess(orderInfo);
        //订单支付成功事务提交后相关事件
        registerProcessAfterTransSuccess(new PaySuccessProcessRunnerble(orderInfo), null);
        logger.info("订单orderNo：{} 支付后相关业务操作执行结束", orderInfo.getOrderNo());
        logger.info("订单orderNo={}，paymentSerialNum={} 支付通知处理结束.", orderNo, paymentSerialNum);
    }

    /**
     * C端申请退款订单剩3天未确认
     *
     * @param orderNo 订单号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @StringMessageListener(value = MQHandler.ORDER_REFUND_TIMEOUT_3_DAYS_UNCONFIRMED_HANDLER)
    public void orderRefundTimeOut3DaysUnconfirmed(String orderNo) {
        String lockKey = CacheName.CACHE_KEY_STORE_PICK_UP_CODE_GENERATE + orderNo;
        Lock lock = new RedisLock(cache, lockKey, ORDER_UPDATE_LOCK_EXPIRES_TIME);
        try {
            lock.lock();
            OrderInfo order = getOrderInfo(orderNo);
            if (order.getPayStatus() == PayStatusEnum.PAID.getStatusCode() && order.getOrderStatus() == OrderStatusEnum.WAIT_REFUND.getStatusCode()) {
                orderApplyRefund(order, "申请退款超时3天系统自动退款", order.getCreatedBy(), "sys");
                orderRefundTimeOutSendMsg(3, order);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * C端申请退款订单剩1天未确认(申请退款时间+2天)
     *
     * @param orderNo 订单号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @StringMessageListener(value = MQHandler.ORDER_REFUND_TIMEOUT_1_DAY_UNCONFIRMED_HANDLER)
    public void orderRefundTimeOut1DayUnconfirmed(String orderNo) {
        OrderInfo order = getOrderInfo(orderNo);
        if (order.getPayStatus() == PayStatusEnum.PAID.getStatusCode() && order.getOrderStatus() == OrderStatusEnum.WAIT_REFUND.getStatusCode()) {
            orderRefundTimeOutSendMsg(2, order);
        }
    }

    /**
     * C端申请退款订单剩1小时未确认（申请退款时间+3天-1小时）
     *
     * @param orderNo 订单号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @StringMessageListener(value = MQHandler.ORDER_REFUND_TIMEOUT_1_HOUR_UNCONFIRMED_HANDLER)
    public void orderRefundTimeOut1HourUnconfirmed(String orderNo) {
        OrderInfo order = getOrderInfo(orderNo);
        if (order.getPayStatus() == PayStatusEnum.PAID.getStatusCode() && order.getOrderStatus() == OrderStatusEnum.WAIT_REFUND.getStatusCode()) {
            orderRefundTimeOutSendMsg(1, order);
        }
    }

    /**
     * 订单退款回调（状态置为已退款）
     *
     * @param condition 入参
     * @return true 更新成功 false 更新失败
     */
    @Override
    public boolean updateOrderRefundCallback(OrderRefundCallbackCondition condition) {
        boolean callbackResult;
        String orderNo = condition.getOrderNo();
        if (StringUtils.isBlank(orderNo)) {
            throw new BusinessException(BusinessCode.ORDER_NO_EMPTY, ORDER_NO_CANNOT_NULL);
        }
        String lockKey = CacheName.CACHE_KEY_STORE_PICK_UP_CODE_GENERATE + orderNo;
        Lock lock = new RedisLock(cache, lockKey, ORDER_UPDATE_LOCK_EXPIRES_TIME);
        try {
            lock.lock();
            OrderInfo order = getOrderInfo(orderNo);
            //状态是待退款和已付款的订单才能把状态置为已退款
            if (order.getOrderStatus() == OrderStatusEnum.WAIT_REFUND.getStatusCode() && order.getPayStatus() == PayStatusEnum.PAID.getStatusCode()) {
                int result = this.orderInfoMapper.updateOrderStatusForRefundCallback(orderNo);
                if (result > 0) {
                    logger.info("退款回调-添加流转日志开始-订单号={}", orderNo);
                    Short oldStatus = order.getOrderStatus();
                    String oldOrderJsonString = JsonUtil.toJSONString(order);
                    order.setOrderStatus(OrderStatusEnum.REFUNDED.getStatusCode());
                    String newOrderJsonString = JsonUtil.toJSONString(order);
                    //添加订单流转日志
                    orderChangeLogService.orderChange(orderNo, oldOrderJsonString, newOrderJsonString, oldStatus,
                            order.getOrderStatus(), order.getCreatedBy(), "sys", "系统申请退款回调成功", MainPointEnum.MAIN);
                    logger.info("退款回调-添加流转日志结束-发送消息开始-订单号={}", orderNo);
                    // 给C端发送消息 “您有一笔订单已退款完成，退款金额已退回至您的付款账户，请注意查收”
                    NeteaseMsgCondition neteaseMsgCondition = new NeteaseMsgCondition();
                    neteaseMsgCondition.setCustomerId(order.getCustomerId());
                    String storeMsgContent = "您有一笔订单已退款完成，退款金额已退回至您的付款账户，请注意查收";
                    NeteaseMsg neteaseMsg = new NeteaseMsg();
                    neteaseMsg.setMsgContent(storeMsgContent);
                    neteaseMsg.setAudioType(0);
                    neteaseMsgCondition.setNeteaseMsg(neteaseMsg);
                    messageServiceClient.sendNeteaseMsg(neteaseMsgCondition);
                    //给门店发送消息
                    orderRefundTimeOutSendMsg(4, order);
                    logger.info("退款回调-发送消息结束-订单号={}", orderNo);
                    callbackResult = true;
                } else {
                    logger.info("退款回调-订单设置状态为已退款失败-订单号={}", orderNo);
                    callbackResult = false;
                }
            } else {
                callbackResult = false;
                logger.info("退款回调-订单设置状态为已退款失败-原因：订单状态不匹配-订单号={}", orderNo);
            }
        } finally {
            lock.unlock();
        }
        return callbackResult;
    }

    /**
     * 申请退款发送请求
     *
     * @param order      订单
     * @param operatorId 操作人ID
     * @param operator   操作人
     * @param reason     退款原因
     */
    private void applyRefund(OrderInfo order, Long operatorId, String operator, String reason) {
        if (order.getPayStatus() != PayStatusEnum.PAID.getStatusCode() || !StringUtils.isNotBlank(order.getPaymentSerialNum())) {
            throw new BusinessException(BusinessCode.ORDER_REFUND_STATUS_ERROR, "申请退款状态异常");
        }
        PayRefundCondition payRefundCondition = new PayRefundCondition();
        payRefundCondition.setOutTradeNo(order.getPaymentSerialNum());
        payRefundCondition.setTotalAmount(order.getRealPaymentMoney());
        payRefundCondition.setRefundAmount(order.getRealPaymentMoney());
        payRefundCondition.setCreatedBy(operatorId);
        payRefundCondition.setCreatedByName(operator);
        payRefundCondition.setRefundDesc(reason);
        PayRefundVO payRefundVO = payServiceClient.orderRefund(payRefundCondition).getData();
        if (!payRefundVO.isStatus()) {
            throw new BusinessException(BusinessCode.ORDER_REFUND_FAIL, "申请退款申请异常");
        }
    }


    /**
     * 订单取消service 超时订单和取消订单
     *
     * @param orderNo      订单号
     * @param cancelReason 取消原因
     * @param operatorId   操作人ID
     * @param operatorName 操作人名称
     */
    private void orderCancel(String orderNo, String cancelReason, Long operatorId, String operatorName) {
        OrderInfo order = getOrderInfo(orderNo);
        if (!order.getCustomerId().equals(operatorId)) {
            throw new BusinessException(BusinessCode.ORDER_INFO_NOT_MATCH_ERROR,
                    MessageFormat.format("该订单不是登录用户的订单orderNo={0},orderCustomerId={1},customerId={2}", order.getOrderNo(), order.getCustomerId(), operatorId));
        }
        orderCancel(order, cancelReason, operatorId, operatorName, 1);
    }

    /**
     * 订单取消service 超时订单和取消订单
     *
     * @param order        订单
     * @param cancelReason 取消原因
     * @param operatorId   操作人ID
     * @param operatorName 操作人名称
     * @param type         为1时为C端用户取消订单 2为门店取消订单
     */
    private void orderCancel(OrderInfo order, String cancelReason, Long operatorId, String operatorName, int type) {
        String orderNo = order.getOrderNo();
        //判断是否支付成功,支付成功不让取消
        if (PayStatusEnum.PAID.getStatusCode() == order.getPayStatus()) {
            throw new BusinessException(BusinessCode.WRONG_ORDER_STATUS, MessageFormat.format("订单已支付成功不能取消，请走退款接口 订单号={0}", orderNo));
        }
        if (OrderStatusEnum.CANCELED.getStatusCode() == order.getOrderStatus() || OrderStatusEnum.FINISHED.getStatusCode() == order.getOrderStatus()) {
            throw new BusinessException(BusinessCode.WRONG_ORDER_STATUS, MessageFormat.format("订单状态不匹配，不能取消 订单号={0}", orderNo));
        }
        //设置提货码置为null、取消原因、取消状态等
        int updateRowNum = this.orderInfoMapper.updateOrderStatusForCancel(orderNo, cancelReason);
        if (updateRowNum < 1) {
            throw new BusinessException(BusinessCode.WRONG_ORDER_STATUS, MessageFormat.format("取消订单-状态更新不成功-订单号={0}", orderNo));
        } else {
            logger.info("取消订单-添加流转日志开始-订单号={}", orderNo);
            String oldOrderJsonString = JsonUtil.toJSONString(order);
            Short oldStatus = order.getOrderStatus();
            order.setOrderStatus(OrderStatusEnum.CANCELED.getStatusCode());
            order.setCancelReason(cancelReason);
            String newOrderJsonString = JsonUtil.toJSONString(order);
            //添加订单流转日志
            orderChangeLogService.orderChange(order.getOrderNo(), oldOrderJsonString, newOrderJsonString, oldStatus, order.getOrderStatus(), operatorId, operatorName, cancelReason, MainPointEnum
                    .MAIN);
            logger.info("取消订单-添加流转日志结束-订单号={}", orderNo);
            //发送取消事件
            sendCancelEvent(order, cancelReason, operatorId, operatorName);
            //取消订单成功事务提交后相关事件
            registerProcessAfterTransSuccess(new OrderCancelCompleteProcessRunnable(order, type), null);
        }
    }

    /**
     * B端拒接订单
     *
     * @param orderCancelCondition 入参
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrderByStore(OrderCancelCondition orderCancelCondition) {
        String orderNo = orderCancelCondition.getOrderNo();
        if (StringUtils.isBlank(orderNo)) {
            throw new BusinessException(BusinessCode.ORDER_NO_EMPTY, ORDER_NO_CANNOT_NULL);
        }
        StoreUser store = UserContext.getCurrentStoreUser();
        if (null == store) {
            throw new BusinessException(BusinessCode.CODE_1002, "登录凭证无效");
        }
        ResponseResult<StoreUserInfoVO> storeData = this.storeServiceClient.findStoreUserInfo(store.getBusinessId());
        if (storeData.getCode() != BusinessCode.CODE_OK || storeData.getData() == null) {
            throw new BusinessException(BusinessCode.WRONG_STORE_ID, "调用门店服务查询不到门店");
        }

        StoreUserInfoVO storeVO = storeData.getData();
        String lockKey = CacheName.CACHE_KEY_STORE_PICK_UP_CODE_GENERATE + orderNo;
        Lock lock = new RedisLock(cache, lockKey, ORDER_UPDATE_LOCK_EXPIRES_TIME);
        if (lock.tryLock()) {
            try {
                OrderInfo order = orderInfoMapper.selectByOrderNo(orderNo);
                if (null == order) {
                    logger.info("订单不存在 订单号={}", orderNo);
                    throw new BusinessException(BusinessCode.ORDER_DOES_NOT_EXIST, "订单不存在");
                }
                //判断是否支付成功,支付成功走退款逻辑，支付不成功走取消订单逻辑
                if (PayStatusEnum.PAID.getStatusCode() == order.getPayStatus()) {
                    orderApplyRefund(order, orderCancelCondition.getCancelReason(), storeVO.getId(), storeVO.getShopkeeper());
                } else {
                    orderCancel(order, orderCancelCondition.getCancelReason(), storeVO.getId(), storeVO.getShopkeeper(), 2);
                }
            } finally {
                lock.unlock();
            }
        } else {
            throw new BusinessException(BusinessCode.ORDER_IS_BEING_MODIFIED, ORDER_BEING_MODIFIED);
        }
    }

    /**
     * C端取消订单
     *
     * @param orderCancelCondition 入参
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrderByCustomer(OrderCancelCondition orderCancelCondition) {
        CustomerUser user = UserContext.getCurrentCustomerUser();
        if (null == user) {
            throw new BusinessException(BusinessCode.CODE_1002, "登录用户不存在");
        }
        String orderNo = orderCancelCondition.getOrderNo();
        if (StringUtils.isBlank(orderNo)) {
            throw new BusinessException(BusinessCode.ORDER_NO_EMPTY, ORDER_NO_CANNOT_NULL);
        }
        CustomerUserInfoVO customer = getCustomerUserInfoVO(user.getCustomerId());
        String lockKey = CacheName.CACHE_KEY_STORE_PICK_UP_CODE_GENERATE + orderNo;
        Lock lock = new RedisLock(cache, lockKey, ORDER_UPDATE_LOCK_EXPIRES_TIME);
        if (lock.tryLock()) {
            try {
                orderCancel(orderNo, orderCancelCondition.getCancelReason(), user.getCustomerId(), customer.getNickName());
            } finally {
                lock.unlock();
            }
        } else {
            throw new BusinessException(BusinessCode.ORDER_IS_BEING_MODIFIED, MessageFormat.format("订单正在修改中-订单号={0}", orderNo));
        }
    }

    /**
     * B端门店处理用户退款订单
     *
     * @param condition 入参
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleOrderRefundByStore(OrderRefundStoreHandleCondition condition) {
        if (StringUtils.isBlank(condition.getOrderNo()) || null == condition.getAgree()) {
            throw new BusinessException(BusinessCode.CODE_4022001, "参数异常");
        }
        StoreUser store = UserContext.getCurrentStoreUser();
        if (null == store) {
            throw new BusinessException(BusinessCode.WRONG_STORE_ID, "门店不存在");
        }
        ResponseResult<StoreUserInfoVO> storeData = this.storeServiceClient.findStoreUserInfo(store.getBusinessId());
        if (storeData.getCode() != BusinessCode.CODE_OK || storeData.getData() == null) {
            throw new BusinessException(BusinessCode.WRONG_STORE_ID, "调用门店服务查询不到门店");
        }

        StoreUserInfoVO storeVO = storeData.getData();
        String orderNo = condition.getOrderNo();
        String lockKey = CacheName.CACHE_KEY_STORE_PICK_UP_CODE_GENERATE + orderNo;
        Lock lock = new RedisLock(cache, lockKey, ORDER_UPDATE_LOCK_EXPIRES_TIME);
        if (lock.tryLock()) {
            try {
                short agree = condition.getAgree();
                //门店是否同意退款，不同意时不做任何操作
                if (agree == 1) {
                    logger.info("门店同意退款-操作订单开始-订单号={}", orderNo);
                    OrderInfo order = orderInfoMapper.selectByOrderNo(orderNo);
                    if (null == order || !order.getStoreId().equals(store.getStoreCustomerId())) {
                        throw new BusinessException(BusinessCode.WRONG_ORDERNO, "门店处理用户退款订单查询失败");
                    }
                    orderApplyRefund(order, null, storeVO.getId(), storeVO.getShopkeeper());
                    logger.info("门店同意退款-操作订单结束-订单号={}", orderNo);
                }
            } finally {
                lock.unlock();
            }
        } else {
            throw new BusinessException(BusinessCode.ORDER_IS_BEING_MODIFIED, ORDER_BEING_MODIFIED);
        }
    }


    /**
     * C端申请退款
     *
     * @param orderRefundCondition 入参
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderRefundByCustomer(OrderRefundCondition orderRefundCondition) {
        String orderNo = orderRefundCondition.getOrderNo();
        if (StringUtils.isBlank(orderNo)) {
            throw new BusinessException(BusinessCode.ORDER_NO_EMPTY, "参数错误");
        }
        CustomerUser customer = UserContext.getCurrentCustomerUser();
        if (null == customer) {
            throw new BusinessException(BusinessCode.CODE_1002, MessageFormat.format("用户不存在orderNo={0}", orderNo));
        }
        Long customerId = customer.getCustomerId();
        CustomerUserInfoVO customerUserInfoVO = getCustomerUserInfoVO(customerId);
        String lockKey = CacheName.CACHE_KEY_STORE_PICK_UP_CODE_GENERATE + orderNo;
        Lock lock = new RedisLock(cache, lockKey, ORDER_UPDATE_LOCK_EXPIRES_TIME);
        if (lock.tryLock()) {
            try {
                OrderInfo order = orderInfoMapper.selectByOrderNo(orderNo);
                if (null == order || !order.getCustomerId().equals(customer.getCustomerId())) {
                    throw new BusinessException(BusinessCode.ORDER_DOES_NOT_EXIST, MessageFormat.format("C端申请退款订单查询失败orderNo={0}", orderNo));
                }
                //判断订单状态是否可以申请退款
                Short status = order.getOrderStatus();
                if (order.getPayStatus().equals(PayStatusEnum.UNPAID.getStatusCode())
                        || status.equals(OrderStatusEnum.FINISHED.getStatusCode())
                        || status.equals(OrderStatusEnum.CANCELED.getStatusCode())
                        || status.equals(OrderStatusEnum.REFUNDED.getStatusCode())) {
                    throw new BusinessException(BusinessCode.CODE_4021002, MessageFormat.format("订单状态不允许退款orderNo={0}", orderNo));
                }
                //申请退款时商家没确认就直接退款、退优惠券
                if (order.getPayStatus().equals(PayStatusEnum.PAID.getStatusCode()) && status.equals(OrderStatusEnum.UNRECEIVED.getStatusCode())) {
                    //退款流程
                    orderApplyRefund(order, orderRefundCondition.getCancelReason(), customerId, customerUserInfoVO.getNickName());
                } else {
                    //更新订单状态为待退款，并更新相关属性
                    int updateResult = this.orderInfoMapper.updateOrderStatusForApplyRefund(orderNo, customerId, orderRefundCondition.getCancelReason());
                    //添加订单流转日志
                    if (updateResult > 0) {
                        logger.info("C端申请退款-添加流转日志开始-订单号={}", orderNo);
                        Short oldStatus = order.getOrderStatus();
                        String oldOrderJsonString = JsonUtil.toJSONString(order);
                        order.setOrderStatus(OrderStatusEnum.WAIT_REFUND.getStatusCode());
                        String newOrderJsonString = JsonUtil.toJSONString(order);
                        //添加订单流转日志
                        orderChangeLogService.orderChange(order.getOrderNo(), oldOrderJsonString, newOrderJsonString, oldStatus,
                                order.getOrderStatus(), customer.getCustomerId(), customerUserInfoVO.getNickName(), order.getCancelReason(), MainPointEnum.MAIN);
                        logger.info("C端申请退款-添加流转日志结束-订单号={}", orderNo);
                        //发送云信--手机尾号8513顾客申请退款
                        String mobileStr = OrderUtil.getLast4Mobile(customerUserInfoVO.getCustomerMobile());
                        String msgContent = "【已取消】手机尾号" + mobileStr + "顾客申请退款";
                        //发送MQ延时消息
                        logger.info("C端申请退款-MQ延时消息开始-订单号={}", orderNo);
                        //获取两天后的时间
                        Calendar time2DaysAfter = Calendar.getInstance();
                        time2DaysAfter.add(Calendar.DATE, 2);
                        stringMessageSender.send(MQDestination.ORDER_REFUND_TIMEOUT_1_DAY_UNCONFIRMED, orderNo, (int) time2DaysAfter.getTimeInMillis());
                        //获取3天后-1小时
                        Calendar time1HourAfter = Calendar.getInstance();
                        time1HourAfter.add(Calendar.DATE, 3);
                        long hour1mills = time1HourAfter.getTimeInMillis() - 60 * 60 * 1000;
                        stringMessageSender.send(MQDestination.ORDER_REFUND_TIMEOUT_1_HOUR_UNCONFIRMED, orderNo, (int) hour1mills);
                        logger.info("C端申请退款-MQ延时消息结束-订单号={}", orderNo);
                    } else {
                        throw new BusinessException(BusinessCode.ORDER_STATUS_CHANGE_FAILURE, MessageFormat.format("订单取消-C端申请退款不成功 订单号={}", orderNo));
                    }
                }
            } finally {
                lock.unlock();
            }
        } else {
            throw new BusinessException(BusinessCode.ORDER_IS_BEING_MODIFIED, ORDER_BEING_MODIFIED);
        }
    }

    /**
     * 申请订单退款service
     *
     * @param orderNo      订单号
     * @param cancelReason 原因
     * @param operatorId   操作人ID
     * @param operatorName 操作人
     */
    private void orderApplyRefund(String orderNo, String cancelReason, Long operatorId, String operatorName) {
        OrderInfo order = getOrderInfo(orderNo);
        orderApplyRefund(order, cancelReason, operatorId, operatorName);
    }

    /**
     * 申请订单退款service
     *
     * @param order        订单
     * @param cancelReason 原因
     * @param operatorId   操作人ID
     * @param operatorName 操作人姓名
     */
    private void orderApplyRefund(OrderInfo order, String cancelReason, Long operatorId, String operatorName) {
        if (order.getPayStatus().equals(PayStatusEnum.UNPAID.getStatusCode())) {
            throw new BusinessException(BusinessCode.WRONG_ORDER_STATUS, "未支付的订单不允许退款");
        }
        if (order.getOrderStatus().equals(OrderStatusEnum.FINISHED.getStatusCode())) {
            throw new BusinessException(BusinessCode.ORDER_ALREADY_PAID, "已完成的订单不允许退款");
        }
        String reason = StringUtils.isBlank(cancelReason) ? order.getCancelReason() : cancelReason;
        String orderNo = order.getOrderNo();
        //更新订单状态为退款中
        int updateResult = this.orderInfoMapper.updateOrderStatusForApplyRefund(order.getOrderNo(), null, reason);
        //添加订单流转日志
        if (updateResult > 0) {
//            applyRefund(order, operatorId, operatorName, cancelReason);
            logger.info("订单退款-添加流转日志开始-订单号={}", orderNo);
            Short oldStatus = order.getOrderStatus();
            String oldOrderJsonString = JsonUtil.toJSONString(order);
            order.setOrderStatus(OrderStatusEnum.REFUNDING.getStatusCode());
            String newOrderJsonString = JsonUtil.toJSONString(order);
            //添加订单流转日志
            orderChangeLogService.orderChange(orderNo, oldOrderJsonString, newOrderJsonString, oldStatus,
                    order.getOrderStatus(), operatorId, operatorName, reason, MainPointEnum.MAIN);
            logger.info("订单退款-添加流转日志结束-订单号={}", orderNo);
            //发送订单取消事件
            sendCancelEvent(order, reason, operatorId, operatorName);
        } else {
            logger.info("订单退款用户退款不成功 订单号={}", order.getOrderNo());
            throw new BusinessException(BusinessCode.ORDER_STATUS_CHANGE_FAILURE);
        }
    }

    /**
     * 发送订单取消事件
     *
     * @param order
     * @param cancelReason
     * @param operatorId
     * @param operatorName
     */
    private void sendCancelEvent(OrderInfo order, String cancelReason, Long operatorId, String operatorName) {
        order.setUpdatedBy(operatorId);
        order.setUpdatedByName(operatorName);
        order.setCancelReason(cancelReason);
        logger.info("订单发送取消事件开始-订单号={}", order.getOrderNo());
        eventMessageSender.send(EventType.EVENT_CUSTOMER_ORDER_CANCEL, order.getOrderNo(), order);
        logger.info("订单发送取消事件结束-订单号={}", order.getOrderNo());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderConfirm4Store(OrderConfirmCondition condition) {
        if (condition == null) {
            throw new NullPointerException("确认订单参数 OrderConfirmCondition 为空");
        }
        if (condition.getStoreId() == null) {
            throw new BusinessException(BusinessCode.STORE_ID_EMPTY);
        }
        if (StringUtils.isBlank(condition.getOrderNo())) {
            throw new BusinessException(BusinessCode.ORDER_NO_EMPTY);
        }
        OrderInfo orderInfo = orderInfoMapper.selectByOrderNo(condition.getOrderNo());
        if (orderInfo == null || orderInfo.getStoreId() != condition.getStoreId().longValue()) {
            throw new BusinessException(BusinessCode.WRONG_ORDERNO);
        }
        if (OrderStatusEnum.UNRECEIVED.getStatusCode() != orderInfo.getOrderStatus()) {
            throw new BusinessException(BusinessCode.WRONG_ORDER_STATUS);
        }
        if (orderInfo.getValuationType() == ValuationTypeEnum.OFFLINE_VALUATION.getTypeCode()) {
            if (condition.getOrderTotal() == null || condition.getOrderTotal().compareTo(BigDecimal.ZERO) < 1) {
                throw new BusinessException(BusinessCode.WRONG_ORDER_TOTAL_MONEY);
            }
            orderInfo.setOrderTotalMoney(condition.getOrderTotal());
            orderInfo.setRealPaymentMoney(condition.getOrderTotal());
            int num = orderInfoMapper.updateOrderMoney(condition.getOrderTotal(), condition.getOrderTotal(), orderInfo.getId());
            if (num != 1) {
                throw new BusinessException(BusinessCode.WRONG_ORDERNO);
            }
        }
        //调用订单接单业务流转接口
        getOrderHandler(orderInfo.getValuationType()).orderInfoConfirmProcess(orderInfo);
        //注册确认订单成功相关操作
        registerProcessAfterTransSuccess(new OrderConfirmSuccessRunnerble(orderInfo), null);
        logger.info("门店确认订单结束：condition={}", condition);
    }

    @Override
    public void orderPriceChange4Store(OrderConfirmCondition condition) {
        if (condition == null) {
            throw new NullPointerException("订单价格修改参数 OrderConfirmCondition 为空");
        }
        if (condition.getStoreId() == null) {
            throw new BusinessException(BusinessCode.STORE_ID_EMPTY);
        }
        if (StringUtils.isBlank(condition.getOrderNo())) {
            throw new BusinessException(BusinessCode.ORDER_NO_EMPTY);
        }
        OrderInfo orderInfo = orderInfoMapper.selectByOrderNo(condition.getOrderNo());
        if (orderInfo == null || orderInfo.getStoreId() != condition.getStoreId().longValue()) {
            throw new BusinessException(BusinessCode.WRONG_ORDERNO);
        }
        if (OrderStatusEnum.WAIT_PAY.getStatusCode() != orderInfo.getOrderStatus()) {
            throw new BusinessException(BusinessCode.WRONG_ORDER_STATUS);
        }
        if (orderInfo.getValuationType() != ValuationTypeEnum.OFFLINE_VALUATION.getTypeCode()) {
            throw new BusinessException(BusinessCode.ORDER_IS_NOT_OFFLINE_VALUATION);
        }
        if (condition.getOrderTotal() == null || condition.getOrderTotal().compareTo(BigDecimal.ZERO) < 1) {
            throw new BusinessException(BusinessCode.WRONG_ORDER_TOTAL_MONEY);
        }
        if (condition.getOrderTotal().compareTo(orderInfo.getOrderTotalMoney()) == 0) {
            throw new BusinessException(BusinessCode.WRONG_ORDER_TOTAL_MONEY_NO_CHANGE);
        }
        //TODO 调用支付中心 判断当前订单是否有支付信息
        if (1 != 1) {
            //如果已有支付单存在，则不允许修改支付价格
            throw new BusinessException(BusinessCode.ORDER_IS_BEING_PAID);
        }
        String oldJson = JsonUtil.toJSONString(orderInfo);
        orderInfo.setOrderTotalMoney(condition.getOrderTotal());
        orderInfo.setRealPaymentMoney(condition.getOrderTotal());
        String newJson = JsonUtil.toJSONString(orderInfo);
        int num = orderInfoMapper.updateOrderMoney(condition.getOrderTotal(), condition.getOrderTotal(), orderInfo.getId());
        if (num != 1) {
            throw new BusinessException(BusinessCode.WRONG_ORDERNO);
        }
        // 生产订单流转日志
        orderChangeLogService.orderChange(orderInfo.getOrderNo(), oldJson, newJson, null,
                orderInfo.getOrderStatus(), orderInfo.getCreatedBy(), orderInfo.getCreatedByName(),
                "门店修改线下计价订单价格", MainPointEnum.NOT_MAIN);
        logger.info("门店修改订单价格结束：condition={}", condition);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @StringMessageListener(value = MQHandler.ORDER_RECEIVE_TIMEOUT_DELAYED_HANDLER)
    public void orderReceiveTimeOut(String orderNo) {
        if (StringUtils.isBlank(orderNo)) {
            throw new BusinessException(BusinessCode.ORDER_NO_EMPTY);
        }
        String lockKey = CacheName.CACHE_KEY_STORE_PICK_UP_CODE_GENERATE + orderNo;
        Lock lock = new RedisLock(cache, lockKey, ORDER_UPDATE_LOCK_EXPIRES_TIME);
        try {
            lock.lock();
            OrderInfo orderInfo = orderInfoMapper.selectByOrderNo(orderNo);
            if (orderInfo == null) {
                throw new BusinessException(BusinessCode.WRONG_ORDERNO);
            }
            if (orderInfo.getOrderStatus() != OrderStatusEnum.UNRECEIVED.getStatusCode()) {
                // 如果非 带确认状态，直接返回
                logger.info("订单：{} 状态：{} 非待接单状态，无需进行超时取消操作", orderNo,
                        OrderStatusEnum.getMarkByCode(orderInfo.getOrderStatus()));
                return;
            }
            if (orderInfo.getPayStatus() == PayStatusEnum.PAID.getStatusCode()) {
                //退款
                orderApplyRefund(orderInfo, "超时未接单退款", orderInfo.getCreatedBy(), "sys");
            } else {
                //取消
                orderCancel(orderInfo, "超时未接单取消", orderInfo.getCreatedBy(), "sys", 5);
            }

            registerProcessAfterTransSuccess(new ReceiveTimeOutProcessSuccessRunnerable(orderInfo), null);
        } finally {
            lock.unlock();
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    @StringMessageListener(value = MQHandler.ORDER_PICKUP_TIMEOUT_DELAYED_HANDLER)
    public void orderPickupTimeOut(String orderNo) {
        if (StringUtils.isBlank(orderNo)) {
            throw new BusinessException(BusinessCode.ORDER_NO_EMPTY);
        }
        String lockKey = CacheName.CACHE_KEY_STORE_PICK_UP_CODE_GENERATE + orderNo;
        Lock lock = new RedisLock(cache, lockKey, ORDER_UPDATE_LOCK_EXPIRES_TIME);
        try {
            lock.lock();
            OrderInfo orderInfo = orderInfoMapper.selectByOrderNo(orderNo);
            if (orderInfo == null) {
                throw new BusinessException(BusinessCode.WRONG_ORDERNO);
            }
            if (orderInfo.getOrderStatus() != OrderStatusEnum.WAIT_SELF_LIFTING.getStatusCode()) {
                // 如果非 带确认状态，直接返回
                logger.info("订单：{} 状态：{} 非待自提状态，无需进行超时未自提操作", orderNo,
                        OrderStatusEnum.getMarkByCode(orderInfo.getOrderStatus()));
                return;
            }
            if (orderInfo.getPayStatus() == PayStatusEnum.PAID.getStatusCode()) {
                //退款
                orderApplyRefund(orderInfo, "超时未接单退款", null, "sys");
            }
            registerProcessAfterTransSuccess(new PickupTimeOutProcessSuccessRunnerable(orderInfo), null);
        } finally {
            lock.unlock();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderPickup4Store(OrderPickupCondition condition) {
        if (condition == null) {
            throw new NullPointerException("确认订单参数 OrderConfirmCondition 为空");
        }
        if (condition.getStoreId() == null) {
            throw new BusinessException(BusinessCode.STORE_ID_EMPTY);
        }
        if (StringUtils.isBlank(condition.getOrderNo())) {
            throw new BusinessException(BusinessCode.ORDER_NO_EMPTY);
        }
        OrderInfo orderInfo = orderInfoMapper.selectByOrderNo(condition.getOrderNo());
        if (orderInfo == null || orderInfo.getStoreId() != condition.getStoreId().longValue()) {
            throw new BusinessException(BusinessCode.WRONG_ORDERNO);
        }
        if (StringUtils.isBlank(condition.getPickupCode())
                || !condition.getPickupCode().equals(orderInfo.getPickupCode())) {
            throw new BusinessException(BusinessCode.WRONG_ORDER_PICKUP_CODE);
        }
        if (OrderStatusEnum.WAIT_SELF_LIFTING.getStatusCode() != orderInfo.getOrderStatus().shortValue()) {
            throw new BusinessException(BusinessCode.WRONG_ORDER_STATUS);
        }
        logger.info("订单：orderNo={} 自提收货开始", condition.getOrderNo());
        int ret = orderInfoMapper.orderPickup(condition.getPickupCode(), orderInfo.getId(),
                OrderStatusEnum.WAIT_SELF_LIFTING.getStatusCode(), OrderStatusEnum.FINISHED.getStatusCode());
        if (ret != 1) {
            throw new BusinessException(BusinessCode.ORDER_STATUS_CHANGE_FAILURE);
        }
        String oldOrderJsonString = JsonUtil.toJSONString(orderInfo);
        orderInfo.setOrderStatus(OrderStatusEnum.FINISHED.getStatusCode());
        orderInfo.setPickupCode(null);
        orderInfo.setUpdated(new Date());
        String newOrderJsonString = JsonUtil.toJSONString(orderInfo);
        //生产流水记录
        orderChangeLogService.orderChange(orderInfo.getOrderNo(), oldOrderJsonString, newOrderJsonString, orderInfo.getOrderStatus(),
                orderInfo.getOrderStatus(), orderInfo.getStoreId(), null,
                OrderStatusEnum.FINISHED.getStatusDes(), MainPointEnum.MAIN);
        registerProcessAfterTransSuccess(new OrderCompleteProcessRunnerble(orderInfo), null);
        logger.info("订单：orderNo={} 自提收货结束", condition.getOrderNo());
    }

    /**
     * 注册事务提交后相关操作
     *
     * @param rollBackRunnable
     * @author wangbin
     * @date 2018年8月3日 下午4:50:11
     */
    private void registerProcessAfterTransSuccess(Runnable commitRunnable, Runnable rollBackRunnable) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCompletion(int status) {
                super.afterCompletion(status);
                if (status == STATUS_COMMITTED && commitRunnable != null) {
                    threadPoolExecutor.execute(commitRunnable);
                } else if (status == STATUS_ROLLED_BACK && rollBackRunnable != null) {
                    threadPoolExecutor.execute(rollBackRunnable);
                }
            }
        });
    }

    private OrderInfo assembleOrderInfo(OrderCreateCondition orderCreateCondition) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreated(new Date());
        orderInfo.setCreatedBy(orderCreateCondition.getCustomerId());
        orderInfo.setCustomerId(orderCreateCondition.getCustomerId());
        orderInfo.setStoreId(orderCreateCondition.getStoreId());
        orderInfo.setPayStatus((short) PayStatusEnum.UNPAID.getStatusCode());
        orderInfo.setPayType(orderCreateCondition.getPayType());
        orderInfo.setPickupDateTime(orderCreateCondition.getPickupDateTime());
        orderInfo.setRemarks(orderCreateCondition.getRemark());
        if (orderInfo.getPickupDateTime() == null) {
            orderInfo.setPickupType((short) PickUpTypeEnum.SELF_PICK_UP_NOW.getTypeCode());
        } else {
            orderInfo.setPickupType((short) PickUpTypeEnum.SELF_PICK_UP_LATER.getTypeCode());
        }
        orderInfo.setOrderNo(generateOrderNo());
        StoreUserInfoVO storeUserInfoVO = getStoreUserInfoByStoreId(orderInfo.getStoreId());
        orderInfo.setRegionCode(storeUserInfoVO.getStoreRegionCode());
        //组装订单商品信息
        BigDecimal orderTotal = aseembleAndCalculateOrderItems(orderCreateCondition, orderInfo);
        //计算订单总金额
        short valuationType = ValuationTypeEnum.ONLINE_VALUATION.getTypeCode();
        if (orderTotal == null) {
            //待计价订单
            valuationType = ValuationTypeEnum.OFFLINE_VALUATION.getTypeCode();
        }
        orderInfo.setOrderTotalMoney(orderTotal);
        orderInfo.setValuationType(valuationType);
        //优惠券相关优惠计算
        calculateDiscounts(orderInfo, orderCreateCondition.getCouponIds());
        return orderInfo;
    }

    /**
     * 获取门店信息
     *
     * @param storeId
     * @return
     * @author wangbin
     * @date 2018年8月9日 下午3:07:42
     */
    private StoreUserInfoVO getStoreUserInfoByStoreId(Long storeId) {
        ResponseResult<StoreUserInfoVO> storeRet = storeServiceClient.findStoreUserInfo(storeId);
        if (storeRet == null || storeRet.getCode() != BusinessCode.CODE_OK || storeRet.getData() == null) {
            throw new BusinessException(BusinessCode.WRONG_STORE_ID);
        }
        logger.info("根据storeId={}获取门店信息成功，门店信息：{}", storeId, storeRet.getData());
        return storeRet.getData();
    }

    /**
     * 订单优惠计算
     *
     * @param orderInfo
     * @param couponIds
     * @author wangbin
     * @date 2018年8月3日 上午11:31:48
     */
    private void calculateDiscounts(OrderInfo orderInfo, Long[] couponIds) {
        if (orderInfo.getOrderTotalMoney() == null) {
            logger.info("订单orderNo：{}计算优惠金额时还没有计价，无法进行优惠计算", orderInfo.getOrderNo());
            return;
        }
        if (couponIds == null || couponIds.length < 1) {
            //不使用优惠券
            logger.info("订单：{} 不使用优惠券，不计算优惠券金额", orderInfo.getOrderNo());
            orderInfo.setRealPaymentMoney(orderInfo.getOrderTotalMoney());
            return;
        }
        BigDecimal couponDiscountAmount = getCouponDiscountFromPromotionSystem(orderInfo, couponIds);
        orderInfo.setCouponHxdMoney(couponDiscountAmount);
        orderInfo.setCouponBrandMoney(BigDecimal.ZERO);
        orderInfo.setRandomReductionMoney(BigDecimal.ZERO);
        orderInfo.setRealPaymentMoney(orderInfo.getOrderTotalMoney().subtract(orderInfo.getCouponBrandMoney()).subtract(orderInfo.getCouponHxdMoney()).subtract(orderInfo.getRandomReductionMoney()));
        //通知促销系统优惠券使用情况
        notifyPromotionSystem(orderInfo, couponIds);
    }


    public BigDecimal getCouponDiscountFromPromotionSystem(OrderInfo orderInfo, Long[] couponIds) {
        CouponPreAmountCondition couponPreAmountCondition = assembleCouponPreAmountCondition(orderInfo);
        couponPreAmountCondition.setSendIds(Arrays.asList(couponIds));
        ResponseResult<CouponDiscountVO> ret = couponServiceClient.couponDiscountAmount(couponPreAmountCondition);
        if (ret == null || ret.getCode() != BusinessCode.CODE_OK || ret.getData() == null) {
            //优惠券优惠金额计算失败
            logger.error("订单：{}优惠券优惠金额接口调用失败:code={}，创建订单异常！~", orderInfo.getOrderNo(), ret == null ? null : ret.getCode());
            throw new BusinessException(BusinessCode.CODE_401009);
        }
        BigDecimal couponDiscountAmount = ret.getData().getDiscountAmount();
        if (couponDiscountAmount == null) {
            logger.error("订单：{}优惠券优惠金额接口返回数据为空:couponDiscountAmount={}，创建订单异常！~", orderInfo.getOrderNo(), couponDiscountAmount);
            throw new BusinessException(BusinessCode.CODE_401009);
        }
        orderInfo.setCouponTitles(ret.getData().getCouponTitle());
        logger.error("订单：{}优惠券优惠金额接口返回数据为:couponDiscountAmount={},couponTitles={}", orderInfo.getOrderNo(), couponDiscountAmount, orderInfo.getCouponTitles());
        return couponDiscountAmount;
    }


    public void notifyPromotionSystem(OrderInfo orderInfo, Long[] couponIds) {
        OrderUseCouponCondition orderUseCouponCondition = new OrderUseCouponCondition();
        orderUseCouponCondition.setCouponPrice(orderInfo.getCouponHxdMoney());
        orderUseCouponCondition.setOrderNo(orderInfo.getOrderNo());
        orderUseCouponCondition.setOrderPrice(orderInfo.getOrderTotalMoney());
        orderUseCouponCondition.setSendIds(Arrays.asList(couponIds));
        ResponseResult<Boolean> ret = couponServiceClient.orderUseCoupon(orderUseCouponCondition);
        if (ret == null || ret.getCode() != BusinessCode.CODE_OK || !ret.getData()) {
            //优惠券使用失败
            logger.error("订单：{}优惠券使用更新接口调用失败:code={}，创建订单异常！~", orderInfo.getOrderNo(), ret == null ? null : ret.getCode());
            throw new BusinessException(BusinessCode.CODE_401009);
        }
        logger.info("订单:{},优惠券 couponIds={},使用接口调用成功。", orderInfo.getOrderNo(), Arrays.toString(couponIds));
    }

    private CouponPreAmountCondition assembleCouponPreAmountCondition(OrderInfo orderInfo) {
        List<CouponProductCondition> couponProductConditions = new ArrayList<>();

        for (Iterator<OrderItem> iterator = orderInfo.getOrderItems().iterator(); iterator.hasNext(); ) {
            OrderItem orderItem = iterator.next();
            CouponProductCondition couponProductCondition = new CouponProductCondition();
            couponProductCondition.setSkuNum(orderItem.getAmount());
            couponProductCondition.setPrice(orderItem.getPrice());
            couponProductCondition.setSkuCode(orderItem.getSkuCode());
            couponProductConditions.add(couponProductCondition);

        }
        Map<String, ProductSkuVO> skuInfoMap = SKU_INFO_MAP_THREADLOCAL.get();
        for (Iterator<CouponProductCondition> iterator = couponProductConditions.iterator(); iterator.hasNext(); ) {
            CouponProductCondition couponProductCondition = iterator.next();
            if (skuInfoMap != null && skuInfoMap.size() > 0) {
                couponProductCondition.setBrandCode(skuInfoMap.get(couponProductCondition.getSkuCode()).getBrandCode());
            }
        }
        CouponPreAmountCondition couponPreAmountCondition = new CouponPreAmountCondition();
        couponPreAmountCondition.setProducts(couponProductConditions);
        SKU_INFO_MAP_THREADLOCAL.remove();
        return couponPreAmountCondition;
    }


    private OrderHandler getOrderHandler(short valuationType) {
        if (valuationType == ValuationTypeEnum.ONLINE_VALUATION.getTypeCode()) {
            return onlinePayPickUpInStoreOrderHandler;
        } else if (valuationType == ValuationTypeEnum.OFFLINE_VALUATION.getTypeCode()) {
            return onlinePayPickUpInStoreOfflineOrderHandler;
        }
        throw new BusinessException(BusinessCode.CODE_401008);
    }

    /**
     * 订单下单条件校验
     *
     * @param orderCreateCondition
     * @author wangbin
     * @date 2018年8月3日 上午11:31:12
     */
    private void validateOrderCreateCondition(OrderCreateCondition orderCreateCondition) {
        if (orderCreateCondition.getCustomerId() == null) {
            throw new BusinessException(BusinessCode.CODE_401001);
        }
        if (orderCreateCondition.getStoreId() == null) {
            throw new BusinessException(BusinessCode.STORE_ID_EMPTY);
        }
        if (orderCreateCondition.getPayType() == null || PayTypeEnum.getPayTypeEnumByTypeCode(orderCreateCondition.getPayType()) == null) {
            throw new BusinessException(BusinessCode.CODE_401003);
        }
        if (orderCreateCondition.getOrderItemConditions() == null || orderCreateCondition.getOrderItemConditions().isEmpty()) {
            throw new BusinessException(BusinessCode.CODE_401005);
        }
        for (Iterator<OrderItemCondition> iterator = orderCreateCondition.getOrderItemConditions().iterator(); iterator.hasNext(); ) {
            OrderItemCondition condition = iterator.next();
            if (condition.getAmount() == null || condition.getAmount().intValue() < 1) {
                throw new BusinessException(BusinessCode.CODE_401006);
            }
            if (StringUtils.isBlank(condition.getSkuCode())) {
                throw new BusinessException(BusinessCode.CODE_401007);
            }
        }
    }

    /**
     * 组装订单商品项明细
     *
     * @param orderCreateCondition
     * @param orderInfo
     * @author wangbin
     * @date 2018年8月3日 上午11:30:53
     */
    private BigDecimal aseembleAndCalculateOrderItems(OrderCreateCondition orderCreateCondition, OrderInfo orderInfo) {
        BigDecimal orderTotal = BigDecimal.ZERO;
        List<OrderItem> items = new ArrayList<>();
        int skuQuantity = 0;
        int skuCategoryQuantity = 0;
        List<String> skuCodes = new ArrayList<>();
        for (Iterator<OrderItemCondition> iterator = orderCreateCondition.getOrderItemConditions().iterator(); iterator.hasNext(); ) {
            OrderItemCondition condition = iterator.next();
            skuQuantity += condition.getAmount();
            skuCategoryQuantity += 1;
            OrderItem item = new OrderItem();
            BeanUtils.copyProperties(condition, item);
            item.setOrderNo(orderInfo.getOrderNo());
            item.setCreated(orderInfo.getCreated());
            item.setCreatedBy(orderInfo.getCreatedBy());
            item.setCreatedByName(orderInfo.getCreatedByName());
            skuCodes.add(item.getSkuCode());
            items.add(item);
        }
        if (items.isEmpty()) {
            throw new BusinessException(BusinessCode.CODE_401005);
        }
        //查询商品相关信息
        Map<String, ProductSkuVO> skuInfoMap = querySkuInfos(orderInfo, skuCodes);
        if (skuInfoMap != null && skuInfoMap.size() > 0) {
            SKU_INFO_MAP_THREADLOCAL.set(skuInfoMap);
            for (Iterator<OrderItem> iterator = items.iterator(); iterator.hasNext(); ) {
                OrderItem orderItem = iterator.next();
                ProductSkuVO skuVO = skuInfoMap.get(orderItem.getSkuCode());
                if (skuVO != null) {
                    orderItem.setSkuDesc(skuVO.getSkuName() + "/" + skuVO.getSkuAttributeOption());
                    orderItem.setSkuUrl(skuVO.getSkuImage());
                }
            }
        }
        orderInfo.setSkuCategoryQuantity(skuCategoryQuantity);
        orderInfo.setSkuQuantity(skuQuantity);
        orderInfo.setOrderItems(items);
        //查询计算商品价格信息
        Map<String, ShopCartProdVO> storeProdVOMap = queryStoreSkuInfos(orderInfo, skuCodes);
        if (storeProdVOMap != null && storeProdVOMap.size() > 0) {
            for (Iterator<OrderItem> iterator = items.iterator(); iterator.hasNext(); ) {
                OrderItem orderItem = iterator.next();
                ShopCartProdVO cartProdVO = storeProdVOMap.get(orderItem.getSkuCode());
                if (cartProdVO != null) {
                    BigDecimal price = cartProdVO.getSellMoney();
                    if (price == null) {
                        return null;
                    }
                    orderItem.setPrice(price);
                    orderTotal = orderTotal.add(price.multiply(new BigDecimal(orderItem.getAmount()))
                            .setScale(ORDER_MONEY_SCALE, RoundingMode.HALF_UP));
                } else {
                    throw new BusinessException(BusinessCode.CODE_401005);
                }
            }
        } else {
            throw new BusinessException(BusinessCode.CODE_401005);
        }
        return orderTotal;
    }


    public Map<String, ProductSkuVO> querySkuInfos(OrderInfo orderInfo, List<String> skuCodes) {
        ProductCondition productCondition = new ProductCondition();
        productCondition.setProductSkus(skuCodes);
        productCondition.setSearchSkuCode(SearchSkuCodeEnum.IN_SKU_CODE);
        ResponseResult<List<ProductSkuVO>> ret = productServiceClient.getProductSkus(productCondition);
        if (ret == null || ret.getCode() != BusinessCode.CODE_OK || ret.getData() == null) {
            // 优惠券使用失败
            logger.error("订单：{}商品：skuCodes={}, 返回结果:code={} 商品库中不存在，创建订单异常！~", orderInfo.getOrderNo(),
                    Arrays.toString(skuCodes.toArray(new String[skuCodes.size()])), ret == null ? null : ret.getCode());
            throw new BusinessException(BusinessCode.CODE_401005);
        }
        Map<String, ProductSkuVO> skuBrandMap = new HashMap<>();
        for (Iterator<ProductSkuVO> iterator = ret.getData().iterator(); iterator.hasNext(); ) {
            ProductSkuVO productSkuVO = iterator.next();
            skuBrandMap.put(productSkuVO.getSkuCode(), productSkuVO);
        }
        return skuBrandMap;
    }

    public Map<String, ShopCartProdVO> queryStoreSkuInfos(OrderInfo orderInfo, List<String> skuCodes) {
        ResponseResult<List<ShopCartProdVO>> ret = storeServiceClient.findShopCarProd(skuCodes, orderInfo.getStoreId());
        if (ret == null || ret.getCode() != BusinessCode.CODE_OK || ret.getData() == null) {
            // 优惠券使用失败
            logger.error("订单：{}商品：skuCodes={}, 返回结果:code={} 门店商品库中不存在，创建订单异常！~", orderInfo.getOrderNo(),
                    Arrays.toString(skuCodes.toArray(new String[skuCodes.size()])), ret == null ? null : ret.getCode());
            throw new BusinessException(BusinessCode.CODE_401005);
        }
        Map<String, ShopCartProdVO> storeSkuInfoMap = new HashMap<>();
        for (Iterator<ShopCartProdVO> iterator = ret.getData().iterator(); iterator.hasNext(); ) {
            ShopCartProdVO shopCartProdVO = iterator.next();
            storeSkuInfoMap.put(shopCartProdVO.getSkuCode(), shopCartProdVO);
        }
        return storeSkuInfoMap;
    }

    /**
     * 生成订单号  格式：C+YYMMDDHH(年月日小时8位)XXXXXXXXX(9位随机吗)= 18位
     *
     * @return
     * @author wangbin
     * @date 2018年8月3日 上午11:34:29
     */
    private String generateOrderNo() {
        String orderNo = null;
        Date date = new Date();
        do {
            UUID uuid = UUID.randomUUID();
            int hashCodeV = (uuid.toString() + System.currentTimeMillis()).hashCode();
            if (hashCodeV < 0) {
                // 有可能是负数
                hashCodeV = -hashCodeV;
            }
            while (hashCodeV > 999999999) {
                hashCodeV = hashCodeV >> 1;
            }
            String orderNoDateTimeFormatter = "yyMMddHH";
            // 0 代表前面补充0       
            // 9 代表长度为4       
            // d 代表参数为正数型 
            String randomFormat = "%09d";
            orderNo = "C" + DateFormatUtils.format(date, orderNoDateTimeFormatter) + String.format(randomFormat, hashCodeV);
        } while (!checkOrderNoAvailable(orderNo, date));
        return orderNo;
    }

    /**
     * 校验订单号生成是否重复
     *
     * @param orderNo
     * @param date
     * @return
     * @author wangbin
     * @date 2018年8月11日 下午5:32:33
     */
    private boolean checkOrderNoAvailable(String orderNo, Date date) {

        String orderNoDateTimeFormatter = "yyMMddHH";
        String val = cache.hget(CacheName.CACHE_KEY_ORDERNO_CHECK_EXISTS + DateFormatUtils.format(date, orderNoDateTimeFormatter), orderNo);
        if (StringUtils.isNotBlank(val)) {
            logger.info("订单号生成出现重复：orderNo={}", orderNo);
            return false;
        } else {
            cache.hset(CacheName.CACHE_KEY_ORDERNO_CHECK_EXISTS + DateFormatUtils.format(date, orderNoDateTimeFormatter), orderNo, orderNo);
            //下一小时就过期
            Long expires = (DateUtils.truncate(DateUtils.addHours(date, 1), Calendar.HOUR_OF_DAY).getTime() - date.getTime()) / 1000;
            cache.expire(CacheName.CACHE_KEY_ORDERNO_CHECK_EXISTS + DateFormatUtils.format(date, orderNoDateTimeFormatter), expires.intValue());
            return true;
        }
    }

    /**
     * 订单支付成功 处理
     *
     * @author wangbin
     * @date 2018年8月8日 下午3:16:37
     */
    private class PaySuccessProcessRunnerble implements Runnable {

        private OrderInfo orderInfo;

        public PaySuccessProcessRunnerble(OrderInfo orderInfo) {
            super();
            this.orderInfo = orderInfo;
        }

        @Override
        public void run() {
            getOrderHandler(orderInfo.getValuationType())
                    .orderInfoAfterPaySuccessProcess(orderInfo);
        }
    }

    /**
     * 订单创建成功 处理
     *
     * @author wangbin
     * @date 2018年8月8日 下午3:16:17
     */
    private class SubmitSuccessProcessRunnerble implements Runnable {

        private OrderInfo orderInfo;

        public SubmitSuccessProcessRunnerble(OrderInfo orderInfo) {
            super();
            this.orderInfo = orderInfo;
        }

        @Override
        public void run() {
            getOrderHandler(orderInfo.getValuationType())
                    .orderInfoAfterCreateSuccessProcess(orderInfo);
        }
    }

    /**
     * 订单创建失败 处理
     *
     * @author wangbin
     * @date 2018年8月8日 下午3:16:17
     */
    private class SubmitFailureProcessRunnerble implements Runnable {

        private OrderInfo orderInfo;

        private Long[] couponIds;

        public SubmitFailureProcessRunnerble(OrderInfo orderInfo, Long[] couponIds) {
            super();
            this.orderInfo = orderInfo;
            this.couponIds = couponIds;
        }

        @Override
        public void run() {
            //订单提交失败，退回优惠券
            if (couponIds != null && couponIds.length > 0) {
                logger.info("订单：{} 提交失败，进行优惠券回退操作.", orderInfo.getOrderNo());
                OrderUntreadCouponCondition orderUntreadCouponCondition = new OrderUntreadCouponCondition();
                orderUntreadCouponCondition.setOrderNo(orderInfo.getOrderNo());
                //状态置为退回
                orderUntreadCouponCondition.setStatus("4");
                ResponseResult<Boolean> ret = couponServiceClient.orderUntreadCoupon(orderUntreadCouponCondition);
                if (ret == null || ret.getCode() != BusinessCode.CODE_OK || !ret.getData()) {
                    logger.info("订单：{} 提交失败，进行优惠券回退操作失败，code={}.", orderInfo.getOrderNo(), ret == null ? null : ret.getCode());
                } else {
                    logger.info("订单：{} 提交失败，进行优惠券回退操作成功，couponIds={}.", orderInfo.getOrderNo(), Arrays.toString(couponIds));
                }
            } else {
                logger.info("订单：{} 提交失败，没有使用优惠券，不需要进行优惠券回退操作", orderInfo.getOrderNo());
            }
        }
    }

    /**
     * 订单提货完成 处理
     *
     * @author wangbin
     * @date 2018年8月8日 下午3:16:17
     */
    private class OrderCompleteProcessRunnerble implements Runnable {

        private OrderInfo orderInfo;

        public OrderCompleteProcessRunnerble(OrderInfo orderInfo) {
            super();
            this.orderInfo = orderInfo;
        }

        @Override
        public void run() {
            //支付成功清空门店订单销量统计cache
            cache.del(OrderUtil.getStoreOrderSalesSummaryKey(orderInfo.getStoreId()));
            try {
                String last4MobileNums;
                CustomerUserInfoVO customerUserInfoVO = getCustomerUserInfoVO(orderInfo.getCustomerId());
                if (StringUtils.isBlank(customerUserInfoVO.getCustomerMobile())) {
                    logger.info("用户customerId={}，未找到手机号", orderInfo.getCustomerId());
                    last4MobileNums = "";
                } else {
                    last4MobileNums = StringUtils.substring(customerUserInfoVO.getCustomerMobile(), 7);
                }
                String storeMsg = MessageFormat.format(OrderNotifyMsg.ORDER_COMPLETE_MSG_4_STORE, last4MobileNums);
                String createdBy = "";
                int expiration = 0;
                int msgType = 0;
                short pageType = 1;
                int audioType = 0;
                String treeCode = "treeCode";
                NeteaseMsgCondition neteaseMsgCondition = OrderUtil.genNeteaseMsgCondition(orderInfo.getStoreId(), storeMsg, createdBy, expiration, msgType,
                        pageType, audioType, treeCode);
                if (messageServiceClient.sendNeteaseMsg(neteaseMsgCondition).getCode() != BusinessCode.CODE_OK) {
                    throw new BusinessException(BusinessCode.CODE_1001);
                }
            } catch (Exception e) {
                logger.error("订单提货完成给门店发送消息失败：", e);
            }
            //给用户发信息
            try {
                String customerMsg = OrderNotifyMsg.ORDER_COMPLETE_MSG_4_CUSTOMER;
                String openid = getCustomerUserInfoVO(orderInfo.getCustomerId()).getOpenid();
                String page = null;
                MiniTemplateData data = new MiniTemplateData();
                data.setKeyName("keyword1");
                data.setValue(customerMsg);
                short msgType2C = MiniMsgTypeEnum.USER_PICK_UP_GOODS.getMsgType();
                MiniMsgCondition miniMsgCondition = OrderUtil.genMiniMsgCondition(openid, page, msgType2C);
                if (messageServiceClient.sendMiniMsg(miniMsgCondition).getCode() != BusinessCode.CODE_OK) {
                    throw new BusinessException(BusinessCode.CODE_1001);
                }
            } catch (Exception e) {
                logger.error("订单提货完成给用户发送消息失败：", e);
            }
            // 发送mq完成消息
            eventMessageSender.send(EventType.EVENT_CUSTOMER_ORDER_FINISHED, orderInfo.getOrderNo(), orderInfo);
        }
    }

    /**
     * s
     * 订单门店确认完成 处理
     *
     * @author wangbin
     * @date 2018年8月8日 下午3:16:17
     */
    private class OrderConfirmSuccessRunnerble implements Runnable {

        private OrderInfo orderInfo;

        public OrderConfirmSuccessRunnerble(OrderInfo orderInfo) {
            super();
            this.orderInfo = orderInfo;
        }

        @Override
        public void run() {
            getOrderHandler(orderInfo.getValuationType())
                    .orderInfoAfterConfirmSuccessProcess(orderInfo);
        }
    }

    /**
     * 订单门店 超时未确认处理成功
     *
     * @author wangbin
     * @date 2018年8月13日 下午3:16:17
     */
    private class ReceiveTimeOutProcessSuccessRunnerable implements Runnable {

        private OrderInfo orderInfo;

        public ReceiveTimeOutProcessSuccessRunnerable(OrderInfo orderInfo) {
            super();
            this.orderInfo = orderInfo;
        }

        @Override
        public void run() {
            //给用户发信息
            try {
                String customerMsg = OrderNotifyMsg.ORDER_RECEIVE_TIMEOUT_MSG_4_CUSTOMER;
                String openid = getCustomerUserInfoVO(orderInfo.getCustomerId()).getOpenid();
                String page = null;
                MiniTemplateData data = new MiniTemplateData();
                data.setKeyName("keyword1");
                data.setValue(customerMsg);
                short msgType2C = MiniMsgTypeEnum.STORE_NOT_CONFIRM_TIMEOUT.getMsgType();
                MiniMsgCondition miniMsgCondition = OrderUtil.genMiniMsgCondition(openid, page, msgType2C);
                if (messageServiceClient.sendMiniMsg(miniMsgCondition).getCode() != BusinessCode.CODE_OK) {
                    throw new BusinessException(BusinessCode.CODE_1001);
                }
            } catch (Exception e) {
                logger.error("订单未接单超时给用户发送消息失败：", e);
            }
        }
    }

    /**
     * 订单客户 超时未提货处理成功
     *
     * @author wangbin
     * @date 2018年8月13日 下午3:16:17
     */
    private class PickupTimeOutProcessSuccessRunnerable implements Runnable {

        private OrderInfo orderInfo;

        public PickupTimeOutProcessSuccessRunnerable(OrderInfo orderInfo) {
            super();
            this.orderInfo = orderInfo;
        }

        @Override
        public void run() {
            //客户信息发送
            String customerMsg;
            short msgType2C;
            if (orderInfo.getPayStatus().shortValue() == PayStatusEnum.PAID.getStatusCode()) {
                customerMsg = OrderNotifyMsg.ORDER_PICKUP_ALREADY_PAID_TIMEOUT_MSG_4_CUSTOMER;
                msgType2C = MiniMsgTypeEnum.USER_TIMEOUT_NO_GOODS_PAID.getMsgType();
            } else {
                customerMsg = OrderNotifyMsg.ORDER_PICKUP_UNPAID_TIMEOUT_MSG_4_CUSTOMER;
                msgType2C = MiniMsgTypeEnum.USER_TIMEOUT_NO_GOODS_NO_PAYMENT.getMsgType();
            }
            try {
                String openid = getCustomerUserInfoVO(orderInfo.getCustomerId()).getOpenid();
                String page = null;
                MiniTemplateData data = new MiniTemplateData();
                data.setKeyName("keyword1");
                data.setValue(customerMsg);
                MiniMsgCondition miniMsgCondition = OrderUtil.genMiniMsgCondition(openid, page, msgType2C);
                if (messageServiceClient.sendMiniMsg(miniMsgCondition).getCode() != BusinessCode.CODE_OK) {
                    throw new BusinessException(BusinessCode.CODE_1001);
                }
            } catch (Exception e) {
                logger.error("订单未提货超时给用户发送消息失败：", e);
            }
        }
    }

    /**
     * 订单取消事务提交成功后 处理
     *
     * @author pangjianhua
     * @date 2018年8月11日 下午3:16:17
     */
    private class OrderCancelCompleteProcessRunnable implements Runnable {

        private OrderInfo orderInfo;
        private int type;

        public OrderCancelCompleteProcessRunnable(OrderInfo orderInfo, int type) {
            super();
            this.orderInfo = orderInfo;
            this.type = type;
        }

        @Override
        public void run() {
            switch (type) {
                case 1:
                    // 给门店【已取消】手机尾号8513张先生已取消订单
                    CustomerUserInfoVO customer = getCustomerUserInfoVO(orderInfo.getCustomerId());
                    String mobileStr = OrderUtil.getLast4Mobile(customer.getCustomerMobile());
                    NeteaseMsgCondition neteaseMsgCondition = new NeteaseMsgCondition();
                    neteaseMsgCondition.setCustomerId(orderInfo.getStoreId());
                    String storeMsgContent = "【已取消】手机尾号" + mobileStr + "的顾客已取消订单";
                    NeteaseMsg neteaseMsg = new NeteaseMsg();
                    neteaseMsg.setMsgContent(storeMsgContent);
                    neteaseMsg.setAudioType(0);
                    neteaseMsgCondition.setNeteaseMsg(neteaseMsg);
                    messageServiceClient.sendNeteaseMsg(neteaseMsgCondition);

                    //给申请用户 “您的订单已取消”。
                    NeteaseMsgCondition customerNeteaseMsgCondition = new NeteaseMsgCondition();
                    customerNeteaseMsgCondition.setCustomerId(orderInfo.getCustomerId());
                    String customerMsgContent = "您的订单已取消";
                    NeteaseMsg customerNeteaseMsg = new NeteaseMsg();
                    neteaseMsg.setMsgContent(customerMsgContent);
                    neteaseMsg.setAudioType(0);
                    customerNeteaseMsgCondition.setNeteaseMsg(customerNeteaseMsg);
                    break;
                case 2:
                    // 发送云信给订单用户 小店因为{取消原因}取消了订单，请再看看其他的商品吧
                    NeteaseMsgCondition customerNeteaseMsgConditionType2 = new NeteaseMsgCondition();
                    customerNeteaseMsgConditionType2.setCustomerId(orderInfo.getCustomerId());
                    String customerMsgContentType2 = "小店因为{" + orderInfo.getCancelReason() + "}取消了订单，请再看看其他的商品吧";
                    NeteaseMsg customerNeteaseMsgType2 = new NeteaseMsg();
                    customerNeteaseMsgType2.setMsgContent(customerMsgContentType2);
                    customerNeteaseMsgType2.setAudioType(0);
                    customerNeteaseMsgConditionType2.setNeteaseMsg(customerNeteaseMsgType2);
                    break;
                default:
            }
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

    /**
     * 获取订单
     *
     * @param orderNo 订单号
     * @return {@link OrderInfo}
     */
    private OrderInfo getOrderInfo(String orderNo) {
        OrderInfo order = orderInfoMapper.selectByOrderNo(orderNo);
        if (null == order) {
            logger.info("订单不存在 订单号={}", orderNo);
            throw new BusinessException(BusinessCode.ORDER_DOES_NOT_EXIST, "订单不存在");
        }
        return order;
    }

    /**
     * 退款给门店发送消息
     *
     * @param type      类型 1：1小时，2：一天
     * @param orderInfo
     */
    private void orderRefundTimeOutSendMsg(int type, OrderInfo orderInfo) {
        CustomerUserInfoVO customer = getCustomerUserInfoVO(orderInfo.getCustomerId());
        String mobileStr = OrderUtil.getLast4Mobile(customer.getCustomerMobile());
        NeteaseMsgCondition neteaseMsgCondition = new NeteaseMsgCondition();
        neteaseMsgCondition.setCustomerId(orderInfo.getStoreId());
        //【申请退款】手机尾号8513顾客申请退款，系统1天后将自动退款
        String msgContent = "";
        switch (type) {
            case 1:
                msgContent = "【申请退款】手机尾号" + mobileStr + "顾客申请退款，系统1小时后将自动退款";
                break;
            case 2:
                msgContent = "【申请退款】手机尾号" + mobileStr + "顾客申请退款，系统1天后将自动退款";
                break;
            case 3:
                msgContent = "【退款中】手机尾号" + mobileStr + "顾客申请退款，超时3天系统已退款";
                break;
            case 4:
                //【已退款】手机尾号8513黄小姐退款已到账
                msgContent = "【已退款】手机尾号" + mobileStr + "顾客退款已到账";
                break;
            default:
        }
        if (StringUtils.isBlank(msgContent)) {
            return;
        }
        NeteaseMsg neteaseMsg = new NeteaseMsg();
        neteaseMsg.setMsgContent(msgContent);
        neteaseMsg.setAudioType(0);
        neteaseMsgCondition.setNeteaseMsg(neteaseMsg);
        messageServiceClient.sendNeteaseMsg(neteaseMsgCondition);
    }

}
