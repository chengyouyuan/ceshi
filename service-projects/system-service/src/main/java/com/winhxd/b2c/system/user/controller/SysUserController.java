package com.winhxd.b2c.system.user.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.system.user.condition.SysUserCondition;
import com.winhxd.b2c.common.domain.system.user.dto.SysUserPasswordDTO;
import com.winhxd.b2c.common.domain.system.user.model.SysUser;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.system.UserServiceClient;
import com.winhxd.b2c.system.user.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.security.MD5Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;
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
@Api(tags = "系统用户管理")
@RestController
@RequestMapping("/")
public class SysUserController implements UserServiceClient {

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
    @Override
    @ApiOperation(value = "新增用户", response = Long.class)
    public ResponseResult<Long> add(@RequestBody SysUser sysUser){
        logger.info("{} - 新增用户, 参数：sysUser={}", MODULE_NAME, sysUser);
        ResponseResult<Long> result = new ResponseResult<>();
        try {
            if(StringUtils.isNotBlank(sysUser.getPassword())){
                sysUser.setPassword(DigestUtils.md5DigestAsHex(StringUtils.trim(sysUser.getPassword()).getBytes()));
            } else {
                sysUser.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
            }

            sysUserService.addSysUser(sysUser);
            result.setData(sysUser.getId());
        } catch (BusinessException e){
            logger.error("{} - 新增用户失败, 参数：sysUser={}", MODULE_NAME, sysUser, e);
            result = new ResponseResult<>(e.getErrorCode());
        } catch (Exception e){
            logger.error("{} - 新增用户失败, 参数：sysUser={}", MODULE_NAME, sysUser, e);
            result = new ResponseResult<>(BusinessCode.CODE_1001);
        }
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
    public ResponseResult update(@RequestBody SysUser sysUser){
        logger.info("{} - 修改用户, 参数：sysUser={}", MODULE_NAME, sysUser);
        ResponseResult<Long> result = new ResponseResult<>();
        try {
            String password = sysUser.getPassword();
            if(StringUtils.isNotBlank(password)){
                sysUser.setPassword(DigestUtils.md5DigestAsHex(StringUtils.trim(password).getBytes()));
            }else{
                sysUser.setPassword(null);
            }
            sysUserService.updateSysUser(sysUser);
        } catch (BusinessException e){
            logger.error("{} - 修改用户失败, 参数：sysUser={}", MODULE_NAME, sysUser, e);
            result = new ResponseResult<>(e.getErrorCode());
        } catch (Exception e){
            logger.error("{} - 修改用户失败, 参数：sysUser={}", MODULE_NAME, sysUser, e);
            result = new ResponseResult<>(BusinessCode.CODE_1001);
        }
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
    public ResponseResult updatePassword(@RequestBody SysUserPasswordDTO sysUser){
        logger.info("{} - 修改密码, 参数：sysUser={}", MODULE_NAME, sysUser);
        ResponseResult<Long> result = new ResponseResult<>();
        try {
            sysUserService.updatePassword(sysUser);
        } catch (BusinessException e){
            logger.error("{} - 修改密码失败, 参数：sysUser={}", MODULE_NAME, sysUser, e);
            result = new ResponseResult<>(e.getErrorCode());
        } catch (Exception e){
            logger.error("{} - 修改密码失败, 参数：sysUser={}", MODULE_NAME, sysUser, e);
            result = new ResponseResult<>(BusinessCode.CODE_1001);
        }
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
    public ResponseResult<PagedList<SysUser>> list(@RequestBody SysUserCondition condition){
        logger.info("{} - 查询用户列表, 参数：condition={}", MODULE_NAME, condition);
        ResponseResult<PagedList<SysUser>> result = new ResponseResult<>();
        try {
            PagedList<SysUser> page = sysUserService.selectSysUser(condition);
            result.setData(page);
        } catch (BusinessException e){
            logger.error("{} - 查询用户列表失败, 参数：condition={}", MODULE_NAME, condition, e);
            result = new ResponseResult(e.getErrorCode());
        } catch (Exception e){
            logger.error("{} - 查询用户列表失败, 参数：condition={}", MODULE_NAME, condition, e);
            result = new ResponseResult(BusinessCode.CODE_1001);
        }
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
        ResponseResult<SysUser> result = new ResponseResult<>();
        try {
            SysUser sysUser = sysUserService.getByAccount(account);
            result.setData(sysUser);
        } catch (BusinessException e){
            logger.error("{} - 根据登录账号获取用户信息失败, 参数：account={}", MODULE_NAME, account, e);
            result = new ResponseResult(e.getErrorCode());
        } catch (Exception e){
            logger.error("{} - 根据登录账号获取用户信息失败, 参数：account={}", MODULE_NAME, account, e);
            result = new ResponseResult(BusinessCode.CODE_1001);
        }
        return result;
    }

    /**
     * 根据主键获取用户信息
     * @author zhangzhengyang
     * @date 2018/8/2
     * @param userId
     * @return
     */
    @Override
    @ApiOperation(value = "根据主键获取用户信息")
    public ResponseResult<SysUser> getById(@PathVariable("userId") Long userId){
        logger.info("{} - 根据主键获取用户信息, 参数：userId={}", MODULE_NAME, userId);
        ResponseResult<SysUser> result = new ResponseResult<>();
        try {
            SysUser sysUser = sysUserService.getSysUserById(userId);
            result.setData(sysUser);
            return result;
        } catch (BusinessException e){
            logger.error("{} - 根据登录账号获取用户信息失败, 参数：userId={}", MODULE_NAME, userId, e);
            result = new ResponseResult(e.getErrorCode());
        } catch (Exception e){
            logger.error("{} - 根据登录账号获取用户信息失败, 参数：userId={}", MODULE_NAME, userId, e);
            result = new ResponseResult(BusinessCode.CODE_1001);
        }
        return result;
    }

}
