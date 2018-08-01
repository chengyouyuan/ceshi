package com.winhxd.b2c.system.dao;

import com.winhxd.b2c.system.model.SysRule;

public interface SysRuleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysRule record);

    int insertSelective(SysRule record);

    SysRule selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRule record);

    int updateByPrimaryKey(SysRule record);
}