package com.winhxd.b2c.admin.module.system.controller;

import com.winhxd.b2c.admin.common.context.UserManager;
import com.winhxd.b2c.admin.common.security.annotation.CheckPermission;
import com.winhxd.b2c.admin.common.security.annotation.MenuAssign;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.system.security.enums.MenuEnum;
import com.winhxd.b2c.common.domain.system.security.enums.PermissionEnum;
import com.winhxd.b2c.common.domain.system.user.condition.SysUserCondition;
import com.winhxd.b2c.common.domain.system.user.dto.SysUserDTO;
import com.winhxd.b2c.common.domain.system.user.dto.SysUserPasswordDTO;
import com.winhxd.b2c.common.domain.system.user.model.SysUser;
import com.winhxd.b2c.common.domain.system.user.vo.UserInfo;
import com.winhxd.b2c.common.feign.system.UserServiceClient;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.security.MD5Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    private UserServiceClient userServiceClient;

    @ApiOperation("新增用户")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.CODE_1003, message = "没有权限")
    })
    @PostMapping(value = "/user/add")
    @CheckPermission({PermissionEnum.SYSTEM_MANAGEMENT_USER_ADD})
    public ResponseResult add(@RequestBody SysUserDTO sysUserDTO) {
        logger.info("{} - 新增用户, 参数：sysUser={}", MODULE_NAME, sysUserDTO);

        UserInfo userInfo = UserManager.getCurrentUser();
        Date date = Calendar.getInstance().getTime();

        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserDTO, sysUser);
        if(StringUtils.isNotBlank(sysUser.getPassword())){
            sysUser.setPassword(DigestUtils.md5DigestAsHex(StringUtils.trim(sysUser.getPassword()).getBytes()));
        } else {
            sysUser.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        }
        sysUser.setCreated(date);
        sysUser.setCreatedBy(userInfo.getUsername());
        sysUser.setUpdated(date);
        sysUser.setUpdatedBy(userInfo.getUsername());

        return userServiceClient.add(sysUser);
    }

    @ApiOperation("编辑用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户编号", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.CODE_1003, message = "没有权限")
    })
    @PutMapping(value = "/user/edit")
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
        sysUser.setUpdatedBy(userInfo.getUsername());

        return userServiceClient.update(sysUser);
    }

    @ApiOperation(value = "修改密码")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效")
    })
    @PutMapping(value = "/user/updatePassword")
    @CheckPermission({PermissionEnum.AUTHENTICATED})
    public ResponseResult updatePassword(@RequestBody SysUserPasswordDTO passwordDTO){
        logger.info("{} - 修改密码, 参数：passwordDTO={}", MODULE_NAME, passwordDTO);

        if(null == passwordDTO.getId()){
            return new ResponseResult(BusinessCode.CODE_1007);
        }

        UserInfo userInfo = UserManager.getCurrentUser();

        passwordDTO.setPassword(MD5Encoder.encode(passwordDTO.getPassword().getBytes()));
        passwordDTO.setNewPassword(MD5Encoder.encode(passwordDTO.getNewPassword().getBytes()));

        passwordDTO.setUpdated(Calendar.getInstance().getTime());
        passwordDTO.setUpdatedBy(userInfo.getUsername());

        return userServiceClient.updatePassword(passwordDTO);
    }

    @ApiOperation(value = "查询用户列表")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.CODE_1003, message = "没有权限")
    })
    @GetMapping(value = "/user/list")
    @CheckPermission({PermissionEnum.SYSTEM_MANAGEMENT_USER})
    public ResponseResult<PagedList<SysUser>> list(@RequestBody SysUserCondition condition){
        logger.info("{} - 查询用户列表, 参数：condition={}", MODULE_NAME, condition);
       return userServiceClient.list(condition);
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
        return userServiceClient.getById(id);
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
    @PostMapping(value = "/user/existsAccount")
    @CheckPermission({PermissionEnum.SYSTEM_MANAGEMENT_USER_ADD})
    public ResponseResult<Boolean> add(String account) {
        logger.info("{} - 验证用户是否已存在, 参数：account={}", MODULE_NAME, account);

        SysUser sysUser = userServiceClient.getByAccount(account).getData();
        if(null != sysUser){
            return new ResponseResult<>(true);
        }

        return new ResponseResult<>(false);
    }

    @ApiOperation(value = "根据主键获取禁用用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户编号", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.CODE_1003, message = "没有权限")
    })
    @DeleteMapping("/user/disabled/{id}")
    @CheckPermission({PermissionEnum.SYSTEM_MANAGEMENT_USER_DELETE})
    public ResponseResult<SysUser> disabled(@PathVariable("id") Long id){
        logger.info("{} - 根据主键获取禁用用户, 参数：id={}", MODULE_NAME, id);
        return userServiceClient.disabled(id);
    }

    @ApiOperation(value = "用户管理页面")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.CODE_1003, message = "没有权限")
    })
    @GetMapping("/user/page")
    @CheckPermission({PermissionEnum.SYSTEM_MANAGEMENT_USER})
    @MenuAssign({MenuEnum.SYSTEM_MANAGEMENT_USER})
    public ResponseResult<Map<String,Object>> page(){
        logger.info("{} - 用户管理页面", MODULE_NAME);
        ResponseResult<Map<String,Object>> result = new ResponseResult<>(BusinessCode.CODE_OK);
        Map<String,Object> data = new HashMap<>();
        Map<String,String> url = new HashMap<>();
        url.put("list","/user/list");
        url.put("add","/user/add");
        url.put("edit","/user/edit");
        data.put("url",url);
        result.setData(data);
        return result;
    }

}
