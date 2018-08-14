package com.winhxd.b2c.pay.dao;

import com.winhxd.b2c.common.domain.pay.model.PayAccountingDetail;

public interface PayAccountingDetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PayAccountingDetail record);

    int insertSelective(PayAccountingDetail record);

    PayAccountingDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PayAccountingDetail record);

    int updateByPrimaryKey(PayAccountingDetail record);
}