package com.winhxd.b2c.system.service;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.system.user.condition.SysUserCondition;
import com.winhxd.b2c.common.domain.system.user.condition.SysUserResetPasswordCondition;
import com.winhxd.b2c.common.domain.system.user.dto.SysUserPasswordDTO;
import com.winhxd.b2c.common.domain.system.user.model.SysUser;

public interface SysUserService {

    /**
     * 新增用户
     * @author zhangzhengyang
     * @date 2018/8/1
     * @param sysUser
     * @return int
     */
    int saveSysUser(SysUser sysUser);

    /**
     * 修改用户
     * @author zhangzhengyang
     * @date 2018/8/1
     * @param sysUser
     * @return int
     */
    int modifySysUser(SysUser sysUser);

    /**
     * 修改密码
     * @author zhangzhengyang
     * @date 2018/8/1
     * @param sysUser
     * @return void
     */
    void modifyPassword(SysUserPasswordDTO sysUser);

    /**
     * 查询用户列表
     * @author zhangzhengyang
     * @date 2018/8/1
     * @param condition
     * @return java.util.List<com.winhxd.b2c.common.domain.system.sys.vo.SysUserVO>
     */
    PagedList<SysUser> findSysUserPagedList(SysUserCondition condition);

    /**
     * 根据登录账号获取用户信息
     * @author zhangzhengyang
     * @date 2018/8/1
     * @param userCode
     * @return com.winhxd.b2c.common.domain.system.sys.vo.SysUserVO
     */
    SysUser getSysUserByAccount(String userCode);

    /**
     * 根据主键获取用户信息
     * @author zhangzhengyang
     * @date 2018/8/1
     * @param id
     * @return com.winhxd.b2c.common.domain.system.sys.vo.SysUserVO
     */
    SysUser getSysUserById(Long id);

    /**
     * 根据主键禁用用户
     * @author zhangzhengyang
     * @date 2018/8/7
     * @param id
     */
    int disabled(Long id);

    /**
     * 根据主键启用用户
     * @author songkai
     * @date 2018/8/23
     * @param id
     */
    int enable(Long id);

    /**
     * 重置密码
     *
     * @param sysUserResetPasswordCondition
     * @return
     */
    int updatePassword(SysUserResetPasswordCondition sysUserResetPasswordCondition);

}
