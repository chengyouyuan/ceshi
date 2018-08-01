package com.winhxd.b2c.system.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.page.GenericPage;
import com.winhxd.b2c.common.domain.system.sys.condition.SysUserCondition;
import com.winhxd.b2c.common.domain.system.sys.dto.SysUserDTO;
import com.winhxd.b2c.common.domain.system.sys.dto.SysUserPasswordDTO;
import com.winhxd.b2c.common.domain.system.sys.vo.SysUserVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.system.dao.SysUserMapper;
import com.winhxd.b2c.system.dao.SysUserRuleMapper;
import com.winhxd.b2c.system.model.SysUser;
import com.winhxd.b2c.system.model.SysUserRule;
import com.winhxd.b2c.system.service.SysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addSysUser(SysUserDTO sysUserDTO) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserDTO,sysUser);
        int count = sysUserMapper.insertSelective(sysUser);
        SysUserRule sysUserRule = new SysUserRule();
        sysUserRule.setUserId(sysUser.getId());
        sysUserRule.setRuleId(sysUserDTO.getRuleId());
        sysUserRuleMapper.insertSelective(sysUserRule);
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSysUser(SysUserDTO sysUserDTO) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserDTO,sysUser);
        int count = sysUserMapper.updateByPrimaryKeySelective(sysUser);
        sysUserRuleMapper.deleteByUserId(sysUser.getId());
        SysUserRule sysUserRule = new SysUserRule();
        sysUserRule.setUserId(sysUser.getId());
        sysUserRule.setRuleId(sysUserDTO.getRuleId());
        sysUserRuleMapper.insertSelective(sysUserRule);
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(SysUserPasswordDTO passwordDTO) {
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(passwordDTO.getId());
        if(!sysUser.getPassword().equals(passwordDTO.getPassword())){
            throw new BusinessException(BusinessCode.CODE_301);
        }
        if(sysUser.getPassword().equals(passwordDTO.getNewPassword())){
            throw new BusinessException(BusinessCode.CODE_302);
        }
        sysUser.setPassword(passwordDTO.getNewPassword());
        sysUser.setUpdated(passwordDTO.getUpdated());
        sysUser.setUpdatedBy(passwordDTO.getUpdatedBy());
        sysUserMapper.updateByPrimaryKeySelective(sysUser);
    }

    @Override
    public GenericPage<SysUserVO> selectSysUser(SysUserCondition condition) {
        Page page = PageHelper.startPage(condition.getPageNo(),condition.getPageSize());
        List<SysUserVO> sysUserVOList = sysUserMapper.selectSysUser(condition);
        GenericPage<SysUserVO> genericPage = new GenericPage<>();
        genericPage.setData(sysUserVOList);
        genericPage.setPageNo(condition.getPageNo());
        genericPage.setPageSize(condition.getPageSize());
        genericPage.setTotalRows(page.getTotal());
        return genericPage;
    }

    @Override
    public SysUserVO getSysUserByUserCode(String userCode) {
        SysUserCondition condition = new SysUserCondition();
        condition.setUserCode(userCode);
        return sysUserMapper.selectSysUser(condition).get(0);
    }

    @Override
    public SysUserVO getSysUserById(Long id) {
        SysUserCondition condition = new SysUserCondition();
        condition.setUserId(id);
        return sysUserMapper.selectSysUser(condition).get(0);
    }
}
