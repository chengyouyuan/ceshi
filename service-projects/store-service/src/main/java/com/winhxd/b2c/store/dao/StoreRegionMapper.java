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

    /**
     * 根据区域编码和级别查询是否是有效的测试区域
     * @param regionCode 区域编码
     * @param level 级别
     * @param prefixCode 身份前缀
     * @return
     */
    StoreRegion selectByRegionCode(@Param("regionCode") String regionCode, @Param("level") int level, @Param("prefixCode") String prefixCode);
}