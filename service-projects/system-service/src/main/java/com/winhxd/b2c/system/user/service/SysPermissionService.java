package com.winhxd.b2c.system.user.service;

import com.winhxd.b2c.common.domain.system.user.model.SysPermission;

import java.util.List;

public interface SysPermissionService {

    /**
     * 查询权限列表
     * @author songkai
     * @date 2018/8/13
     * @return
     */
    List<SysPermission> list();

}
