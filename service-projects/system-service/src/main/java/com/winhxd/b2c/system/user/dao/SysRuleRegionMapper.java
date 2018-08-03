package com.winhxd.b2c.system.user.dao;

import com.winhxd.b2c.common.domain.system.user.model.SysRuleRegion;

public interface SysRuleRegionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysRuleRegion record);

    int insertSelective(SysRuleRegion record);

    SysRuleRegion selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRuleRegion record);

    int updateByPrimaryKey(SysRuleRegion record);
}