package com.winhxd.b2c.order.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.cache.Lock;
import com.winhxd.b2c.common.cache.RedisLock;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.domain.order.condition.OrderCancelCondition;
import com.winhxd.b2c.common.exception.BusinessException;
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

import com.winhxd.b2c.common.domain.order.condition.OrderCreateCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderItemCondition;
import com.winhxd.b2c.common.domain.order.enums.OrderStatusEnum;
import com.winhxd.b2c.common.domain.order.enums.PayStatusEnum;
import com.winhxd.b2c.common.domain.order.enums.PayTypeEnum;
import com.winhxd.b2c.common.domain.order.enums.PickUpTypeEnum;
import com.winhxd.b2c.common.domain.order.enums.ValuationTypeEnum;
import com.winhxd.b2c.common.domain.order.model.OrderInfo;
import com.winhxd.b2c.common.domain.order.model.OrderItem;
import com.winhxd.b2c.common.exception.OrderExcepton;
import com.winhxd.b2c.common.exception.OrderExceptonCodes;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.order.dao.OrderInfoMapper;
import com.winhxd.b2c.order.dao.OrderItemMapper;
import com.winhxd.b2c.order.service.OrderChangeLogService;
import com.winhxd.b2c.order.service.OrderChangeLogService.MainPointEnum;
import com.winhxd.b2c.order.service.OrderHandler;
import com.winhxd.b2c.order.service.OrderService;

