package com.winhxd.b2c.system.user.service.impl;

import com.winhxd.b2c.common.domain.system.security.enums.PermissionEnum;
import com.winhxd.b2c.common.domain.system.user.model.SysPermission;
import com.winhxd.b2c.system.user.service.SysPermissionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author songkai
 * @description 系统权限管理实现层
 * @date 2018/8/13
 */
@Service
public class SysPermissionServiceImpl implements SysPermissionService {

    private static List<SysPermission> permissionNodeList;

    @Override
    public List<SysPermission> list() {
        if (null == permissionNodeList || permissionNodeList.size() <= 0) {
            initPermissionNodes();
        }
        return permissionNodeList;
    }

    private void initPermissionNodes() {
        permissionNodeList = new ArrayList<>();
        for (PermissionEnum permission : PermissionEnum.values()) {
            SysPermission node = buildPermissionNode(permission);
            if (permission.getParent() == null) {
                permissionNodeList.add(node);
            } else {
                SysPermission parent = findPermissionNode(permissionNodeList, permission.getParent().toString());
                node.setParent(parent);
                parent.getChildren().add(node);
            }
        }
    }

    private SysPermission buildPermissionNode(PermissionEnum permission) {
        SysPermission tree = new SysPermission();
        tree.setPermission(permission.toString());
        tree.setPermissionName(permission.getName());
        tree.setChildren(new ArrayList<>(0));

        return tree;
    }

    private SysPermission findPermissionNode(List<SysPermission> list, String code) {
        for (SysPermission tree : list) {
            if (tree.getPermission().equals(code)) {
                return tree;
            }
            SysPermission permissionNode = findPermissionNode(tree.getChildren(), code);
            if (permissionNode != null) {
                return permissionNode;
            }
        }
        return null;
    }
}
