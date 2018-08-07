package com.winhxd.b2c.system.user.dao;

import com.winhxd.b2c.common.domain.system.user.model.SysUserRole;

public interface SysUserRuleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysUserRole record);

    int insertSelective(SysUserRole record);

    SysUserRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUserRole record);

    int updateByPrimaryKey(SysUserRole record);

    int deleteByUserId(Long userId);
}