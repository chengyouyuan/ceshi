package com.winhxd.b2c.system.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.system.user.condition.SysRoleCondition;
import com.winhxd.b2c.common.domain.system.user.model.SysPermission;
import com.winhxd.b2c.common.domain.system.user.model.SysRole;
import com.winhxd.b2c.common.domain.system.user.model.SysRolePermission;
import com.winhxd.b2c.system.dao.SysRoleMapper;
import com.winhxd.b2c.system.dao.SysRolePermissionMapper;
import com.winhxd.b2c.system.service.SysPermissionService;
import com.winhxd.b2c.system.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangzhengyang
 * @description 系统权限组管理实现层
 * @date 2018/8/1
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;
    @Autowired
    private SysPermissionService sysPermissionService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveSysRole(SysRole sysRole) {
        int count = sysRoleMapper.insertSelective(sysRole);
        if(null != sysRole.getPermissions()){
            for(SysRolePermission permission : sysRole.getPermissions()){
                permission.setRoleId(sysRole.getId());
                sysRolePermissionMapper.insertSelective(permission);
            }
        }
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int modifySysRole(SysRole sysRole) {
        int count = sysRoleMapper.updateByPrimaryKeySelective(sysRole);
        sysRolePermissionMapper.deleteByRoleId(sysRole.getId());
        if(null != sysRole.getPermissions()){
            for(SysRolePermission permission : sysRole.getPermissions()){
                permission.setRoleId(sysRole.getId());
                sysRolePermissionMapper.insertSelective(permission);
            }
        }
        return count;
    }

    @Override
    public PagedList<SysRole> findSysRolePagedList(SysRoleCondition condition) {
        Page page = PageHelper.startPage(condition.getPageNo(),condition.getPageSize(),condition.getOrderBy());
        PagedList<SysRole> pagedList = new PagedList();
        pagedList.setData(sysRoleMapper.selectSysRole(condition));
        pagedList.setPageNo(condition.getPageNo());
        pagedList.setPageSize(condition.getPageSize());
        pagedList.setTotalRows(page.getTotal());
        return pagedList;
    }


    @Override
    public SysRole getSysRoleById(Long id) {
        SysRole sysRole = sysRoleMapper.getSysRoleById(id);
        List<SysRolePermission> permissions = sysRole.getPermissions();
        //前端过滤半选状态菜单
        Map<String,SysRolePermission> grepMap = new HashMap();
        for (SysRolePermission rolePermission : permissions) {
            grepMap.put(rolePermission.getPermission(), rolePermission);
        }
        List<SysPermission> permissionList = sysPermissionService.list();
        permissionList.stream().forEach(sysPermission -> {
            grepPermison(sysPermission,grepMap,permissions);
        });
        return sysRole;
    }

    @Override
    public int removeSysRoleById(Long id) {
        sysRolePermissionMapper.deleteByRoleId(id);
        return sysRoleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<String> getPermissionsByUser(Long userId) {
        return sysRolePermissionMapper.selectPermissionByUserId(userId);
    }

    private void grepPermison(SysPermission sysPermission, Map<String,SysRolePermission> grepMap, List<SysRolePermission> permissions) {
        List<SysPermission> children = sysPermission.getChildren();
        if (children != null && children.size() > 0) {
            children.stream().forEach(permission -> {
                grepPermison(permission,grepMap,permissions);
                if (null == grepMap.get(permission.getPermission())) {
                    permissions.remove(grepMap.get(sysPermission.getPermission()));
                    grepMap.remove(sysPermission.getPermission());
                    return;
                }
            });
        }
    }

}
