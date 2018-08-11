package com.winhxd.b2c.store.dao;

import com.winhxd.b2c.common.domain.store.model.StoreRegion;
import com.winhxd.b2c.common.domain.store.vo.StoreRegionVO;
import org.apache.ibatis.annotations.Param;

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
     * @param record
     * @return List<StoreRegionVO>
     */
    List<StoreRegionVO> selectStoreRegions(StoreRegion record);

    /**
     * 查询重复测试门店区域
     * @param areaCode
     * @return List<StoreRegion>
     */
    List<StoreRegion> selectRepeatStoreRegion(@Param("areaCode") String areaCode);
}