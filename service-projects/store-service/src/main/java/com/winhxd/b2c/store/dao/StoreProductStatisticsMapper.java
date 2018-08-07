package com.winhxd.b2c.store.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.winhxd.b2c.common.domain.store.condition.StoreProductManageCondition;
import com.winhxd.b2c.common.domain.store.condition.StoreProductStatisticsCondition;
import com.winhxd.b2c.common.domain.store.model.StoreProductStatistics;

/**
 * @description:
 * @author: lvsen
 * @date: 2018/8/6 14:37
 */
@Repository
public interface StoreProductStatisticsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(StoreProductStatistics record);

    int insertSelective(StoreProductStatistics record);

    StoreProductStatistics selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StoreProductStatistics record);

    int updateByPrimaryKey(StoreProductStatistics record);
    /**
     * 通过condition获取门店商品统计信息
    * @Title: selectByCondition 
    * @Description: TODO 
    * @param condition
    * @return List<StoreProductStatistics>
    * @author wuyuanbao
    * @date 2018年8月7日上午10:11:09
     */
    List<StoreProductStatistics> selectByCondition(@Param("condition")StoreProductStatisticsCondition condition);

    /**
     * 功能描述: 修改门店售卖数量
     * @param condition
     * @return
     * @auther lvsen
     * @date 2018/8/6 17:45
     */
    int updateQuantitySoldOutByStoreIdAndProdId(StoreProductManageCondition condition);
}