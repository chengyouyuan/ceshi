package com.winhxd.b2c.system.dao;

import com.winhxd.b2c.common.domain.system.dict.condition.SysDictCondition;
import com.winhxd.b2c.common.domain.system.dict.model.SysDict;

import java.util.List;

public interface SysDictMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysDict record);

    int insertSelective(SysDict record);

    SysDict selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysDict record);

    int updateByPrimaryKey(SysDict record);

    List<SysDict> selectSysDict(SysDictCondition condition);
}