package com.winhxd.b2c.pay.weixin.dao;

import com.winhxd.b2c.pay.weixin.model.PayRefund;

public interface PayRefundMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PayRefund record);

    int insertSelective(PayRefund record);

    PayRefund selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PayRefund record);

    int updateByPrimaryKey(PayRefund record);
}