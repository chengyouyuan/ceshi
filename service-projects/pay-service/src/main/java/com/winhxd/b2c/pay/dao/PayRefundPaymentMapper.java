package com.winhxd.b2c.pay.dao;

import com.winhxd.b2c.common.domain.pay.model.PayRefundPayment;

public interface PayRefundPaymentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PayRefundPayment record);

    int insertSelective(PayRefundPayment record);

    PayRefundPayment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PayRefundPayment record);

    int updateByPrimaryKey(PayRefundPayment record);

    int updateByRefundTransactionNoSelective(PayRefundPayment record);
}