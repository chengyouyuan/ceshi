package com.winhxd.b2c.store.dao;

import com.winhxd.b2c.common.domain.store.model.CustomerBrowseLog;

public interface CustomerBrowseLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CustomerBrowseLog record);

    int insertSelective(CustomerBrowseLog record);

    CustomerBrowseLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CustomerBrowseLog record);

    int updateByPrimaryKey(CustomerBrowseLog record);
}