package com.winhxd.b2c.order.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.winhxd.b2c.common.domain.order.condition.OrderCreateCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderItemCondition;
import com.winhxd.b2c.common.domain.order.enums.PayStatusEnum;
import com.winhxd.b2c.common.domain.order.enums.PayTypeEnum;
import com.winhxd.b2c.common.domain.order.enums.PickUpTypeEnum;
import com.winhxd.b2c.common.domain.order.enums.ValuationTypeEnum;
import com.winhxd.b2c.common.domain.order.model.OrderInfo;
import com.winhxd.b2c.common.domain.order.model.OrderItem;
import com.winhxd.b2c.common.exception.order.OrderCreateExcepton;
import com.winhxd.b2c.common.exception.order.OrderCreateExceptonCodes;
import com.winhxd.b2c.common.util.DateUtil;
import com.winhxd.b2c.order.service.OrderHandler;
import com.winhxd.b2c.order.service.OrderService;

@Service
public class CommonOrderServiceImpl implements OrderService {
    
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

    @Override
    @Transactional(rollbackFor=Exception.class)
    public String submitOrder(OrderCreateCondition orderCreateCondition) {
        if (orderCreateCondition == null) {
            throw new NullPointerException("orderCreateCondition不能为空");
        }
        validateOrderCreateCondition(orderCreateCondition);
        logger.info("创建订单开始：orderCreateCondition={}", orderCreateCondition);
        //生成订单主体信息
        OrderInfo orderInfo = assembleOrderInfo(orderCreateCondition);
        return null;
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
        //组装订单商品信息
        aseembleOrderItems(orderCreateCondition, orderInfo);
        //优惠券相关优惠计算
        calculateDiscounts(orderInfo, orderCreateCondition.getCouponIds());
        getOrderHandler(orderInfo.getPayType(), orderInfo.getValuationType()).orderInfoBeforeCreateProcess(orderInfo);
        
        return null;
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
        if (payType == PayTypeEnum.WECHAT_ONLINE_PAYMENT.getTypeCode()) {
            return onlinePayPickUpInStoreOrderHandler;
        } else if (payType == PayTypeEnum.WECHAT_SCAN_CODE_PAYMENT.getTypeCode()
                && valuationType == ValuationTypeEnum.OFFLINE_VALUATION.getTypeCode()) {
            return sweepPayPickUpInStoreOfflineValOrderHandler;
        } else if (payType == PayTypeEnum.WECHAT_SCAN_CODE_PAYMENT.getTypeCode()
                && valuationType == ValuationTypeEnum.ONLINE_VALUATION.getTypeCode()) {
            return sweepPayPickUpInStoreOnlineValOrderHandler;
        }
        throw new OrderCreateExcepton(OrderCreateExceptonCodes.CODE_401008);
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
            throw new OrderCreateExcepton(OrderCreateExceptonCodes.CODE_401001);
        }
        if (orderCreateCondition.getStoreId() == null) {
            throw new OrderCreateExcepton(OrderCreateExceptonCodes.CODE_401002);
        }
        if (orderCreateCondition.getPayType() == null || PayTypeEnum.getPayTypeEnumByTypeCode(orderCreateCondition.getPayType()) == null) {
            throw new OrderCreateExcepton(OrderCreateExceptonCodes.CODE_401003);
        }
        if (orderCreateCondition.getPickupDateTime() == null) {
            throw new OrderCreateExcepton(OrderCreateExceptonCodes.CODE_401004);
        }
        if (orderCreateCondition.getOrderItemConditions() == null || orderCreateCondition.getOrderItemConditions().isEmpty()) {
            throw new OrderCreateExcepton(OrderCreateExceptonCodes.CODE_401005);
        }
        for (Iterator iterator = orderCreateCondition.getOrderItemConditions().iterator(); iterator.hasNext();) {
            OrderItemCondition condition = (OrderItemCondition) iterator.next();
            if (condition.getAmount() == null || condition.getAmount().intValue() < 1) {
                throw new OrderCreateExcepton(OrderCreateExceptonCodes.CODE_401006);
            }
            if (StringUtils.isBlank(condition.getSkuCode())) {
                throw new OrderCreateExcepton(OrderCreateExceptonCodes.CODE_401007);
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
        return "C" + DateUtil.dateTime2Str(LocalDateTime.now(), orderNoDateTimeFormatter) + String.format(randomFormat, hashCodeV);
    }
}
