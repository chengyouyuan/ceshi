package com.winhxd.b2c.admin.module.system.controller;

import com.winhxd.b2c.admin.common.context.UserManager;
import com.winhxd.b2c.admin.common.security.annotation.CheckPermission;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.system.security.enums.PermissionEnum;
import com.winhxd.b2c.common.domain.system.user.condition.SysRoleCondition;
import com.winhxd.b2c.common.domain.system.user.dto.SysRoleDTO;
import com.winhxd.b2c.common.domain.system.user.model.SysRole;
import com.winhxd.b2c.common.domain.system.user.vo.UserInfo;
import com.winhxd.b2c.common.feign.system.RoleServiceClient;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;

/**
 * @description 系统权限组管理控制层
 * @author zhangzhengyang
 * @date 2018/8/7
 */
@Api(tags = "系统权限组管理")
@RestController
public class RoleController {

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    private static final String MODULE_NAME = "系统权限组管理";

    @Resource
    private RoleServiceClient roleServiceClient;

    @ApiOperation("新增权限组")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.CODE_1003, message = "没有权限")
    })
    @PostMapping(value = "/role/add")
    @CheckPermission({PermissionEnum.SYSTEM_MANAGEMENT_ROLE_ADD})
    public ResponseResult add(@RequestBody SysRoleDTO sysRoleDTO) {
        logger.info("{} - 新增权限组, 参数：sysRole={}", MODULE_NAME, sysRoleDTO);

        UserInfo userInfo = UserManager.getCurrentUser();
        Date date = Calendar.getInstance().getTime();

        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(sysRoleDTO, sysRole);
     
        sysRole.setCreated(date);
        sysRole.setCreatedBy(userInfo.getId());
        sysRole.setCreatedByName(userInfo.getUsername());
        sysRole.setUpdated(date);
        sysRole.setUpdatedBy(userInfo.getId());
        sysRole.setUpdatedByName(userInfo.getUsername());

        return roleServiceClient.saveSysRole(sysRole);
    }

    @ApiOperation("编辑权限组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "权限组编号", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.CODE_1003, message = "没有权限")
    })
    @PostMapping(value = "/role/edit")
    @CheckPermission({PermissionEnum.SYSTEM_MANAGEMENT_ROLE_EDIT})
    public ResponseResult edit(@RequestBody SysRoleDTO sysRoleDTO) {
        logger.info("{} - 编辑权限组, 参数：sysRole={}", MODULE_NAME, sysRoleDTO);

        UserInfo userInfo = UserManager.getCurrentUser();
        Date date = Calendar.getInstance().getTime();

        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(sysRoleDTO, sysRole);

        sysRole.setUpdated(date);
        sysRole.setUpdatedBy(userInfo.getId());
        sysRole.setUpdatedByName(userInfo.getUsername());

        return roleServiceClient.modifySysRole(sysRole);
    }

    @ApiOperation(value = "查询权限组列表")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.CODE_1003, message = "没有权限")
    })
    @PostMapping(value = "/role/list")
    @CheckPermission({PermissionEnum.SYSTEM_MANAGEMENT_ROLE})
    public ResponseResult<PagedList<SysRole>> list(@RequestBody SysRoleCondition condition){
        logger.info("{} - 查询权限组列表, 参数：condition={}", MODULE_NAME, condition);
       return roleServiceClient.findSysRolePagedList(condition);
    }

    @ApiOperation(value = "根据主键获取权限组信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "权限组编号", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.CODE_1003, message = "没有权限")
    })
    @GetMapping("/role/get/{id}")
    @CheckPermission({PermissionEnum.SYSTEM_MANAGEMENT_ROLE})
    public ResponseResult<SysRole> getById(@PathVariable("id") Long id){
        logger.info("{} - 根据主键获取权限组信息, 参数：id={}", MODULE_NAME, id);
        return roleServiceClient.getSysRoleById(id);
    }

    @ApiOperation("根据编号删除权限组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "权限组编号", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.CODE_1003, message = "没有权限"),
            @ApiResponse(code = BusinessCode.CODE_301000, message = "权限组内有成员, 不可删除"),
    })
    @DeleteMapping(value = "/role/remove/{id}")
    @CheckPermission({PermissionEnum.SYSTEM_MANAGEMENT_ROLE_DELETE})
    public ResponseResult remove(@PathVariable("id") Long id) {
        logger.info("{} - 根据编号删除权限组, 参数：id={}", MODULE_NAME, id);
        return roleServiceClient.removeSysRoleById(id);
    }
}
