package com.winhxd.b2c.admin.module.system.controller;

import com.winhxd.b2c.admin.common.security.annotation.CheckPermission;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.system.security.enums.PermissionEnum;
import com.winhxd.b2c.common.domain.system.user.model.SysPermission;
import com.winhxd.b2c.common.feign.system.PermissionServiceClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author songkai
 * @description 系统权限管理控制层
 * @date 2018/8/13
 */
@Api(tags = "系统权限管理")
@RestController
@RequestMapping("permission")
public class PermissionController {

    private static final Logger logger = LoggerFactory.getLogger(PermissionController.class);

    private static final String MODULE_NAME = "系统权限管理";

    @Resource
    private PermissionServiceClient permissionServiceClient;


    @ApiOperation(value = "查询权限列表")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.CODE_1003, message = "没有权限")
    })
    @GetMapping(value = "/list")
    @CheckPermission({PermissionEnum.SYSTEM_MANAGEMENT_ROLE})
    public ResponseResult<List<SysPermission>> list() {
        logger.info("{} - 查询权限列表", MODULE_NAME);
        return permissionServiceClient.list();
    }

}
