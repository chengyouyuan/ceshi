package com.winhxd.b2c.system.controller;

import com.github.pagehelper.Page;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.system.condition.SysUserCondition;
import com.winhxd.b2c.common.domain.system.model.SysUser;
import com.winhxd.b2c.system.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zhangzhengyang
 * @description 系统用户控制
 * @date 2018/8/1
 */
@Api(value = "系统用户管理", tags = "api_user")
@RestController
@RequestMapping("/")
public class SysUserController {

    private static final Logger logger = LoggerFactory.getLogger(SysUserController.class);

    private static final String MODULE_NAME = "系统用户管理";

    @Resource
    private SysUserService sysUserService;

    /**
     * 新增用户
     * @author zhangzhengyang
     * @date 2018/8/2
     * @param sysUser
     * @return
     */
    @ApiOperation(value = "新增用户", response = Long.class)
    public ResponseResult<Long> add(@RequestBody SysUser sysUser){
        logger.info("{} - 新增用户, 参数：{}", MODULE_NAME, sysUser);
        ResponseResult<Long> result = new ResponseResult<>();
        try {
            sysUserService.addSysUser(sysUser);
            result.setData(sysUser.getId());
            return result;
        } catch (Exception e){
            logger.error("{} - 新增用户失败, 参数：{}", MODULE_NAME, sysUser);
            throw e;
        }
    }

    /**
     * 修改用户
     * @author zhangzhengyang
     * @date 2018/8/2
     * @param sysUser
     * @return
     */
    @ApiOperation(value = "修改用户")
    public ResponseResult update(@RequestBody SysUser sysUser){
        logger.info("{} - 修改用户, 参数：{}", MODULE_NAME, sysUser);
        ResponseResult<Long> result = new ResponseResult<>();
        try {
            sysUserService.updateSysUser(sysUser);
            return result;
        } catch (Exception e){
            logger.error("{} - 修改用户失败, 参数：{}", MODULE_NAME, sysUser);
            throw e;
        }
    }

    /**
     * 修改密码
     * @author zhangzhengyang
     * @date 2018/8/2
     * @param sysUser
     * @return
     */
    @ApiOperation(value = "修改密码")
    public ResponseResult updatePassword(@RequestBody SysUser sysUser){
        logger.info("{} - 修改密码, 参数：{}", MODULE_NAME, sysUser);
        ResponseResult<Long> result = new ResponseResult<>();
        try {
            sysUserService.updatePassword(sysUser);
            return result;
        } catch (Exception e){
            logger.error("{} - 修改密码失败, 参数：{}", MODULE_NAME, sysUser);
            throw e;
        }
    }

    /**
     * 查询用户列表
     * @author zhangzhengyang
     * @date 2018/8/2
     * @param condition
     * @return
     */
    @ApiOperation(value = "查询用户列表")
    public ResponseResult<Page<SysUser>> list(@RequestBody SysUserCondition condition){
        logger.info("{} - 查询用户列表, 参数：{}", MODULE_NAME, condition);
        ResponseResult<Page<SysUser>> result = new ResponseResult<>();
        try {
            Page<SysUser> page = sysUserService.selectSysUser(condition);
            result.setData(page);
            return result;
        } catch (Exception e){
            logger.error("{} - 查询用户列表失败, 参数：{}", MODULE_NAME, condition);
            throw e;
        }
    }

    /**
     * 根据登录账号获取用户信息
     * @author zhangzhengyang
     * @date 2018/8/2
     * @param userCode
     * @return
     */
    @ApiOperation(value = "根据登录账号获取用户信息")
    public ResponseResult<SysUser> getByUserCode(@PathVariable("userCode") String userCode){
        logger.info("{} - 根据登录账号获取用户信息, 参数：{}", MODULE_NAME, userCode);
        ResponseResult<SysUser> result = new ResponseResult<>();
        try {
            SysUser sysUser = sysUserService.getSysUserByUserCode(userCode);
            result.setData(sysUser);
            return result;
        } catch (Exception e){
            logger.error("{} - 根据登录账号获取用户信息失败, 参数：{}", MODULE_NAME, userCode);
            throw e;
        }
    }

    /**
     * 根据主键获取用户信息
     * @author zhangzhengyang
     * @date 2018/8/2
     * @param userId
     * @return
     */
    @ApiOperation(value = "根据主键获取用户信息")
    public ResponseResult<SysUser> getById(Long userId){
        logger.info("{} - 根据主键获取用户信息, 参数：{}", MODULE_NAME, userId);
        ResponseResult<SysUser> result = new ResponseResult<>();
        try {
            SysUser sysUser = sysUserService.getSysUserById(userId);
            result.setData(sysUser);
            return result;
        } catch (Exception e){
            logger.error("{} - 根据主键获取用户信息失败, 参数：{}", MODULE_NAME, userId);
            throw e;
        }
    }

}
