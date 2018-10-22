package com.winhxd.b2c.system.service;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.system.user.condition.SysRoleCondition;
import com.winhxd.b2c.common.domain.system.user.model.SysRole;

import java.util.List;

public interface SysRoleService {

    /**
     * 新增角色
     * @author zhangzhengyang
     * @date 2018/8/7
     * @param sysRole
     * @return int
     */
    int saveSysRole(SysRole sysRole);

    /**
     * 修改权限组
     * @author zhangzhengyang
     * @date 2018/8/7
     * @param sysRole
     * @return int
     */
    int modifySysRole(SysRole sysRole);


    /**
     * 查询权限组列表
     * @author zhangzhengyang
     * @date 2018/8/7
     * @param condition
     * @return java.util.List<com.winhxd.b2c.common.domain.system.sys.vo.SysRuleVO>
     */
    PagedList<SysRole> findSysRolePagedList(SysRoleCondition condition);
    
    /**
     * 根据主键获取权限组信息
     * @author zhangzhengyang
     * @date 2018/8/7
     * @param id
     * @return com.winhxd.b2c.common.domain.system.sys.vo.SysRuleVO
     */
    SysRole getSysRoleById(Long id);

    /**
     * 根据主键删除权限组信息
     * @author zhangzhengyang
     * @date 2018/8/9
     * @param id
     */
    int removeSysRoleById(Long id);


    /**
     * 根据用户ID获取权限列表
     * @param userId
     * @return
     */
    List<String> getPermissionsByUser(Long userId);

}
