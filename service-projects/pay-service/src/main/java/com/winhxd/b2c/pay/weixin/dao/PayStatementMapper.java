package com.winhxd.b2c.pay.weixin.dao;

import java.util.Date;

import com.winhxd.b2c.common.domain.pay.model.PayStatement;

public interface PayStatementMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PayStatement record);

    int insertSelective(PayStatement record);

    PayStatement selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PayStatement record);

    int updateByPrimaryKey(PayStatement record);

    int deleteByBillDate(Date billDate);
}