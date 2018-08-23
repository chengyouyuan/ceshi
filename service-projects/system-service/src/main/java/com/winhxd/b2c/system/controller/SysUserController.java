package com.winhxd.b2c.system.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.system.user.condition.SysUserCondition;
import com.winhxd.b2c.common.domain.system.user.dto.SysUserPasswordDTO;
import com.winhxd.b2c.common.domain.system.user.model.SysUser;
import com.winhxd.b2c.common.feign.system.UserServiceClient;
import com.winhxd.b2c.system.service.SysRoleService;
import com.winhxd.b2c.system.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangzhengyang
 * @description 系统用户控制
 * @date 2018/8/1
 */
@Api(tags = "系统用户管理")
@RestController
@RequestMapping("/")
public class SysUserController implements UserServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(SysUserController.class);

    private static final String MODULE_NAME = "系统用户管理";

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysRoleService sysRoleService;


    /**
     * 新增用户
     * @author zhangzhengyang
     * @date 2018/8/2
     * @param sysUser
     * @return
     */
    @Override
    @ApiOperation(value = "新增用户")
    public ResponseResult<Long> save(@RequestBody SysUser sysUser){
        logger.info("{} - 新增用户, 参数：sysUser={}", MODULE_NAME, sysUser);
        ResponseResult<Long> result = new ResponseResult<>(BusinessCode.CODE_OK);
        sysUserService.save(sysUser);
        result.setData(sysUser.getId());
        return result;
    }

    /**
     * 修改用户
     * @author zhangzhengyang
     * @date 2018/8/2
     * @param sysUser
     * @return
     */
    @Override
    @ApiOperation(value = "修改用户")
    public ResponseResult<Void> modify(@RequestBody SysUser sysUser){
        logger.info("{} - 修改用户, 参数：sysUser={}", MODULE_NAME, sysUser);
        ResponseResult result = new ResponseResult<>(BusinessCode.CODE_OK);
        sysUserService.modify(sysUser);
        return result;
    }

    /**
     * 修改密码
     * @author zhangzhengyang
     * @date 2018/8/2
     * @param sysUser
     * @return
     */
    @Override
    @ApiOperation(value = "修改密码")
    public ResponseResult<Void> updatePassword(@RequestBody SysUserPasswordDTO sysUser){
        logger.info("{} - 修改密码, 参数：sysUser={}", MODULE_NAME, sysUser);
        ResponseResult result = new ResponseResult<>(BusinessCode.CODE_OK);
        sysUserService.modifyPassword(sysUser);
        return result;
    }

    /**
     * 查询用户列表
     * @author zhangzhengyang
     * @date 2018/8/2
     * @param condition
     * @return
     */
    @Override
    @ApiOperation(value = "查询用户列表")
    public ResponseResult<PagedList<SysUser>> find(@RequestBody SysUserCondition condition){
        logger.info("{} - 查询用户列表, 参数：condition={}", MODULE_NAME, condition);
        ResponseResult<PagedList<SysUser>> result = new ResponseResult<>(BusinessCode.CODE_OK);
        PagedList<SysUser> page = sysUserService.find(condition);
        result.setData(page);
        return result;
    }

    /**
     * 根据登录账号获取用户信息
     * @author zhangzhengyang
     * @date 2018/8/2
     * @param account
     * @return
     */
    @Override
    @ApiOperation(value = "根据登录账号获取用户信息")
    public ResponseResult<SysUser> getByAccount(@PathVariable("account") String account){
        logger.info("{} - 根据登录账号获取用户信息, 参数：account={}", MODULE_NAME, account);
        ResponseResult<SysUser> result = new ResponseResult<>(BusinessCode.CODE_OK);
        SysUser sysUser = sysUserService.getByAccount(account);
        if (null != sysUser) {
            sysUser.setPermissions(sysRoleService.getPermissionsByUser(sysUser.getId()));
        }
        result.setData(sysUser);
        return result;
    }

    /**
     * 根据主键获取用户信息
     * @author zhangzhengyang
     * @date 2018/8/2
     * @param id
     * @return
     */
    @Override
    @ApiOperation(value = "根据主键获取用户信息")
    public ResponseResult<SysUser> get(@PathVariable("id") Long id){
        logger.info("{} - 根据主键获取用户信息, 参数：id={}", MODULE_NAME, id);
        ResponseResult<SysUser> result = new ResponseResult<>(BusinessCode.CODE_OK);
        SysUser sysUser = sysUserService.get(id);
        result.setData(sysUser);
        return result;
    }

    /**
     * 根据主键禁用用户
     * @author zhangzhengyang
     * @date 2018/8/2
     * @param id
     * @return
     */
    @Override
    @ApiOperation(value = "根据主键禁用用户")
    public ResponseResult<Void> disabled(@PathVariable("id") Long id){
        logger.info("{} - 根据主键禁用用户, 参数：id={}", MODULE_NAME, id);
        ResponseResult result = new ResponseResult<>(BusinessCode.CODE_OK);
        sysUserService.disabled(id);
        return result;
    }
    /**
     * 根据主键启用用户
     * @author songkai
     * @date 2018/8/23
     * @param id
     * @return
     */
    @Override
    public ResponseResult<Void> enable(@PathVariable("id") Long id) {
        logger.info("{} - 根据主键启用用户, 参数：id={}", MODULE_NAME, id);
        ResponseResult result = new ResponseResult<>(BusinessCode.CODE_OK);
        sysUserService.enable(id);
        return result;
    }
}
