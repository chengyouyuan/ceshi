package com.winhxd.b2c.order.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
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
import com.winhxd.b2c.common.domain.order.condition.OrderCancelCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderConfirmCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderCreateCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderItemCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderPickupCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderRefundCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderRefundStoreHandleCondition;
import com.winhxd.b2c.common.domain.order.enums.OrderStatusEnum;
import com.winhxd.b2c.common.domain.order.enums.PayStatusEnum;
import com.winhxd.b2c.common.domain.order.enums.PayTypeEnum;
import com.winhxd.b2c.common.domain.order.enums.PickUpTypeEnum;
import com.winhxd.b2c.common.domain.order.enums.ValuationTypeEnum;
import com.winhxd.b2c.common.domain.order.model.OrderInfo;
import com.winhxd.b2c.common.domain.order.model.OrderItem;
import com.winhxd.b2c.common.domain.promotion.condition.OrderUntreadCouponCondition;
import com.winhxd.b2c.common.domain.promotion.condition.OrderUseCouponCondition;
import com.winhxd.b2c.common.domain.system.login.vo.CustomerUserInfoVO;
import com.winhxd.b2c.common.domain.system.login.vo.StoreUserInfoVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.customer.CustomerServiceClient;
import com.winhxd.b2c.common.feign.promotion.CouponServiceClient;
import com.winhxd.b2c.common.feign.store.StoreServiceClient;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.order.dao.OrderInfoMapper;
import com.winhxd.b2c.order.dao.OrderItemMapper;
import com.winhxd.b2c.order.service.OrderChangeLogService;
import com.winhxd.b2c.order.service.OrderChangeLogService.MainPointEnum;
import com.winhxd.b2c.order.service.OrderHandler;
import com.winhxd.b2c.order.service.OrderService;

@Service
public class CommonOrderServiceImpl implements OrderService {

    private static final int QUEUE_CAPACITY = 1000;
    private static final int KEEP_ALIVE_TIME = 50;
    private static final int MAXIMUM_POOL_SIZE = 20;
    private static final int CORE_POOL_SIZE = 5;
    private static final int ORDER_MONEY_SCALE = 2;
    private static final Logger logger = LoggerFactory.getLogger(CommonOrderServiceImpl.class);

    @Autowired
    @Qualifier("OnlinePayPickUpInStoreOrderHandler")
    private OrderHandler onlinePayPickUpInStoreOrderHandler;

    @Autowired
    @Qualifier("OnlinePayPickUpInStoreOfflineOrderHandler")
    private OrderHandler onlinePayPickUpInStoreOfflineOrderHandler;

    @Resource
    private OrderInfoMapper orderInfoMapper;

    @Resource
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderChangeLogService orderChangeLogService;
    @Resource
    private StoreServiceClient storeServiceClient;
    @Resource
    private CouponServiceClient couponServiceClient;
    @Resource
    private Cache cache;

