package com.winhxd.b2c.pay.dao;

import com.winhxd.b2c.common.domain.pay.model.PayWithdrawals;

public interface PayWithdrawalsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PayWithdrawals record);

    int insertSelective(PayWithdrawals record);

    PayWithdrawals selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PayWithdrawals record);

    int updateByPrimaryKey(PayWithdrawals record);
}