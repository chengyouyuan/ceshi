package com.winhxd.b2c.system.dao;

import com.winhxd.b2c.common.domain.system.sys.condition.SysUserCondition;
import com.winhxd.b2c.common.domain.system.sys.dto.SysUserPasswordDTO;
import com.winhxd.b2c.common.domain.system.sys.vo.SysUserVO;
import com.winhxd.b2c.common.domain.system.sys.model.SysUser;

import java.util.List;

public interface SysUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    List<SysUserVO> selectSysUser(SysUserCondition condition);

    SysUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    int updatePassword(SysUserPasswordDTO record);
}