package com.winhxd.b2c.pay.weixin.dao;

import com.winhxd.b2c.pay.weixin.model.PayBill;

public interface PayBillMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PayBill record);

    int insertSelective(PayBill record);

    PayBill selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PayBill record);

    int updateByPrimaryKey(PayBill record);
}