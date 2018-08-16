package com.winhxd.b2c.system.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.system.user.model.SysPermission;
import com.winhxd.b2c.common.feign.system.PermissionServiceClient;
import com.winhxd.b2c.system.service.SysPermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统权限管理
 *
 * @author songkai
 * @description 系统权限控制
 * @date 2018/8/13
 */
@Api(tags = "系统权限管理")
@RestController
@RequestMapping("/")
public class SysPermissionController implements PermissionServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(SysPermissionController.class);

    private static final String MODULE_NAME = "系统权限管理";

    @Autowired
    private SysPermissionService sysPermissionService;
    /**
     * 查询权限列表
     * @author songkai
     * @date 2018/8/13
     * @return
     */
    @Override
    @ApiOperation(value = "查询权限列表")
    public ResponseResult<List<SysPermission>> list() {
        logger.info("{} - 查询权限列表", MODULE_NAME);
        ResponseResult<List<SysPermission>> result = new ResponseResult<>(BusinessCode.CODE_OK);
        List<SysPermission> list = sysPermissionService.list();
        result.setData(list);
        return result;
    }
}
