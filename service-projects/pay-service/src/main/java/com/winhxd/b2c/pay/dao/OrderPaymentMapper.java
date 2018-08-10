package com.winhxd.b2c.pay.dao;

import com.winhxd.b2c.common.domain.order.model.OrderPayment;

public interface OrderPaymentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderPayment record);

    int insertSelective(OrderPayment record);

    OrderPayment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderPayment record);

    int updateByPrimaryKey(OrderPayment record);
}