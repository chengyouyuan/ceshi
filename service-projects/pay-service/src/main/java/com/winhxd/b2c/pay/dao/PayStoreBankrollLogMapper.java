package com.winhxd.b2c.pay.dao;

import com.winhxd.b2c.common.domain.pay.model.PayStoreBankrollLog;

public interface PayStoreBankrollLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PayStoreBankrollLog record);

    int insertSelective(PayStoreBankrollLog record);

    PayStoreBankrollLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PayStoreBankrollLog record);

    int updateByPrimaryKey(PayStoreBankrollLog record);
}