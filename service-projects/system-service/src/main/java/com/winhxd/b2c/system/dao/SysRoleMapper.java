package com.winhxd.b2c.system.dao;

import com.github.pagehelper.Page;
import com.winhxd.b2c.common.domain.system.user.condition.SysRoleCondition;
import com.winhxd.b2c.common.domain.system.user.model.SysRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public interface SysRoleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    Page<SysRole> selectSysRole(SysRoleCondition condition);

    /**
     * 根据ID获取权限组(含成员数量)
     * @param id
     * @return
     */
    SysRole getSysRoleById(@Param("id") Long id);


}