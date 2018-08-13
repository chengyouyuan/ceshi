package com.winhxd.b2c.store.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.store.condition.StoreRegionCondition;
import com.winhxd.b2c.common.domain.store.enums.StoreRegionEnum;
import com.winhxd.b2c.common.domain.store.model.StoreRegion;
import com.winhxd.b2c.common.domain.store.vo.StoreRegionVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.store.dao.StoreRegionMapper;
import com.winhxd.b2c.store.service.StoreRegionService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author: wangbaokuo
 * @date: 2018/8/10 14:56
 */
@Service
public class StoreRegionServiceImpl implements StoreRegionService{

    private static final Logger logger = LoggerFactory.getLogger(StoreRegionServiceImpl.class);

    @Resource
    StoreRegionMapper storeRegionMapper;

    @Override
    public PagedList<StoreRegionVO> findStoreRegions(StoreRegionCondition condition) {
        Page page = PageHelper.startPage(condition.getPageNo(),condition.getPageSize());
        PagedList<StoreRegionVO> pagedList = new PagedList();
        StoreRegion region = new StoreRegion();
        BeanUtils.copyProperties(condition, region);
        pagedList.setData(storeRegionMapper.selectStoreRegions(region));
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
        String areaCode = condition.getAreaCode().substring(0, 3);
        List<StoreRegion> repeat = storeRegionMapper.selectRepeatStoreRegion(areaCode);
        if (CollectionUtils.isNotEmpty(repeat)) {
            logger.error("保存测试门店区域异常{} -> 销售区域重复");
            throw new BusinessException(BusinessCode.CODE_103901);
        }
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

    @Override
    public StoreRegion selectByRegionCode(String regionCode) {
        return storeRegionMapper.selectByRegionCode(regionCode);
    }
}
