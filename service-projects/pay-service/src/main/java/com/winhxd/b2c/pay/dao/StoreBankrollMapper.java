package com.winhxd.b2c.pay.dao;

import com.winhxd.b2c.common.domain.pay.model.StoreBankroll;

public interface StoreBankrollMapper {
    int deleteByPrimaryKey(Long id);

    int insert(StoreBankroll record);

    int insertSelective(StoreBankroll record);

    StoreBankroll selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StoreBankroll record);

    int updateByPrimaryKey(StoreBankroll record);
}