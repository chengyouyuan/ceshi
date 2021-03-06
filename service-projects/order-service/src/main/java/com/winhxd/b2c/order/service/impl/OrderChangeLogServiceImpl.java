package com.winhxd.b2c.order.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winhxd.b2c.common.domain.order.enums.OrderStatusEnum;
import com.winhxd.b2c.common.domain.order.vo.OrderChangeVO;
import com.winhxd.b2c.order.dao.OrderChangeLogMapper;
import com.winhxd.b2c.order.model.OrderChangeLog;
import com.winhxd.b2c.order.service.OrderChangeLogService;

@Service
public class OrderChangeLogServiceImpl implements OrderChangeLogService {

    private static final Logger logger = LoggerFactory.getLogger(OrderChangeLogServiceImpl.class);

    private static final Long SYSTEM_ID = 0L;

    @Autowired
    private OrderChangeLogMapper orderChangeLogMapper;

    @Override
    public void orderChange(String orderNo, String originalJson, String newJson, Short originalStatus, Short newStatus,
            Long createdBy, String createdByName, String changeMsg, MainPointEnum pointType) {
        if (StringUtils.isBlank(orderNo)) {
            throw new NullPointerException("订单号不能为空");
        }
        if (StringUtils.isBlank(newJson)) {
            throw new NullPointerException("变化后订单json串不能为空");
        }
        if (newStatus == null) {
            throw new NullPointerException("变化后订单状态不能为空");
        }
        if (createdBy == null) {
            createdBy = SYSTEM_ID;
        }
        if (pointType == null) {
            pointType = MainPointEnum.NOT_MAIN;
        }
        if (StringUtils.isBlank(changeMsg)) {
            throw new NullPointerException("changeMsg不能为空");
        }
        OrderChangeLog orderChangeLog = new OrderChangeLog(orderNo, originalStatus, newStatus, changeMsg,
                pointType.getCode(), new Date(), createdBy, createdByName, originalJson, newJson);
        orderChangeLogMapper.insertSelective(orderChangeLog);
        logger.info("订单流转变化：", orderChangeLog);

    }

    @Override
    public List<OrderChangeVO> listOrderChanges(String orderNo) {
        if (StringUtils.isBlank(orderNo)) {
            throw new NullPointerException("订单编号不能为空");
        }
        return orderChangeLogMapper.listOrderChanges(orderNo);
    }

}
