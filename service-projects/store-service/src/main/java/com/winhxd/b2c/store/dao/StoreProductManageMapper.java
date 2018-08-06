package com.winhxd.b2c.store.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.winhxd.b2c.common.domain.store.condition.StoreProductManageCondition;
import com.winhxd.b2c.common.domain.store.model.StoreProductManage;

public interface StoreProductManageMapper {
    int deleteByPrimaryKey(Long id);

    int insert(StoreProductManage record);

    int insertSelective(StoreProductManage record);

    StoreProductManage selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StoreProductManage record);

    int updateByPrimaryKey(StoreProductManage record);
    
    /**
     * 通过门店商品管理condition获取sku集合(带排序)
     * 目前支持价格跟创建时间排序，是否推荐，门店id,状态
    * @Title: selectPutawaySkusByConditon 
    * @Description: TODO 
    * @param condition
    * @return List<String>
    * @author wuyuanbao
    * @date 2018年8月4日下午3:34:29
     */
    List<String> selectSkusByConditon(@Param("condition")StoreProductManageCondition condition);
}