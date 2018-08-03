package com.winhxd.b2c.order.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.winhxd.b2c.common.domain.order.condition.OrderCreateCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderDetailCondition;
import com.winhxd.b2c.common.domain.order.enums.PayTypeEnum;
import com.winhxd.b2c.common.domain.order.enums.ValuationTypeEnum;
import com.winhxd.b2c.common.domain.order.model.OrderInfo;
import com.winhxd.b2c.common.exception.order.OrderCreateExcepton;
import com.winhxd.b2c.common.exception.order.OrderCreateExceptonCodes;
import com.winhxd.b2c.order.service.OrderService;

public class CommonOrderServiceImpl implements OrderService {
    
    private static final int ORDER_MONEY_SCALE = 2;
    private static final Logger logger = LoggerFactory.getLogger(CommonOrderServiceImpl.class);

    @Override
    public String submitOrder(OrderCreateCondition orderCreateCondition) {
        if (orderCreateCondition == null) {
            throw new NullPointerException("orderCreateCondition不能为空");
        }
        validateOrderCreateCondition(orderCreateCondition);
        logger.info("创建订单开始：orderCreateCondition={}", orderCreateCondition);
        //计算订单总金额
        BigDecimal orderTotal = calculateOrderTotal(orderCreateCondition);
        short valuationType = (short) ValuationTypeEnum.ONLINE_VALUATION.getTypeCode();
        if (orderTotal == null) {
            //待计价订单
            valuationType = (short) ValuationTypeEnum.OFFLINE_VALUATION.getTypeCode();
        }
//        OrderInfo orderInfo = 
        return null;
    }

    private BigDecimal calculateOrderTotal(OrderCreateCondition orderCreateCondition) {
        BigDecimal orderTotal = BigDecimal.ZERO;
        for (Iterator iterator = orderCreateCondition.getOrderDetailConditions().iterator(); iterator.hasNext();) {
            OrderDetailCondition detailCondition = (OrderDetailCondition) iterator.next();
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
        if (orderCreateCondition.getOrderDetailConditions() == null || orderCreateCondition.getOrderDetailConditions().isEmpty()) {
            throw new OrderCreateExcepton(OrderCreateExceptonCodes.CODE_401005);
        }
        for (Iterator iterator = orderCreateCondition.getOrderDetailConditions().iterator(); iterator.hasNext();) {
            OrderDetailCondition condition = (OrderDetailCondition) iterator.next();
            if (condition.getAmount() == null || condition.getAmount().intValue() < 1) {
                throw new OrderCreateExcepton(OrderCreateExceptonCodes.CODE_401006);
            }
            if (StringUtils.isBlank(condition.getSkuCode())) {
                throw new OrderCreateExcepton(OrderCreateExceptonCodes.CODE_401007);
            }
        }
    }

}
