package com.winhxd.b2c.admin.module.system.controller;

import com.winhxd.b2c.admin.common.context.UserManager;
import com.winhxd.b2c.admin.common.security.annotation.CheckPermission;
import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.system.security.enums.PermissionEnum;
import com.winhxd.b2c.common.domain.system.user.condition.SysUserCondition;
import com.winhxd.b2c.common.domain.system.user.condition.SysUserResetPasswordCondition;
import com.winhxd.b2c.common.domain.system.user.dto.SysUserDTO;
import com.winhxd.b2c.common.domain.system.user.dto.SysUserPasswordDTO;
import com.winhxd.b2c.common.domain.system.user.enums.UserIdentityEnum;
import com.winhxd.b2c.common.domain.system.user.enums.UserStatusEnum;
import com.winhxd.b2c.common.domain.system.user.model.SysUser;
import com.winhxd.b2c.common.domain.system.user.vo.UserInfo;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.system.UserServiceClient;
import com.winhxd.b2c.common.util.JsonUtil;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;

/**
 * @description 系统用户管理控制层
 * @author zhangzhengyang
 * @date 2018/8/6
 */
@Api(tags = "系统用户管理")
@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private static final String MODULE_NAME = "系统用户管理";

    @Resource
    private Cache cache;

    @Resource
    private UserServiceClient userServiceClient;

    @ApiOperation("新增用户")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.CODE_1003, message = "没有权限"),
            @ApiResponse(code = BusinessCode.CODE_1013, message = "账号已存在")
    })
    @PostMapping(value = "/user/add")
    @CheckPermission({PermissionEnum.SYSTEM_MANAGEMENT_USER_ADD})
    public ResponseResult add(@RequestBody SysUserDTO sysUserDTO) {
        logger.info("{} - 新增用户, 参数：sysUser={}", MODULE_NAME, sysUserDTO);

        SysUser existsSysUser = userServiceClient.getByAccount(sysUserDTO.getAccount()).getData();
        if(null != existsSysUser){
            logger.warn("{} - 新增用户失败，账号已存在, 参数：sysUser={}", MODULE_NAME, sysUserDTO);
            return new ResponseResult(BusinessCode.CODE_1013);
        }

        UserInfo userInfo = UserManager.getCurrentUser();
        Date date = Calendar.getInstance().getTime();

        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserDTO, sysUser);
        if(StringUtils.isNotBlank(sysUser.getPassword())){
            sysUser.setPassword(DigestUtils.md5DigestAsHex(StringUtils.trim(sysUser.getPassword()).getBytes()));
        } else {
            sysUser.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        }
        sysUser.setStatus(UserStatusEnum.ENABLED.getCode());
        sysUser.setCreated(date);
        sysUser.setCreatedBy(userInfo.getId());
        sysUser.setCreatedByName(userInfo.getUsername());
        sysUser.setUpdated(date);
        sysUser.setUpdatedBy(userInfo.getId());
        sysUser.setUpdatedByName(userInfo.getUsername());

        return userServiceClient.saveSysUser(sysUser);
    }

    @ApiOperation("编辑用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户编号", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.CODE_1003, message = "没有权限"),
            @ApiResponse(code = BusinessCode.CODE_1013, message = "账号已存在")
    })
    @PostMapping(value = "/user/edit")
    @CheckPermission({PermissionEnum.SYSTEM_MANAGEMENT_USER_EDIT})
    public ResponseResult edit(@RequestBody SysUserDTO sysUserDTO) {
        logger.info("{} - 编辑用户, 参数：sysUser={}", MODULE_NAME, sysUserDTO);

        UserInfo userInfo = UserManager.getCurrentUser();
        Date date = Calendar.getInstance().getTime();

        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserDTO, sysUser);
        String password = sysUser.getPassword();
        if(StringUtils.isNotBlank(password)){
            sysUser.setPassword(DigestUtils.md5DigestAsHex(StringUtils.trim(password).getBytes()));
        }else{
            sysUser.setPassword(null);
        }

        sysUser.setUpdated(date);
        sysUser.setUpdatedBy(userInfo.getId());
        sysUser.setUpdatedByName(userInfo.getUsername());

        // 清除操作的用户缓存
        UserManager.delUserCache(sysUser.getId(), cache);
        return userServiceClient.modifySysUser(sysUser);
    }

    @ApiOperation(value = "修改密码")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效")
    })
    @PostMapping(value = "/user/updatePassword")
    @CheckPermission({PermissionEnum.AUTHENTICATED})
    public ResponseResult updatePassword(@RequestBody SysUserPasswordDTO passwordDTO){
        logger.info("{} - 修改密码开始", MODULE_NAME);

        if(null == passwordDTO.getId()){
            return new ResponseResult(BusinessCode.CODE_1007);
        }

        UserInfo userInfo = UserManager.getCurrentUser();

        passwordDTO.setPassword(DigestUtils.md5DigestAsHex(StringUtils.trim(passwordDTO.getPassword()).getBytes()));
        passwordDTO.setNewPassword(DigestUtils.md5DigestAsHex(StringUtils.trim(passwordDTO.getNewPassword()).getBytes()));

        passwordDTO.setUpdated(Calendar.getInstance().getTime());
        passwordDTO.setUpdatedBy(userInfo.getId());
        passwordDTO.setUpdatedByName(userInfo.getUsername());

        // 清除操作的用户缓存
        UserManager.delUserCache(passwordDTO.getId(), cache);
        return userServiceClient.updatePassword(passwordDTO);
    }

    @ApiOperation(value = "查询用户列表")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.CODE_1003, message = "没有权限")
    })
    @PostMapping(value = "/user/list")
    @CheckPermission({PermissionEnum.SYSTEM_MANAGEMENT_USER})
    public ResponseResult<PagedList<SysUser>> list(@RequestBody SysUserCondition condition){
        logger.info("{} - 查询用户列表, 参数：condition={}", MODULE_NAME, condition);
       return userServiceClient.findSysUserPagedList(condition);
    }

    @ApiOperation(value = "根据主键获取用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户编号", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.CODE_1003, message = "没有权限")
    })
    @GetMapping("/user/get/{id}")
    @CheckPermission({PermissionEnum.SYSTEM_MANAGEMENT_USER})
    public ResponseResult<SysUser> getById(@PathVariable("id") Long id){
        logger.info("{} - 根据主键获取用户信息, 参数：id={}", MODULE_NAME, id);
        return userServiceClient.getSysUserById(id);
    }

    @ApiOperation("验证用户是否已存在")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "账号", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.CODE_1003, message = "没有权限")
    })
    @GetMapping(value = "/user/existsAccount")
    @CheckPermission({PermissionEnum.SYSTEM_MANAGEMENT_USER_ADD})
    public ResponseResult<Boolean> existsAccount(String account) {
        logger.info("{} - 验证用户是否已存在, 参数：account={}", MODULE_NAME, account);

        SysUser sysUser = userServiceClient.getByAccount(account).getData();
        if(null != sysUser){
            return new ResponseResult<>(true);
        }

        return new ResponseResult<>(false);
    }

    @ApiOperation(value = "根据主键禁用用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户编号", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.CODE_1003, message = "没有权限"),
            @ApiResponse(code = BusinessCode.CODE_1015, message = "操作被禁止"),
    })
    @GetMapping("/user/disabled/{id}")
    @CheckPermission({PermissionEnum.SYSTEM_MANAGEMENT_USER_DELETE})
    public ResponseResult<Void> disabled(@PathVariable("id") Long id){
        logger.info("{} - 根据主键禁用用户, 参数：id={}", MODULE_NAME, id);

        SysUser sysUser = userServiceClient.getSysUserById(id).getData();
        if (null != sysUser.getIdentity() && sysUser.getIdentity().intValue() == UserIdentityEnum.SUPER_ADMIN.getIdentity()) {
            logger.warn("{} - 操作被禁止", MODULE_NAME);
            return new ResponseResult(BusinessCode.CODE_1015);
        }

        // 清除操作的用户缓存
        UserManager.delUserCache(id, cache);
        return userServiceClient.disabled(id);
    }

    @ApiOperation(value = "根据主键启用用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户编号", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.CODE_1003, message = "没有权限")
    })
    @GetMapping("/user/enable/{id}")
    @CheckPermission({PermissionEnum.SYSTEM_MANAGEMENT_USER_ENABLE})
    public ResponseResult<Void> enable(@PathVariable("id") Long id){
        logger.info("{} - 根据主键启用用户, 参数：id={}", MODULE_NAME, id);
        // 清除操作的用户缓存
        UserManager.delUserCache(id, cache);
        return userServiceClient.enable(id);
    }

    @ApiOperation(value = "重置密码，根据用户名发送验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userAccount", value = "用户名", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    @PostMapping("/user/sendVerifyCode")
    public ResponseResult<Void> sendVerifyCode(@RequestBody SysUserResetPasswordCondition sysUserResetPasswordCondition) {
        logger.info("根据用户名发送验证码, 参数：userAccount={}", JsonUtil.toJSONString(sysUserResetPasswordCondition));
        String userAccount = sysUserResetPasswordCondition.getUserAccount();
        if (StringUtils.isEmpty(userAccount)) {
            logger.warn("重置密码发送验证码时，用户名为空");
            throw new BusinessException(BusinessCode.CODE_1016);
        }
        ResponseResult<Void> enable = userServiceClient.sendVerifyCode(sysUserResetPasswordCondition);
        return enable;
    }

    @ApiOperation(value = "重置密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userAccount", value = "用户名", required = true),
            @ApiImplicitParam(name = "retrieveWay", value = "重置密码类型", required = true),
            @ApiImplicitParam(name = "verifyCode", value = "验证码", required = true),
            @ApiImplicitParam(name = "pwd", value = "密码", required = true),
            @ApiImplicitParam(name = "repwd", value = "确认密码", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    @PostMapping("/user/resetPassword")
    public ResponseResult<Void> resetPassword(@RequestBody SysUserResetPasswordCondition sysUserResetPasswordCondition) {
        logger.info("{}重置密码开始", sysUserResetPasswordCondition.getUserAccount());
        if (StringUtils.isEmpty(sysUserResetPasswordCondition.getUserAccount())) {
            logger.error("重置密码用户名为空");
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        if (StringUtils.isEmpty(sysUserResetPasswordCondition.getVerifyCode())) {
            logger.error("重置密码，验证码为空");
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        if (StringUtils.isEmpty(sysUserResetPasswordCondition.getPwd()) ||
                StringUtils.isEmpty(sysUserResetPasswordCondition.getRepwd())) {
            logger.error("重置密码，密码为空");
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        ResponseResult<Void> result = userServiceClient.resetPassword(sysUserResetPasswordCondition);
        return result;
    }

}
