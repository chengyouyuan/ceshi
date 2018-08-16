package com.winhxd.b2c.system.dao;

import com.winhxd.b2c.common.domain.system.user.model.SysRolePermission;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SysRolePermissionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysRolePermission record);

    int insertSelective(SysRolePermission record);

    SysRolePermission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRolePermission record);

    int updateByPrimaryKey(SysRolePermission record);

    List<String> selectPermissionByUserId(Long userId);

    int deleteByRoleId(Long roleId);
}