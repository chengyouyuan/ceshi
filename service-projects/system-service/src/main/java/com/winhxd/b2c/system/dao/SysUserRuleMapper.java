package com.winhxd.b2c.system.dao;

import com.winhxd.b2c.common.domain.system.model.SysUserRule;

public interface SysUserRuleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysUserRule record);

    int insertSelective(SysUserRule record);

    SysUserRule selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUserRule record);

    int updateByPrimaryKey(SysUserRule record);

    int deleteByUserId(Long userId);
}