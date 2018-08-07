package com.winhxd.b2c.system.user.dao;

import com.winhxd.b2c.common.domain.system.user.model.SysRolePermission;

import java.util.List;

public interface SysRulePermissionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysRolePermission record);

    int insertSelective(SysRolePermission record);

    SysRolePermission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRolePermission record);

    int updateByPrimaryKey(SysRolePermission record);

    List<String> selectPermissionByUserId(Long userId);
}