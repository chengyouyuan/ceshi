package com.winhxd.b2c.admin.module.system.controller;

import com.winhxd.b2c.admin.common.context.UserManager;
import com.winhxd.b2c.admin.common.security.annotation.CheckPermission;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.system.security.enums.PermissionEnum;
import com.winhxd.b2c.common.domain.system.user.condition.SysUserCondition;
import com.winhxd.b2c.common.domain.system.user.dto.SysUserPasswordDTO;
import com.winhxd.b2c.common.domain.system.user.model.SysUser;
import com.winhxd.b2c.common.domain.system.user.vo.UserInfo;
import com.winhxd.b2c.common.feign.system.UserServiceClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.tomcat.util.security.MD5Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;

@Api(tags = "系统用户管理")
@RestController("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private static final String MODULE_NAME = "系统用户管理";

    @Resource
    private UserServiceClient userServiceClient;

    @ApiOperation("新增用户")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.CODE_1003, message = "没有权限")
    })
    @PostMapping(value = "/add")
    @CheckPermission({PermissionEnum.SYSTEM_MANAGEMENT_USER_ADD})
    public ResponseResult add(SysUser sysUser) {
        logger.info("{} - 新增用户, 参数：sysUser={}", MODULE_NAME, sysUser);

        UserInfo userInfo = UserManager.getCurrentUser();
        Date date = Calendar.getInstance().getTime();

        sysUser.setCreated(date);
        sysUser.setCreatedBy(userInfo.getUserName());
        sysUser.setUpdated(date);
        sysUser.setUpdatedBy(userInfo.getUserName());

        return userServiceClient.add(sysUser);
    }

    @ApiOperation("编辑用户")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.CODE_1003, message = "没有权限")
    })
    @PutMapping(value = "/edit")
    @CheckPermission({PermissionEnum.SYSTEM_MANAGEMENT_USER_EDIT})
    public ResponseResult edit(SysUser sysUser) {
        logger.info("{} - 编辑用户, 参数：sysUser={}", MODULE_NAME, sysUser);

        UserInfo userInfo = UserManager.getCurrentUser();
        Date date = Calendar.getInstance().getTime();

        sysUser.setUpdated(date);
        sysUser.setUpdatedBy(userInfo.getUserName());

        return userServiceClient.update(sysUser);
    }

    @ApiOperation(value = "修改密码")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效")
    })
    @PutMapping(value = "/updatePassword")
    @CheckPermission({PermissionEnum.AUTHENTICATED})
    public ResponseResult updatePassword(SysUserPasswordDTO passwordDTO){
        logger.info("{} - 修改密码, 参数：passwordDTO={}", MODULE_NAME, passwordDTO);

        if(null == passwordDTO.getId()){
            return new ResponseResult(BusinessCode.CODE_1007);
        }

        UserInfo userInfo = UserManager.getCurrentUser();

        passwordDTO.setPassword(MD5Encoder.encode(passwordDTO.getPassword().getBytes()));
        passwordDTO.setNewPassword(MD5Encoder.encode(passwordDTO.getNewPassword().getBytes()));

        passwordDTO.setUpdated(Calendar.getInstance().getTime());
        passwordDTO.setUpdatedBy(userInfo.getUserName());

        return userServiceClient.updatePassword(passwordDTO);
    }

    @ApiOperation(value = "查询用户列表")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.CODE_1003, message = "没有权限")
    })
    @GetMapping(value = "/list")
    @CheckPermission({PermissionEnum.SYSTEM_MANAGEMENT_USER})
    public ResponseResult<PagedList<SysUser>> list(SysUserCondition condition){
        logger.info("{} - 查询用户列表, 参数：condition={}", MODULE_NAME, condition);
       return userServiceClient.list(condition);
    }

    @ApiOperation(value = "根据主键获取用户信息")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.CODE_1003, message = "没有权限")
    })
    @GetMapping("/get/{id}")
    @CheckPermission({PermissionEnum.SYSTEM_MANAGEMENT_USER})
    public ResponseResult<SysUser> getById(@PathVariable("id") Long id){
        logger.info("{} - 根据主键获取用户信息, 参数：id={}", MODULE_NAME, id);
        return userServiceClient.getById(id);
    }
}
