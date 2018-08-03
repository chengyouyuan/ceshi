package com.winhxd.b2c.order.service.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winhxd.b2c.common.domain.order.enums.OrderStatusEnum;
import com.winhxd.b2c.order.dao.OrderChangeLogMapper;
import com.winhxd.b2c.order.model.OrderChangeLog;
import com.winhxd.b2c.order.service.OrderChangeLogService;

@Service
public class OrderChangeLogServiceImpl implements OrderChangeLogService {

    private static final Logger logger = LoggerFactory.getLogger(OrderChangeLogServiceImpl.class);
    
    @Autowired
    private OrderChangeLogMapper orderChangeLogMapper;

    @Override
    public void orderChange(String orderNo, String originalJson, String newJson, OrderStatusEnum originalStatus,
            OrderStatusEnum newStatus, Long createdBy, String createdByName, String changeMsg) {
        if (StringUtils.isBlank(orderNo)) {
            throw new NullPointerException("订单号不能为空");
        }
        if (StringUtils.isBlank(newJson)) {
            throw new NullPointerException("变化后订单json串不能为空");
        }
        if (newStatus == null) {
            throw new NullPointerException("变化后订单状态不能为空");
        }
        if (StringUtils.isBlank(createdByName) || createdBy == null) {
            throw new NullPointerException("操作人不能为空");
        }
        MainPointEnum pointType = MainPointEnum.MAIN;
        // 如果订单状态没有改变，则为非主要节点
        if (originalStatus != null && originalStatus.getStatusCode() == newStatus.getStatusCode()) {
            pointType = MainPointEnum.NOT_MAIN;
        }
        if (StringUtils.isBlank(changeMsg)) {
            changeMsg = newStatus.getStatusDes();
        }
        OrderChangeLog orderChangeLog = new OrderChangeLog(orderNo,
                originalStatus == null ? null : originalStatus.getStatusCode(), newStatus.getStatusCode(),
                changeMsg, pointType.getCode(), new Date(), createdBy, "" + createdByName, originalJson, newJson);
        orderChangeLogMapper.insertSelective(orderChangeLog);
        logger.info("订单流转变化：", orderChangeLog);

    }

}
