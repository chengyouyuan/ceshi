package com.winhxd.b2c.pay.dao;

import java.util.List;
import java.util.Map;

import com.winhxd.b2c.common.domain.pay.model.PayStoreBankrollLog;

public interface PayStoreBankrollLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PayStoreBankrollLog record);

    int insertSelective(PayStoreBankrollLog record);

    PayStoreBankrollLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PayStoreBankrollLog record);

    int updateByPrimaryKey(PayStoreBankrollLog record);
    
    List<PayStoreBankrollLog> selectListByNoAndType(Map<String, Object> map);
}