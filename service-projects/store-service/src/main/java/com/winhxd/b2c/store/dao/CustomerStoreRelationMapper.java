package com.winhxd.b2c.store.dao;

import com.winhxd.b2c.common.domain.store.model.CustomerStoreRelation;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CustomerStoreRelationMapper{
    int deleteByPrimaryKey(Long id);

    int insert(CustomerStoreRelation record);

    int insertSelective(CustomerStoreRelation record);

    CustomerStoreRelation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CustomerStoreRelation record);

    int updateByPrimaryKey(CustomerStoreRelation record);
    /**
     * @author chengyy
     * @date 2018/8/3 13:34
     * @Description 根据条件查询数据
     * @param record 查询条件对象
     * @return 查询到的结果数据
     */
    List<CustomerStoreRelation> selectByCondition(@Param("condition") CustomerStoreRelation record);
}