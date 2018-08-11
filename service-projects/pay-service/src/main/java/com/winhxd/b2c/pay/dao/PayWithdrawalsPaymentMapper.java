package com.winhxd.b2c.pay.dao;

import com.winhxd.b2c.common.domain.pay.model.PayWithdrawalsPayment;

public interface PayWithdrawalsPaymentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PayWithdrawalsPayment record);

    int insertSelective(PayWithdrawalsPayment record);

    PayWithdrawalsPayment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PayWithdrawalsPayment record);

    int updateByPrimaryKey(PayWithdrawalsPayment record);
}