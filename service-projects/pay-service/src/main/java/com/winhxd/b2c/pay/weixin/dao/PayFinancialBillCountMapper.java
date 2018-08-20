package com.winhxd.b2c.pay.weixin.dao;

import com.winhxd.b2c.common.domain.pay.model.PayFinancialBillCount;

public interface PayFinancialBillCountMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PayFinancialBillCount record);

    int insertSelective(PayFinancialBillCount record);

    PayFinancialBillCount selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PayFinancialBillCount record);

    int updateByPrimaryKey(PayFinancialBillCount record);
}