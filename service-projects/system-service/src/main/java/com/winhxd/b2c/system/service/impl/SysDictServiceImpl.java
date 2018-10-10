package com.winhxd.b2c.system.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.system.dict.condition.SysDictCondition;
import com.winhxd.b2c.common.domain.system.dict.model.SysDict;
import com.winhxd.b2c.common.domain.system.dict.model.SysDictItem;
import com.winhxd.b2c.system.dao.SysDictItemMapper;
import com.winhxd.b2c.system.dao.SysDictMapper;
import com.winhxd.b2c.system.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangzhengyang
 * @description 系统字典管理实现层
 * @date 2018/9/26
 */
@Service
public class SysDictServiceImpl implements SysDictService {

    @Autowired
    private SysDictMapper sysDictMapper;
    @Autowired
    private SysDictItemMapper sysDictItemMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SysDict sysDict) {
        int result = sysDictMapper.insertSelective(sysDict);

        List<SysDictItem> items = sysDict.getItems();
        items = items.stream().map(item -> {item.setDictId(sysDict.getId());return item;}).collect(Collectors.toList());
        sysDictItemMapper.insertBatch(items);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int modify(SysDict sysDict) {
        int result = sysDictMapper.updateByPrimaryKeySelective(sysDict);

        sysDictItemMapper.deleteByDictId(sysDict.getId());
        List<SysDictItem> items = sysDict.getItems();
        items = items.stream().map(item -> {item.setDictId(sysDict.getId());return item;}).collect(Collectors.toList());
        sysDictItemMapper.insertBatch(items);
        return result;
    }


    @Override
    public PagedList<SysDict> find(SysDictCondition condition) {
        Page page = PageHelper.startPage(condition.getPageNo(),condition.getPageSize(),condition.getOrderBy());
        PagedList<SysDict> pagedList = new PagedList();
        pagedList.setData(sysDictMapper.selectSysDict(condition));
        pagedList.setPageNo(condition.getPageNo());
        pagedList.setPageSize(condition.getPageSize());
        pagedList.setTotalRows(page.getTotal());
        return pagedList;
    }

    @Override
    public List<SysDictItem> findByDictCode(String dictCode) {
        return sysDictItemMapper.selectByDictCode(dictCode);
    }

    @Override
    public SysDict get(Long id) {
        return sysDictMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int remove(Long id) {
        return sysDictMapper.deleteByPrimaryKey(id);
    }

}
