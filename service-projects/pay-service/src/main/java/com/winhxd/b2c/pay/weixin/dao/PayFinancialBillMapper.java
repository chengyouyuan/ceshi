package com.winhxd.b2c.pay.weixin.dao;

import com.winhxd.b2c.pay.weixin.model.PayFinancialBill;

public interface PayFinancialBillMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PayFinancialBill record);

    int insertSelective(PayFinancialBill record);

    PayFinancialBill selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PayFinancialBill record);

    int updateByPrimaryKey(PayFinancialBill record);
}