package com.winhxd.b2c.system.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.system.user.condition.SysUserCondition;
import com.winhxd.b2c.common.domain.system.user.condition.SysUserResetPasswordCondition;
import com.winhxd.b2c.common.domain.system.user.dto.SysUserPasswordDTO;
import com.winhxd.b2c.common.domain.system.user.enums.UserStatusEnum;
import com.winhxd.b2c.common.domain.system.user.model.SysUser;
import com.winhxd.b2c.common.domain.system.user.model.SysUserRole;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.system.dao.SysRolePermissionMapper;
import com.winhxd.b2c.system.dao.SysUserMapper;
import com.winhxd.b2c.system.dao.SysUserRoleMapper;
import com.winhxd.b2c.system.service.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author zhangzhengyang
 * @description 系统用户管理实现层
 * @date 2018/8/1
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    private static final Logger logger = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;
    @Autowired
    private Cache cache;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveSysUser(SysUser sysUser) {
        int count = sysUserMapper.insertSelective(sysUser);
        SysUserRole sysUserRule = new SysUserRole();
        sysUserRule.setUserId(sysUser.getId());
        sysUserRule.setRoleId(sysUser.getRoleId());
        sysUserRoleMapper.insertSelective(sysUserRule);
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int modifySysUser(SysUser sysUser) {
        int count = sysUserMapper.updateByPrimaryKeySelective(sysUser);
        sysUserRoleMapper.deleteByUserId(sysUser.getId());
        SysUserRole sysUserRule = new SysUserRole();
        sysUserRule.setUserId(sysUser.getId());
        sysUserRule.setRoleId(sysUser.getRoleId());
        sysUserRoleMapper.insertSelective(sysUserRule);
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyPassword(SysUserPasswordDTO newSysUser) {
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(newSysUser.getId());
        if(!sysUser.getPassword().equals(newSysUser.getPassword())){
            // 原密码输入错误
            throw new BusinessException(BusinessCode.CODE_302001);
        }
        if(sysUser.getPassword().equals(newSysUser.getNewPassword())){
            // 新密码与原密码相同
            throw new BusinessException(BusinessCode.CODE_302002);
        }
        sysUser.setPassword(newSysUser.getNewPassword());
        sysUser.setUpdated(newSysUser.getUpdated());
        sysUser.setUpdatedBy(newSysUser.getUpdatedBy());
        sysUserMapper.updateByPrimaryKeySelective(sysUser);
    }

    @Override
    public PagedList<SysUser> findSysUserPagedList(SysUserCondition condition) {
        Page page = PageHelper.startPage(condition.getPageNo(),condition.getPageSize(),condition.getOrderBy());
        PagedList<SysUser> pagedList = new PagedList();
        Page<SysUser> users = sysUserMapper.selectSysUser(condition);
        for (SysUser user : users) {
            user.setStatusDesc(UserStatusEnum.getDescMap().get(user.getStatus()));
        }
        pagedList.setData(users);
        pagedList.setPageNo(condition.getPageNo());
        pagedList.setPageSize(condition.getPageSize());
        pagedList.setTotalRows(page.getTotal());
        return pagedList;
    }

    @Override
    public SysUser getSysUserByAccount(String account) {
        return sysUserMapper.getByAccount(account);
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

    @Override
    public int disabled(Long id) {
        SysUser sysUser = new SysUser();
        sysUser.setId(id);
        sysUser.setStatus(UserStatusEnum.DISABLED.getCode());
        return sysUserMapper.updateByPrimaryKeySelective(sysUser);
    }

    @Override
    public int enable(Long id) {
        SysUser sysUser = new SysUser();
        sysUser.setId(id);
        sysUser.setStatus(UserStatusEnum.ENABLED.getCode());
        return sysUserMapper.updateByPrimaryKeySelective(sysUser);
    }

    @Override
    public int updatePassword(SysUserResetPasswordCondition sysUserResetPasswordCondition) {
        String verifyCode = cache.get(CacheName.ADMIN_USER_SEND_VERIFICATION_CODE + sysUserResetPasswordCondition.getUserAccount());
        if (StringUtils.isEmpty(verifyCode) ||
                !verifyCode.equals(sysUserResetPasswordCondition.getVerifyCode())) {
            logger.info("验证码错误");
            throw new BusinessException(BusinessCode.CODE_100808);
        }
        if (!sysUserResetPasswordCondition.getPwd().equals(sysUserResetPasswordCondition.getRepwd())) {
            logger.info("密码和确认密码不相同");
            throw new BusinessException(BusinessCode.CODE_1017);
        }
        //通过用户名修改密码
        sysUserResetPasswordCondition.setPwd(DigestUtils.md5DigestAsHex(org.apache.commons.lang3.StringUtils.trim(sysUserResetPasswordCondition.getPwd()).getBytes()));
        int i = sysUserMapper.updatePasswordByAccount(sysUserResetPasswordCondition);
        //删除缓存中的验证码
        cache.del(CacheName.ADMIN_USER_SEND_VERIFICATION_CODE + sysUserResetPasswordCondition.getUserAccount());
        return i;
    }
}