import javax.annotation.Resource;

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
    @Qualifier("SweepPayPickUpInStoreOfflineValOrderHandler")
    private OrderHandler sweepPayPickUpInStoreOfflineValOrderHandler;
    
    @Autowired
    @Qualifier("SweepPayPickUpInStoreOnlineValOrderHandler")
    private OrderHandler sweepPayPickUpInStoreOnlineValOrderHandler;
    
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    
    @Autowired
    private OrderItemMapper orderItemMapper;
    
    @Autowired
    private OrderChangeLogService orderChangeLogService;
    @Resource
    private Cache cache;


    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, new ArrayBlockingQueue<>(QUEUE_CAPACITY));

    @Override
    @Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRES_NEW)
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
        getOrderHandler(orderInfo.getPayType(), orderInfo.getValuationType()).orderInfoBeforeCreateProcess(orderInfo);
        logger.info("订单orderNo：{} 创建前相关业务操作执行结束", orderInfo.getOrderNo());
        orderInfoMapper.insertSelective(orderInfo);
        orderItemMapper.insertItems(orderInfo.getOrderItems());
        // 生产订单流转日志
        orderChangeLogService.orderChange(orderInfo.getOrderNo(), null, JsonUtil.toJSONString(orderInfo), null,
                orderInfo.getOrderStatus(), orderInfo.getCreatedBy(), orderInfo.getCreatedByName(),
                OrderStatusEnum.SUBMITTED.getStatusDes(), MainPointEnum.MAIN);
        //订单创建后相关业务操作
        logger.info("订单orderNo：{} 创建后相关业务操作执行开始", orderInfo.getOrderNo());
        getOrderHandler(orderInfo.getPayType(), orderInfo.getValuationType()).orderInfoAfterCreateProcess(orderInfo);
        logger.info("订单orderNo：{} 创建后相关业务操作执行结束", orderInfo.getOrderNo());
        logger.info("创建订单结束orderNo：{}", orderInfo.getOrderNo());
        //注册订单创建成功事物提交后相关事件
        registerProcessAfterOrderSubmitSuccess(orderInfo);
        return orderInfo.getOrderNo();
    }
    

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void orderPaySuccessNotify(String orderNo) {
        if (StringUtils.isBlank(orderNo)) {
            throw new NullPointerException("订单支付通知orderNo不能为空");
        }
        OrderInfo orderInfo = orderInfoMapper.selectByOrderNo(orderNo);
        if (orderInfo == null) {
            throw new OrderExcepton(OrderExceptonCodes.WRONG_ORDERNO);
        }
        if (PayStatusEnum.UNPAID.getStatusCode() != orderInfo.getPayStatus().shortValue()) {
            throw new OrderExcepton(OrderExceptonCodes.ORDER_ALREADY_PAID);
        }
        logger.info("订单orderNo={}，支付通知处理开始.", orderNo);
        Date payFinishDateTime = new Date();
        int updNum = orderInfoMapper.updateOrderPayStatus(PayStatusEnum.PAID.getStatusCode(), payFinishDateTime, orderInfo.getId());
        if (updNum != 1) {
            throw new OrderExcepton(OrderExceptonCodes.ORDER_ALREADY_PAID);
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
        getOrderHandler(orderInfo.getPayType(), orderInfo.getValuationType()).orderFinishPayProcess(orderInfo);
        logger.info("订单orderNo：{} 支付后相关业务操作执行结束", orderInfo.getOrderNo());
    }


    /**
     * 订单取消service
     *
     * @author pangjianhua
     * @date 2018年8月2日 下午5:51:46
     * @param orderCancelCondition 入参
     * @return true 成功，false不成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelOrder(OrderCancelCondition orderCancelCondition) throws InterruptedException {
        boolean result = true;
        String orderNo = orderCancelCondition.getOrderNo();
        if (StringUtils.isBlank(orderNo)) {
            throw new BusinessException(BusinessCode.CODE_411001, "订单号不能未空");
        }
        String lockKey = CacheName.CACHE_KEY_STORE_PICK_UP_CODE_GENERATE + orderNo;
        Lock lock = new RedisLock(cache, lockKey, 1000);
        if (lock.tryLock(1000, TimeUnit.MILLISECONDS)) {
            try {
                //获取order对象
                OrderInfo order = orderInfoMapper.selectByOrderNo(orderNo);
                //判断是否支付成功,支付成功不让取消
                if (PayStatusEnum.PAID.getStatusCode() == order.getPayStatus()) {
                    logger.info("订单已支付成功不能取消，请走退款接口 订单号={}", orderNo);
                    throw new BusinessException(BusinessCode.CODE_411001, "订单已支付成功不能取消");
                }
                //把提货码置为null、取消原因、取消状态等
                int updateRowNum = this.orderInfoMapper.updateOrderStatusForCancel(orderNo, orderCancelCondition.getCancelReason());
                if (updateRowNum < 1) {
                    logger.info("订单取消更新不成功 订单号={}", orderNo);
                    result = false;
                }
            } finally {
                lock.unlock();
            }
        } else {
            throw new BusinessException(BusinessCode.CODE_411001, "订单正在修改中");
        }
        return result;
    }

    /**
     * 订单成功创建成功 业务操作
     * @author wangbin
     * @date  2018年8月3日 下午4:50:11
     * @param orderInfo
     */
    private void registerProcessAfterOrderSubmitSuccess(OrderInfo orderInfo) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                threadPoolExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        //TODO 调用 顾客门店绑定接口
                        getOrderHandler(orderInfo.getPayType(), orderInfo.getValuationType()).orderInfoAfterCreateSuccessProcess(orderInfo);
                    }
                });
            }
        }
       );
    }

    private OrderInfo assembleOrderInfo(OrderCreateCondition orderCreateCondition) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreated(new Date());
        orderInfo.setCreatedBy(orderCreateCondition.getCustomerId());
        //计算订单总金额
        BigDecimal orderTotal = calculateOrderTotal(orderCreateCondition);
        short valuationType = (short) ValuationTypeEnum.ONLINE_VALUATION.getTypeCode();
        if (orderTotal == null) {
            //待计价订单
            valuationType = (short) ValuationTypeEnum.OFFLINE_VALUATION.getTypeCode();
        }
        orderInfo.setOrderTotalMoney(orderTotal);
        orderInfo.setValuationType(valuationType);
        orderInfo.setCustomerId(orderCreateCondition.getCustomerId());
        orderInfo.setStoreId(orderCreateCondition.getStoreId());
        orderInfo.setPayStatus((short)PayStatusEnum.UNPAID.getStatusCode());
        orderInfo.setPayType(orderCreateCondition.getPayType());
        orderInfo.setPickupDateTime(orderCreateCondition.getPickupDateTime());
        orderInfo.setRemark(orderCreateCondition.getRemark());
        orderInfo.setPickupType((short)PickUpTypeEnum.SELF_PICK_UP.getTypeCode());
        orderInfo.setOrderNo(generateOrderNo());
        // TODO 获取门店地理区域信息
        orderInfo.setRegionCode("regionCode");
        //组装订单商品信息
        aseembleOrderItems(orderCreateCondition, orderInfo);
        //优惠券相关优惠计算
        calculateDiscounts(orderInfo, orderCreateCondition.getCouponIds());
        return orderInfo;
    }

    /**
     * 订单优惠计算
     * @author wangbin
     * @date  2018年8月3日 上午11:31:48
     * @param orderInfo
     * @param couponIds
     */
    private void calculateDiscounts(OrderInfo orderInfo, Long[] couponIds) {
        if (orderInfo.getOrderTotalMoney() == null) {
            logger.info("订单orderNo：{}计算优惠金额时还没有计价，无法进行优惠计算", orderInfo.getOrderNo());
            return;
        }
        //TODO 调用促销系统
        orderInfo.setCouponHxdMoney(BigDecimal.ZERO);
        orderInfo.setCouponBrandMoney(BigDecimal.ZERO);
    }

    private OrderHandler getOrderHandler(short payType, short valuationType) {
        if (payType == PayTypeEnum.WECHAT_ONLINE_PAYMENT.getTypeCode()
                && valuationType == ValuationTypeEnum.ONLINE_VALUATION.getTypeCode()) {
            return onlinePayPickUpInStoreOrderHandler;
        } else if (payType == PayTypeEnum.WECHAT_SCAN_CODE_PAYMENT.getTypeCode()
                && valuationType == ValuationTypeEnum.OFFLINE_VALUATION.getTypeCode()) {
            return sweepPayPickUpInStoreOfflineValOrderHandler;
        } else if (payType == PayTypeEnum.WECHAT_SCAN_CODE_PAYMENT.getTypeCode()
                && valuationType == ValuationTypeEnum.ONLINE_VALUATION.getTypeCode()) {
            return sweepPayPickUpInStoreOnlineValOrderHandler;
        }
        throw new OrderExcepton(OrderExceptonCodes.CODE_401008);
    }

    private BigDecimal calculateOrderTotal(OrderCreateCondition orderCreateCondition) {
        BigDecimal orderTotal = BigDecimal.ZERO;
        for (Iterator iterator = orderCreateCondition.getOrderItemConditions().iterator(); iterator.hasNext();) {
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
     * @author wangbin
     * @date  2018年8月3日 上午11:31:12
     * @param orderCreateCondition
     */
    private void validateOrderCreateCondition(OrderCreateCondition orderCreateCondition) {
        if (orderCreateCondition.getCustomerId() == null) {
            throw new OrderExcepton(OrderExceptonCodes.CODE_401001);
        }
        if (orderCreateCondition.getStoreId() == null) {
            throw new OrderExcepton(OrderExceptonCodes.CODE_401002);
        }
        if (orderCreateCondition.getPayType() == null || PayTypeEnum.getPayTypeEnumByTypeCode(orderCreateCondition.getPayType()) == null) {
            throw new OrderExcepton(OrderExceptonCodes.CODE_401003);
        }
        if (orderCreateCondition.getPickupDateTime() == null) {
            throw new OrderExcepton(OrderExceptonCodes.CODE_401004);
        }
        if (orderCreateCondition.getOrderItemConditions() == null || orderCreateCondition.getOrderItemConditions().isEmpty()) {
            throw new OrderExcepton(OrderExceptonCodes.CODE_401005);
        }
        for (Iterator iterator = orderCreateCondition.getOrderItemConditions().iterator(); iterator.hasNext();) {
            OrderItemCondition condition = (OrderItemCondition) iterator.next();
            if (condition.getAmount() == null || condition.getAmount().intValue() < 1) {
                throw new OrderExcepton(OrderExceptonCodes.CODE_401006);
            }
            if (StringUtils.isBlank(condition.getSkuCode())) {
                throw new OrderExcepton(OrderExceptonCodes.CODE_401007);
            }
        }
    }

    /**
     * 组装订单商品项明细
     * @author wangbin
     * @date  2018年8月3日 上午11:30:53
     * @param orderCreateCondition
     * @param orderInfo
     */
    private void aseembleOrderItems(OrderCreateCondition orderCreateCondition, OrderInfo orderInfo) {
        List<OrderItem> items = new ArrayList<>();
        for (Iterator<OrderItemCondition> iterator = orderCreateCondition.getOrderItemConditions().iterator(); iterator.hasNext();) {
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
     * @author wangbin
     * @date  2018年8月3日 上午11:34:29
     * @return
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
}
