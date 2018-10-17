package com.winhxd.b2c.system.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.system.dict.condition.SysDictCondition;
import com.winhxd.b2c.common.domain.system.dict.model.SysDict;
import com.winhxd.b2c.common.domain.system.dict.model.SysDictItem;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.system.dao.SysDictItemMapper;
import com.winhxd.b2c.system.dao.SysDictMapper;
import com.winhxd.b2c.system.service.SysDictService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangzhengyang
 * @description 系统字典管理实现层
 * @date 2018/9/26
 */
@Service
public class SysDictServiceImpl implements SysDictService {

    private static final Logger logger = LoggerFactory.getLogger(SysDictServiceImpl.class);

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
        //字典组修改前校验编码是否重复
        List<SysDict> sysDicts = sysDictMapper.selectSysDictExpectId(sysDict);
        sysDicts = sysDicts.stream().filter(dict -> dict.getCode().equals(sysDict.getCode())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(sysDicts)) {
            logger.info("修改字典组时，编码重复");
            throw new BusinessException(BusinessCode.CODE_303001);
        }
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
