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
     * 根据区域编码和级别查询是否是有效的测试区域
     * @param regionCodeList 区域编码集合
     * @return
     */
    StoreRegion selectByRegionCode(@Param("regionCodeList") List<String> regionCodeList);

    /**
     * 查父销售区域
     * @author: wangbaokuo
     * @date: 2018/8/21 20:59
     * @param: regionCodes
     * @return:
     */
    List<StoreRegionVO> selectFatherRegion(@Param("regionCodes") List<String> regionCodes);

    /**
     * 查子销售区域
     * @author: wangbaokuo
     * @date: 2018/8/21 20:59
     * @param: areaCode level
     * @return:
     */
    List<StoreRegionVO> selectSonRegion(@Param("areaCode") String areaCode, @Param("level") Integer level);

}