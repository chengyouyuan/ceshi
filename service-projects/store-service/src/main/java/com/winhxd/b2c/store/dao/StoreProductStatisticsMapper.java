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

}