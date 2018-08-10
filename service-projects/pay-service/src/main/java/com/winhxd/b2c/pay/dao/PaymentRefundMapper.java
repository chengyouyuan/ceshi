package com.winhxd.b2c.pay.dao;

import com.winhxd.b2c.common.domain.pay.model.PaymentRefund;

public interface PaymentRefundMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PaymentRefund record);

    int insertSelective(PaymentRefund record);

    PaymentRefund selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PaymentRefund record);

    int updateByPrimaryKey(PaymentRefund record);
}