    @Autowired
    private CustomerServiceClient customerServiceclient;

    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, new ArrayBlockingQueue<>(QUEUE_CAPACITY));

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public String submitOrder(OrderCreateCondition orderCreateCondition) {
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
        //注册订单创建成功事物提交后相关事件
        registerProcessAfterTransSuccess(new SubmitSuccessProcessRunnerble(orderInfo), new SubmitFailureProcessRunnerble(orderInfo, orderCreateCondition.getCouponIds()));
        return orderInfo.getOrderNo();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderPaySuccessNotify(String orderNo) {
        if (StringUtils.isBlank(orderNo)) {
            throw new NullPointerException("订单支付通知orderNo不能为空");
        }
        OrderInfo orderInfo = orderInfoMapper.selectByOrderNo(orderNo);
        if (orderInfo == null) {
            throw new BusinessException(BusinessCode.WRONG_ORDERNO);
        }
        if (PayStatusEnum.UNPAID.getStatusCode() != orderInfo.getPayStatus()) {
            throw new BusinessException(BusinessCode.ORDER_ALREADY_PAID);
        }
        logger.info("订单orderNo={}，支付通知处理开始.", orderNo);
        Date payFinishDateTime = new Date();
        int updNum = orderInfoMapper.updateOrderPayStatus(PayStatusEnum.PAID.getStatusCode(), payFinishDateTime, orderInfo.getId());
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
        //订单支付成功事物提交后相关事件
        registerProcessAfterTransSuccess(new PaySuccessProcessRunnerble(orderInfo), null);
        logger.info("订单orderNo：{} 支付后相关业务操作执行结束", orderInfo.getOrderNo());
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
            throw new BusinessException(BusinessCode.ORDER_NO_EMPTY, "订单号不能为空");
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
        Lock lock = new RedisLock(cache, lockKey, 1000);
        if (lock.tryLock()) {
            try {
                OrderInfo order = orderInfoMapper.selectByOrderNo(orderNo);
                if (null == order) {
                    logger.info("订单不存在 订单号={}", orderNo);
                    throw new BusinessException(BusinessCode.ORDER_DOES_NOT_EXIST, "订单不存在");
                }
                //判断是否支付成功,支付成功走退款逻辑，支付不成功走取消订单逻辑
                if (PayStatusEnum.PAID.getStatusCode() == order.getPayStatus()) {
                    orderRefund(order, orderCancelCondition.getCancelReason(), storeVO.getId(), storeVO.getShopkeeper());
                } else {
                    orderCancel(order, orderCancelCondition.getCancelReason(), storeVO.getId(), storeVO.getShopkeeper());
                }
            } finally {
                lock.unlock();
            }
        } else {
            throw new BusinessException(BusinessCode.ORDER_IS_BEING_MODIFIED, "订单正在修改中");
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
            throw new BusinessException(BusinessCode.ORDER_NO_EMPTY, "订单号不能为空");
        }
        String lockKey = CacheName.CACHE_KEY_STORE_PICK_UP_CODE_GENERATE + orderNo;
        Lock lock = new RedisLock(cache, lockKey, 1000);
        if (lock.tryLock()) {
            try {
                OrderInfo order = orderInfoMapper.selectByOrderNo(orderNo);
                if (null == order) {
                    logger.info("订单不存在 订单号={}", orderNo);
                    throw new BusinessException(BusinessCode.ORDER_DOES_NOT_EXIST, "订单不存在");
                }
                //判断是否支付成功,支付成功不让取消
                if (PayStatusEnum.PAID.getStatusCode() == order.getPayStatus()) {
                    logger.info("订单已支付成功不能取消，请走退款接口 订单号={}", orderNo);
                    throw new BusinessException(BusinessCode.WRONG_ORDER_STATUS, "订单已支付成功不能取消");
                }

                orderCancel(order, orderCancelCondition.getCancelReason(), user.getCustomerId(), null);
                //TODO 订单取消发送云信
            } finally {
                lock.unlock();
            }
        } else {
            throw new BusinessException(BusinessCode.ORDER_IS_BEING_MODIFIED, "订单正在修改中");
        }
    }

    /**
     * 订单取消service 超时订单和取消订单
     *
     * @param order        订单
     * @param cancelReason 取消原因
     * @param operatorId   操作人ID
     * @param operatorName 操作人名称
     */
    private void orderCancel(OrderInfo order, String cancelReason, Long operatorId, String operatorName) {
        String orderNo = order.getOrderNo();
        //设置提货码置为null、取消原因、取消状态等
        int updateRowNum = this.orderInfoMapper.updateOrderStatusForCancel(orderNo, cancelReason);
        if (updateRowNum < 1) {
            logger.info("取消订单-状态更新不成功-订单号={}", orderNo);
            throw new BusinessException(BusinessCode.WRONG_ORDER_STATUS, "取消订单状态更新不成功");
        } else {
            //优惠券一并退回
            logger.info("取消订单-退优惠券开始-订单号={}", orderNo);
            OrderUntreadCouponCondition couponCondition = new OrderUntreadCouponCondition();
            couponCondition.setOrderNo(orderNo);
            ResponseResult<Boolean> couponData = couponServiceClient.orderUntreadCoupon(couponCondition);
            if (couponData.getCode() != BusinessCode.CODE_OK || !couponData.getData()) {
                logger.info("取消订单-退优惠券返回数据失败-订单号={}", orderNo);
            }
            logger.info("取消订单-退优惠券结束-订单号={}", orderNo);
            logger.info("取消订单-添加流转日志开始-订单号={}", orderNo);
            String oldOrderJsonString = JsonUtil.toJSONString(order);
            Short oldStatus = order.getOrderStatus();
            order.setOrderStatus(OrderStatusEnum.CANCELED.getStatusCode());
            order.setCancelReason(cancelReason);
            String newOrderJsonString = JsonUtil.toJSONString(order);
            //添加订单流转日志
            orderChangeLogService.orderChange(order.getOrderNo(), oldOrderJsonString, newOrderJsonString, oldStatus,
                    order.getOrderStatus(), operatorId, operatorName, cancelReason, MainPointEnum.MAIN);
            logger.info("取消订单-添加流转日志结束-订单号={}", orderNo);
        }
    }

    /**
     * 门店处理用户退款订单
     *
     * @param condition 入参
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleOrderRefundByStore(OrderRefundStoreHandleCondition condition) {
        if (StringUtils.isBlank(condition.getOrderNo()) || null == condition.getAgree()) {
            throw new BusinessException(BusinessCode.CODE_422001, "参数异常");
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
        Lock lock = new RedisLock(cache, lockKey, 1000);
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
                    orderRefund(order, null, storeVO.getId(), storeVO.getShopkeeper());
                    logger.info("门店同意退款-操作订单结束-订单号={}", orderNo);
                }
            } finally {
                lock.unlock();
            }
        } else {
            throw new BusinessException(BusinessCode.ORDER_IS_BEING_MODIFIED, "订单正在修改中");
        }
    }

    /**
     * 订单退款service
     *
     * @param orderNo 订单编号
     * @param store   门店信息
     */
    private void orderRefund(OrderInfo order, String cancelReason, Long operatorId, String operatorName) {
        if (order.getPayStatus().equals(PayStatusEnum.UNPAID.getStatusCode())) {
            throw new BusinessException(BusinessCode.WRONG_ORDER_STATUS, "未支付的订单不允许退款");
        }
        if (order.getOrderStatus().equals(OrderStatusEnum.FINISHED.getStatusCode())) {
            throw new BusinessException(BusinessCode.ORDER_ALREADY_PAID, "已完成的订单不允许退款");
        }
        String reason = StringUtils.isBlank(cancelReason) ? order.getCancelReason() : cancelReason;
        String orderNo = order.getOrderNo();
        //更新订单状态为退款中
        int updateResult = this.orderInfoMapper.updateOrderStatusForRefund(order.getOrderNo(), reason);
        //添加订单流转日志
        if (updateResult > 0) {
            //TODO 调用订单退款接口 请求返回成功状态改为退款中
            //退优惠券
            logger.info("订单取消处理用户退款-退优惠券开始-订单号={}", orderNo);
            OrderUntreadCouponCondition couponCondition = new OrderUntreadCouponCondition();
            couponCondition.setOrderNo(orderNo);
            //状态置为退回
            couponCondition.setStatus("4");
            ResponseResult<Boolean> couponData = couponServiceClient.orderUntreadCoupon(couponCondition);
            if (couponData.getCode() != BusinessCode.CODE_OK || !couponData.getData()) {
                logger.info("订单取消处理用户退款-退优惠券返回数据失败-订单号={}", orderNo);
            }
            logger.info("订单取消处理用户退款-退优惠券结束-订单号={}", orderNo);
            logger.info("订单取消处理用户退款-添加流转日志开始-订单号={}", orderNo);
            Short oldStatus = order.getOrderStatus();
            String oldOrderJsonString = JsonUtil.toJSONString(order);
            order.setOrderStatus(OrderStatusEnum.REFUNDED.getStatusCode());
            String newOrderJsonString = JsonUtil.toJSONString(order);
            //添加订单流转日志
            orderChangeLogService.orderChange(orderNo, oldOrderJsonString, newOrderJsonString, oldStatus,
                    order.getOrderStatus(), operatorId, operatorName, reason, MainPointEnum.MAIN);
            logger.info("取消订单-添加流转日志结束-订单号={}", orderNo);
        } else {
            logger.info("订单取消处理用户退款不成功 订单号={}", order.getOrderNo());
            throw new BusinessException(BusinessCode.ORDER_STATUS_CHANGE_FAILURE, "订单取消处理用户退款不成功");
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
            throw new BusinessException(BusinessCode.CODE_1002, "用户不存在");
        }
        Long customerId = customer.getCustomerId();
        CustomerUserInfoVO customerUserInfoVO =   getCustomerUserInfoVO(customerId);
        String lockKey = CacheName.CACHE_KEY_STORE_PICK_UP_CODE_GENERATE + orderNo;
        Lock lock = new RedisLock(cache, lockKey, 1000);
        if (lock.tryLock()) {
            try {
                OrderInfo order = orderInfoMapper.selectByOrderNo(orderNo);
                if (null == order || !order.getStoreId().equals(customer.getCustomerId())) {
                    throw new BusinessException(BusinessCode.ORDER_DOES_NOT_EXIST, "C端申请退款订单查询失败");
                }
                //判断订单状态是否可以申请退款
                Short status = order.getOrderStatus();
                if (status.equals(PayStatusEnum.UNPAID.getStatusCode())
                        || status.equals(OrderStatusEnum.FINISHED.getStatusCode())
                        || status.equals(OrderStatusEnum.CANCELED.getStatusCode())
                        || status.equals(OrderStatusEnum.REFUNDED.getStatusCode())) {
                    throw new BusinessException(BusinessCode.CODE_421002, "订单状态不允许退款");
                }
                //更新订单状态为待退款，并更新相关属性
                int updateResult = this.orderInfoMapper.updateOrderStatusForApplyRefund(orderNo, customerId);
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
                } else {
                    logger.info("订单取消-C端申请退款不成功 订单号={}", orderNo);
                    throw new BusinessException(BusinessCode.ORDER_STATUS_CHANGE_FAILURE, "C端申请退款不成功");
                }
            } finally {
                lock.unlock();
            }
        } else {
            throw new BusinessException(BusinessCode.ORDER_IS_BEING_MODIFIED, "订单正在修改中");
        }
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
        logger.info("门店确认订单结束：condition={}", condition);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderReceiveTimeOut(String orderNo) {
        if (StringUtils.isBlank(orderNo)) {
            throw new BusinessException(BusinessCode.ORDER_NO_EMPTY);
        }
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
        //TODO 退款或取消
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderPickupTimeOut(String orderNo) {
        if (StringUtils.isBlank(orderNo)) {
            throw new BusinessException(BusinessCode.ORDER_NO_EMPTY);
        }
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
        // TODO 退款
    }

    @Override
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
                || condition.getPickupCode().equals(orderInfo.getPickupCode())) {
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
        //TODO 发送云信
        String last4MobileNums;
        CustomerUserInfoVO customerUserInfoVO = getCustomerUserInfoVO(orderInfo.getCustomerId());
        if (StringUtils.isBlank(customerUserInfoVO.getCustomerMobile())) {
            logger.info("用户customerId={}，未找到手机号", orderInfo.getCustomerId());
            last4MobileNums = "";
        } else {
            last4MobileNums = StringUtils.substring(customerUserInfoVO.getCustomerMobile(), 7);
        }
        String msg = MessageFormat.format(OrderNotifyMsg.ORDER_COMPLETE_MSG_4_STORE, last4MobileNums);
        registerProcessAfterTransSuccess(new OrderCompleteProcessRunnerble(orderInfo), null);
        logger.info("订单：orderNo={} 自提收货结束", condition.getOrderNo());
    }

    /**
     * 注册事物提交后相关操作
     *
     * @param orderInfo
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
        //计算订单总金额
        BigDecimal orderTotal = calculateOrderTotal(orderCreateCondition);
        short valuationType = ValuationTypeEnum.ONLINE_VALUATION.getTypeCode();
        if (orderTotal == null) {
            //待计价订单
            valuationType = ValuationTypeEnum.OFFLINE_VALUATION.getTypeCode();
        }
        orderInfo.setOrderTotalMoney(orderTotal);
        orderInfo.setValuationType(valuationType);
        orderInfo.setCustomerId(orderCreateCondition.getCustomerId());
        orderInfo.setStoreId(orderCreateCondition.getStoreId());
        orderInfo.setPayStatus((short) PayStatusEnum.UNPAID.getStatusCode());
        orderInfo.setPayType(orderCreateCondition.getPayType());
        orderInfo.setPickupDateTime(orderCreateCondition.getPickupDateTime());
        orderInfo.setRemarks(orderCreateCondition.getRemark());
        orderInfo.setPickupType((short) PickUpTypeEnum.SELF_PICK_UP.getTypeCode());
        orderInfo.setOrderNo(generateOrderNo());
        StoreUserInfoVO storeUserInfoVO = getStoreUserInfoByStoreId(orderInfo.getStoreId());
        orderInfo.setRegionCode(storeUserInfoVO.getStoreRegionCode());
        //组装订单商品信息
        aseembleOrderItems(orderCreateCondition, orderInfo);
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
        //TODO 调用促销系统
        orderInfo.setCouponHxdMoney(BigDecimal.ZERO);
        orderInfo.setCouponBrandMoney(BigDecimal.ZERO);
        orderInfo.setRandomReductionMoney(BigDecimal.ZERO);
        orderInfo.setRealPaymentMoney(orderInfo.getOrderTotalMoney().subtract(orderInfo.getCouponBrandMoney()).subtract(orderInfo.getCouponHxdMoney()).subtract(orderInfo.getRandomReductionMoney()));
        //通知促销系统优惠券使用情况
        OrderUseCouponCondition orderUseCouponCondition = new OrderUseCouponCondition();
        orderUseCouponCondition.setCouponPrice(orderInfo.getCouponHxdMoney());
        orderUseCouponCondition.setOrderNo(orderInfo.getOrderNo());
        orderUseCouponCondition.setOrderPrice(orderInfo.getOrderTotalMoney());
        orderUseCouponCondition.setSendIds(Arrays.asList(couponIds));
        ResponseResult ret = couponServiceClient.orderUseCoupon(orderUseCouponCondition);
        if (ret == null || ret.getCode() != BusinessCode.CODE_OK) {
            //优惠券使用失败
            logger.error("订单：{}优惠券使用更新接口调用失败:code={}，创建订单异常！~", orderInfo.getOrderNo(), ret == null ? null : ret.getCode());
            throw new BusinessException(BusinessCode.CODE_401009);
        }
        logger.info("订单:{},优惠券 couponIds={},使用接口调用成功。", orderInfo.getOrderNo(), Arrays.toString(couponIds));
    }

    private OrderHandler getOrderHandler(short valuationType) {
        if (valuationType == ValuationTypeEnum.ONLINE_VALUATION.getTypeCode()) {
            return onlinePayPickUpInStoreOrderHandler;
        } else if (valuationType == ValuationTypeEnum.OFFLINE_VALUATION.getTypeCode()) {
            return onlinePayPickUpInStoreOfflineOrderHandler;
        }
        throw new BusinessException(BusinessCode.CODE_401008);
    }

    private BigDecimal calculateOrderTotal(OrderCreateCondition orderCreateCondition) {
        BigDecimal orderTotal = BigDecimal.ZERO;
        for (Iterator iterator = orderCreateCondition.getOrderItemConditions().iterator(); iterator.hasNext(); ) {
            OrderItemCondition detailCondition = (OrderItemCondition) iterator.next();
            // TODO 获取该经销商 产品价格,暂时使用传入价格
            BigDecimal price = detailCondition.getPrice();
            if (price == null) {
                return null;
            }
            orderTotal = orderTotal.add(detailCondition.getPrice().multiply(new BigDecimal(detailCondition.getAmount()))
                    .setScale(ORDER_MONEY_SCALE, RoundingMode.HALF_UP));
        }
        return orderTotal;
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
        if (orderCreateCondition.getPickupDateTime() == null) {
            throw new BusinessException(BusinessCode.CODE_401004);
        }
        if (orderCreateCondition.getOrderItemConditions() == null || orderCreateCondition.getOrderItemConditions().isEmpty()) {
            throw new BusinessException(BusinessCode.CODE_401005);
        }
        for (Iterator iterator = orderCreateCondition.getOrderItemConditions().iterator(); iterator.hasNext(); ) {
            OrderItemCondition condition = (OrderItemCondition) iterator.next();
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
    private void aseembleOrderItems(OrderCreateCondition orderCreateCondition, OrderInfo orderInfo) {
        List<OrderItem> items = new ArrayList<>();
        for (Iterator<OrderItemCondition> iterator = orderCreateCondition.getOrderItemConditions().iterator(); iterator.hasNext(); ) {
            OrderItemCondition condition = iterator.next();
            OrderItem item = new OrderItem();
            BeanUtils.copyProperties(condition, item);
            item.setOrderNo(orderInfo.getOrderNo());
            item.setCreated(orderInfo.getCreated());
            item.setCreatedBy(orderInfo.getCreatedBy());
            item.setCreatedByName(orderInfo.getCreatedByName());
            items.add(item);
        }
        orderInfo.setOrderItems(items);
    }

    /**
     * 生成订单号  格式：C+YYMMDDHH(年月日小时8位)XXXXXXXXX(9位随机吗)= 18位
     *
     * @return
     * @author wangbin
     * @date 2018年8月3日 上午11:34:29
     */
    private String generateOrderNo() {
        UUID uuid = UUID.randomUUID();
        int hashCodeV = (uuid.toString() + System.currentTimeMillis()).hashCode();
        if (hashCodeV < 0) {// 有可能是负数
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
        return "C" + DateFormatUtils.format(new Date(), orderNoDateTimeFormatter) + String.format(randomFormat, hashCodeV);
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
                ResponseResult ret = couponServiceClient.orderUntreadCoupon(orderUntreadCouponCondition);
                if (ret == null || ret.getCode() != BusinessCode.CODE_OK) {
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
            //TODO 发送mq完成消息
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
