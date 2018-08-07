package com.winhxd.b2c.system.user.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.system.user.condition.SysRoleCondition;
import com.winhxd.b2c.common.domain.system.user.model.SysRole;
import com.winhxd.b2c.common.domain.system.user.model.SysRolePermission;
import com.winhxd.b2c.system.user.dao.SysRoleMapper;
import com.winhxd.b2c.system.user.dao.SysRolePermissionMapper;
import com.winhxd.b2c.system.user.service.SysRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhangzhengyang
 * @description 系统权限组管理实现层
 * @date 2018/8/1
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysRolePermissionMapper sysRolePermissionMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addSysRule(SysRole sysRole) {
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
    public int updateSysRule(SysRole sysRole) {
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
    public PagedList<SysRole> selectSysRule(SysRoleCondition condition) {
        Page page = PageHelper.startPage(condition.getPageNo(),condition.getPageSize(),condition.getOrderBy());
        PagedList<SysRole> pagedList = new PagedList();
        pagedList.setData(sysRoleMapper.selectSysRole(condition));
        pagedList.setPageNo(condition.getPageNo());
        pagedList.setPageSize(condition.getPageSize());
        pagedList.setTotalRows(page.getTotal());
        return pagedList;
    }


    @Override
    public SysRole getSysRuleById(Long id) {
        SysRoleCondition condition = new SysRoleCondition();
        condition.setId(id);
        List<SysRole> sysRoleList = sysRoleMapper.selectSysRole(condition);
        if(CollectionUtils.isEmpty(sysRoleList)){
            return null;
        }
        return sysRoleList.get(0);
    }

}
