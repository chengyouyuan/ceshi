package com.winhxd.b2c.pay.weixin.dao;

import java.util.Date;

import com.winhxd.b2c.common.domain.pay.model.PayStatementCount;

public interface PayStatementCountMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PayStatementCount record);

    int insertSelective(PayStatementCount record);

    PayStatementCount selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PayStatementCount record);

    int updateByPrimaryKey(PayStatementCount record);

    int deleteByBillDate(Date billDate);
}