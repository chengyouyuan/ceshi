package com.winhxd.b2c.pay.dao;

import com.winhxd.b2c.common.domain.pay.model.PaymentWithdrawals;

public interface PaymentWithdrawalsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PaymentWithdrawals record);

    int insertSelective(PaymentWithdrawals record);

    PaymentWithdrawals selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PaymentWithdrawals record);

    int updateByPrimaryKey(PaymentWithdrawals record);
}