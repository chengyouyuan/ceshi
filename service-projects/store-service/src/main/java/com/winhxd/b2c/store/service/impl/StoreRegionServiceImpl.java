package com.winhxd.b2c.store.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.store.condition.StoreRegionCondition;
import com.winhxd.b2c.common.domain.store.enums.StoreRegionEnum;
import com.winhxd.b2c.common.domain.store.model.StoreRegion;
import com.winhxd.b2c.common.domain.store.vo.StoreRegionVO;
import com.winhxd.b2c.store.dao.StoreRegionMapper;
import com.winhxd.b2c.store.service.StoreRegionService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author: wangbaokuo
 * @date: 2018/8/10 14:56
 */
@Service
public class StoreRegionServiceImpl implements StoreRegionService{

    @Resource
    StoreRegionMapper storeRegionMapper;

    @Override
    public PagedList<StoreRegionVO> findStoreRegions(StoreRegionCondition condition) {
        Page page = PageHelper.startPage(condition.getPageNo(),condition.getPageSize(), " created desc ");
        PagedList<StoreRegionVO> pagedList = new PagedList();
        pagedList.setData(storeRegionMapper.selectStoreRegions(condition));
        pagedList.setPageNo(condition.getPageNo());
        pagedList.setPageSize(condition.getPageSize());
        pagedList.setTotalRows(page.getTotal());
        return pagedList;

    }

    @Override
    public int removeStoreRegion(Long id) {
        StoreRegion storeRegion = new StoreRegion();
        storeRegion.setId(id);
        storeRegion.setStatus(StoreRegionEnum.VALIDATE.getCode());
        storeRegion.setUpdated(new Date());
        storeRegion.setUpdatedBy(Long.parseLong(UserContext.getCurrentAdminUser().getAccount()));
        return storeRegionMapper.updateByPrimaryKey(storeRegion);
    }

    @Override
    public int saveStoreRegion(StoreRegionCondition condition) {
        StoreRegion storeRegion = new StoreRegion();
        BeanUtils.copyProperties(condition, storeRegion);
        Date current = new Date();
        Long account = Long.parseLong(UserContext.getCurrentAdminUser().getAccount());
        storeRegion.setCreated(current);
        storeRegion.setUpdated(current);
        storeRegion.setCreatedBy(account);
        storeRegion.setUpdatedBy(account);
        storeRegion.setStatus(StoreRegionEnum.EFFICTIVE.getCode());
        return storeRegionMapper.insertSelective(storeRegion);
    }
}
