package com.winhxd.b2c.system.user.dao;

import com.winhxd.b2c.common.domain.system.user.model.SysRule;

import java.util.List;

public interface SysRuleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysRule record);

    int insertSelective(SysRule record);

    SysRule selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRule record);

    int updateByPrimaryKey(SysRule record);

}