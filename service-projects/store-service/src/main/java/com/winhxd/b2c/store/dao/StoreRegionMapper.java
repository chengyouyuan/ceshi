package com.winhxd.b2c.store.dao;

import com.winhxd.b2c.common.domain.store.condition.StoreRegionCondition;
import com.winhxd.b2c.common.domain.store.model.StoreRegion;
import com.winhxd.b2c.common.domain.store.vo.StoreRegionVO;

import java.util.List;

public interface StoreRegionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(StoreRegion record);

    int insertSelective(StoreRegion record);

    StoreRegion selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StoreRegion record);

    int updateByPrimaryKey(StoreRegion record);

    /**
     * 查询StoreRegionVO
     * @param condition
     * @return List<StoreRegionVO>
     */
    List<StoreRegionVO> selectStoreRegions(StoreRegionCondition condition);
}