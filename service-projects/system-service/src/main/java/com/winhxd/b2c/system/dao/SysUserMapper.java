package com.winhxd.b2c.system.dao;

import com.github.pagehelper.Page;
import com.winhxd.b2c.common.domain.system.user.condition.SysUserCondition;
import com.winhxd.b2c.common.domain.system.user.condition.SysUserResetPasswordCondition;
import com.winhxd.b2c.common.domain.system.user.model.SysUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface SysUserMapper {
    /**
     * 插入新用户
     * @param record
     * @return
     */
    int insertSelective(SysUser record);

    /**
     * 根据条件查询用户列表
     * @param condition
     * @return
     */
    Page<SysUser> selectSysUser(SysUserCondition condition);

    /**
     * 根据主键查询用户
     * @param id
     * @return
     */
    SysUser selectByPrimaryKey(Long id);

    /**
     * 更新用户
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(SysUser record);

    /**
     * 根据账号查询用户
     * @param account
     * @return
     */
    SysUser getByAccount(@Param("account") String account);

    /**
     * 根据账号更新密码
     *
     * @param sysUserResetPasswordCondition
     * @return
     */
    int updatePasswordByAccount(SysUserResetPasswordCondition sysUserResetPasswordCondition);

}