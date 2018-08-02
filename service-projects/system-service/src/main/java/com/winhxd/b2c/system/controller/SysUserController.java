package com.winhxd.b2c.system.controller;

import com.github.pagehelper.Page;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.system.condition.SysUserCondition;
import com.winhxd.b2c.common.domain.system.model.SysUser;
import com.winhxd.b2c.system.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_500, message = "服务器内部异常")
    })
    @RequestMapping(value = "/api/user/3010/v1/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_500, message = "服务器内部异常")
    })
    @RequestMapping(value = "/api/user/3011/v1/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_500, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_301201, message = "原密码输入错误"),
            @ApiResponse(code = BusinessCode.CODE_301202, message = "新密码与原密码相同")
    })
    @RequestMapping(value = "/api/user/3012/v1/updatePassword", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_500, message = "服务器内部异常")
    })
    @RequestMapping(value = "/api/user/3013/v1/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_500, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_301401, message = "服务器内部异常")
    })
    @RequestMapping(value = "/api/user/3014/v1/get/{userCode}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_500, message = "服务器内部异常")
    })
    @RequestMapping(value = "/api/user/3015/v1/get/{userId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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
