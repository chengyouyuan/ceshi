package com.winhxd.b2c.system.dao;

import com.github.pagehelper.Page;
import com.winhxd.b2c.common.domain.system.condition.SysUserCondition;
import com.winhxd.b2c.common.domain.system.model.SysUser;

public interface SysUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    Page<SysUser> selectSysUser(SysUserCondition condition);

    SysUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    int updatePassword(SysUser record);
}