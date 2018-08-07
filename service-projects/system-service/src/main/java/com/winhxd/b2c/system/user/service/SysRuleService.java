package com.winhxd.b2c.system.user.service;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.system.user.condition.SysRoleCondition;
import com.winhxd.b2c.common.domain.system.user.model.SysRole;

public interface SysRuleService {

    /**
     * 新增角色
     * @author zhangzhengyang
     * @date 2018/8/7
     * @param sysRole
     * @return int
     */
    int addSysRule(SysRole sysRole);

    /**
     * 修改权限组
     * @author zhangzhengyang
     * @date 2018/8/7
     * @param sysRole
     * @return int
     */
    int updateSysRule(SysRole sysRole);


    /**
     * 查询权限组列表
     * @author zhangzhengyang
     * @date 2018/8/7
     * @param condition
     * @return java.util.List<com.winhxd.b2c.common.domain.system.sys.vo.SysRuleVO>
     */
    PagedList<SysRole> selectSysRule(SysRoleCondition condition);
    
    /**
     * 根据主键获取权限组信息
     * @author zhangzhengyang
     * @date 2018/8/7
     * @param id
     * @return com.winhxd.b2c.common.domain.system.sys.vo.SysRuleVO
     */
    SysRole getSysRuleById(Long id);

}
