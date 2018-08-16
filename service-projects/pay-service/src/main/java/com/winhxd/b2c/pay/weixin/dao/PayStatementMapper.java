package com.winhxd.b2c.pay.weixin.dao;

import com.winhxd.b2c.pay.weixin.model.PayStatement;

public interface PayStatementMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PayStatement record);

    int insertSelective(PayStatement record);

    PayStatement selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PayStatement record);

    int updateByPrimaryKey(PayStatement record);
}