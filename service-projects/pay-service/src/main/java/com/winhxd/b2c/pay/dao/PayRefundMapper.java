package com.winhxd.b2c.pay.dao;

import com.winhxd.b2c.common.domain.pay.model.PayRefund;

public interface PayRefundMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PayRefund record);

    int insertSelective(PayRefund record);

    PayRefund selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PayRefund record);

    int updateByPrimaryKey(PayRefund record);
}