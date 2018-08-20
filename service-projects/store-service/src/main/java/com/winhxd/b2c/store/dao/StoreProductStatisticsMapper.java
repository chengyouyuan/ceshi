package com.winhxd.b2c.store.dao;


import org.springframework.stereotype.Repository;

import com.winhxd.b2c.common.domain.store.model.StoreProductStatistics;

/**
 * @description:
 * @author: lvsen
 * @date: 2018/8/6 14:37
 */
@Repository
public interface StoreProductStatisticsMapper {
    /**
     * 物理删除
    * @Title: deleteByPrimaryKey 
    * @Description: TODO 
    * @param id
    * @return int
    * @author wuyuanbao
    * @date 2018年8月20日上午10:06:43
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入一条数据
    * @Title: insert 
    * @Description: TODO 
    * @param record
    * @return int
    * @author wuyuanbao
    * @date 2018年8月20日上午10:06:52
     */
    int insert(StoreProductStatistics record);

    /**
     * 插入一条数据
    * @Title: insertSelective 
    * @Description: TODO 
    * @param record
    * @return int
    * @author wuyuanbao
    * @date 2018年8月20日上午10:07:12
     */
    int insertSelective(StoreProductStatistics record);

    /**
     * 根据id查询
    * @Title: selectByPrimaryKey 
    * @Description: TODO 
    * @param id
    * @return StoreProductStatistics
    * @author wuyuanbao
    * @date 2018年8月20日上午10:07:31
     */
    StoreProductStatistics selectByPrimaryKey(Long id);

    /**
     * 更新数据
    * @Title: updateByPrimaryKeySelective 
    * @Description: TODO 
    * @param record
    * @return int
    * @author wuyuanbao
    * @date 2018年8月20日上午10:08:09
     */
    int updateByPrimaryKeySelective(StoreProductStatistics record);

    /**
     * 更新数据
    * @Title: updateByPrimaryKey 
    * @Description: TODO 
    * @param record
    * @return int
    * @author wuyuanbao
    * @date 2018年8月20日上午10:08:19
     */
    int updateByPrimaryKey(StoreProductStatistics record);

}