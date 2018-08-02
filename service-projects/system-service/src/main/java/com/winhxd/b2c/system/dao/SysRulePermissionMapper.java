package com.winhxd.b2c.system.dao;

import com.winhxd.b2c.common.domain.system.model.SysRulePermission;

import java.util.List;

public interface SysRulePermissionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysRulePermission record);

    int insertSelective(SysRulePermission record);

    SysRulePermission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRulePermission record);

    int updateByPrimaryKey(SysRulePermission record);

    List<String> selectPermissionByUserId(Long userId);
}