package com.winhxd.b2c.pay.dao;

import com.winhxd.b2c.common.domain.pay.model.PayOrderPayment;

public interface PayOrderPaymentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PayOrderPayment record);

    int insertSelective(PayOrderPayment record);

    PayOrderPayment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PayOrderPayment record);

    int updateByPrimaryKey(PayOrderPayment record);
}