package com.winhxd.b2c.system.user.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.system.user.condition.SysUserCondition;
import com.winhxd.b2c.common.domain.system.user.dto.SysUserPasswordDTO;
import com.winhxd.b2c.common.domain.system.user.model.SysUser;
import com.winhxd.b2c.common.domain.system.user.model.SysUserRule;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.system.user.dao.SysRulePermissionMapper;
import com.winhxd.b2c.system.user.dao.SysUserMapper;
import com.winhxd.b2c.system.user.dao.SysUserRuleMapper;
import com.winhxd.b2c.system.user.service.SysUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhangzhengyang
 * @description 系统用户管理实现层
 * @date 2018/8/1
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysUserRuleMapper sysUserRuleMapper;
    @Resource
    private SysRulePermissionMapper sysRulePermissionMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addSysUser(SysUser sysUser) {
        int count = sysUserMapper.insertSelective(sysUser);
        SysUserRule sysUserRule = new SysUserRule();
        sysUserRule.setUserId(sysUser.getId());
        sysUserRule.setRuleId(sysUser.getRuleId());
        sysUserRuleMapper.insertSelective(sysUserRule);
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSysUser(SysUser sysUser) {
        int count = sysUserMapper.updateByPrimaryKeySelective(sysUser);
        sysUserRuleMapper.deleteByUserId(sysUser.getId());
        SysUserRule sysUserRule = new SysUserRule();
        sysUserRule.setUserId(sysUser.getId());
        sysUserRule.setRuleId(sysUser.getRuleId());
        sysUserRuleMapper.insertSelective(sysUserRule);
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(SysUserPasswordDTO newSysUser) {
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(newSysUser.getId());
        if(!sysUser.getPassword().equals(newSysUser.getPassword())){
            // 原密码输入错误
            throw new BusinessException(BusinessCode.CODE_301201);
        }
        if(sysUser.getPassword().equals(newSysUser.getNewPassword())){
            // 新密码与原密码相同
            throw new BusinessException(BusinessCode.CODE_301202);
        }
        sysUser.setPassword(newSysUser.getNewPassword());
        sysUser.setUpdated(newSysUser.getUpdated());
        sysUser.setUpdatedBy(newSysUser.getUpdatedBy());
        sysUserMapper.updateByPrimaryKeySelective(sysUser);
    }

    @Override
    public Page<SysUser> selectSysUser(SysUserCondition condition) {
        PageHelper.startPage(condition.getPageNo(),condition.getPageSize());
        return sysUserMapper.selectSysUser(condition);
    }

    @Override
    public SysUser getSysUserByUserCode(String userCode) {
        SysUserCondition condition = new SysUserCondition();
        condition.setUserCode(userCode);
        List<SysUser> sysUserList = sysUserMapper.selectSysUser(condition);
        if(CollectionUtils.isEmpty(sysUserList)){
            // 该用户不存在
            throw new BusinessException(BusinessCode.CODE_301401);
        }
        SysUser sysUser = sysUserList.get(0);
        List<String> permissionList = sysRulePermissionMapper.selectPermissionByUserId(sysUser.getId());
        sysUser.setPermissions(permissionList);
        return sysUser;
    }

    @Override
    public SysUser getSysUserById(Long id) {
        SysUserCondition condition = new SysUserCondition();
        condition.setUserId(id);
        List<SysUser> sysUserList = sysUserMapper.selectSysUser(condition);
        if(CollectionUtils.isEmpty(sysUserList)){
            return null;
        }
        return sysUserList.get(0);
    }
}
