package com.winhxd.b2c.system.service;

import com.winhxd.b2c.common.domain.page.GenericPage;
import com.winhxd.b2c.common.domain.system.sys.condition.SysUserCondition;
import com.winhxd.b2c.common.domain.system.sys.dto.SysUserDTO;
import com.winhxd.b2c.common.domain.system.sys.dto.SysUserPasswordDTO;
import com.winhxd.b2c.common.domain.system.sys.vo.SysUserVO;

public interface SysUserService {

    /**
     * 新增用户
     * @author zhangzhengyang
     * @date 2018/8/1
     * @param sysUser
     * @return int
     */
    int addSysUser(SysUserDTO sysUser);

    /**
     * 修改用户
     * @author zhangzhengyang
     * @date 2018/8/1
     * @param sysUser
     * @return int
     */
    int updateSysUser(SysUserDTO sysUser);

    /**
     * 修改密码
     * @author zhangzhengyang
     * @date 2018/8/1
     * @param sysUserPasswordDTO
     * @return void
     */
    void updatePassword(SysUserPasswordDTO sysUserPasswordDTO);

    /**
     * 查询用户列表
     * @author zhangzhengyang
     * @date 2018/8/1
     * @param condition
     * @return java.util.List<com.winhxd.b2c.common.domain.system.sys.vo.SysUserVO>
     */
    GenericPage<SysUserVO> selectSysUser(SysUserCondition condition);

    /**
     * 根据登录账号获取用户信息
     * @author zhangzhengyang
     * @date 2018/8/1
     * @param userCode
     * @return com.winhxd.b2c.common.domain.system.sys.vo.SysUserVO
     */
    SysUserVO getSysUserByUserCode(String userCode);

    /**
     * 根据主键获取用户信息
     * @author zhangzhengyang
     * @date 2018/8/1
     * @param id
     * @return com.winhxd.b2c.common.domain.system.sys.vo.SysUserVO
     */
    SysUserVO getSysUserById(Long id);

}
