package com.winhxd.b2c.store.dao;

import com.winhxd.b2c.common.domain.store.condition.StoreProductManageCondition;
import com.winhxd.b2c.common.domain.store.model.StoreProductStatistics;

/**
 * @description:
 * @author: lvsen
 * @date: 2018/8/6 14:37
 */
public interface StoreProductStatisticsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(StoreProductStatistics record);

    int insertSelective(StoreProductStatistics record);

    StoreProductStatistics selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StoreProductStatistics record);

    int updateByPrimaryKey(StoreProductStatistics record);

    /**
     * 功能描述: 修改门店售卖数量
     * @param condition
     * @return
     * @auther lvsen
     * @date 2018/8/6 17:45
     */
    int updateQuantitySoldOutByStoreIdAndProdId(StoreProductManageCondition condition);
}