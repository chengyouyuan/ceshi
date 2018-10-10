package com.winhxd.b2c.system.dao;

import com.winhxd.b2c.common.domain.system.dict.model.SysDictItem;

import java.util.List;

public interface SysDictItemMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysDictItem record);

    int insertSelective(SysDictItem record);

    int insertBatch(List<SysDictItem> list);

    SysDictItem selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysDictItem record);

    int updateByPrimaryKey(SysDictItem record);

    List<SysDictItem> selectByDictCode(String dictCode);
    
    int deleteByDictId(Long dictId);
}