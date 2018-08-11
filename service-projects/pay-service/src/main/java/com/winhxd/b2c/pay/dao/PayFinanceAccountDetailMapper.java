package com.winhxd.b2c.pay.dao;

import com.winhxd.b2c.common.domain.pay.model.PayFinanceAccountDetail;

public interface PayFinanceAccountDetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PayFinanceAccountDetail record);

    int insertSelective(PayFinanceAccountDetail record);

    PayFinanceAccountDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PayFinanceAccountDetail record);

    int updateByPrimaryKey(PayFinanceAccountDetail record);
}