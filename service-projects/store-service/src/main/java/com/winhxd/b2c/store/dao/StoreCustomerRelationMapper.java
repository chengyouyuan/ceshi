package com.winhxd.b2c.store.dao;

import com.winhxd.b2c.common.domain.store.condition.StoreCustomerRegionCondition;
import com.winhxd.b2c.common.domain.store.model.StoreCustomerRelation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface StoreCustomerRelationMapper {
    int deleteByPrimaryKey(Long id);

    int insert(StoreCustomerRelation record);

    int insertSelective(StoreCustomerRelation record);

    StoreCustomerRelation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StoreCustomerRelation record);

    int updateByPrimaryKey(StoreCustomerRelation record);

    /**
     * @author chengyy
     * @date 2018/8/3 13:34
     * @Description 根据条件查询数据
     * @param record 查询条件对象
     * @return 查询到的结果数据
     */
    List<StoreCustomerRelation> selectByCondition(@Param("condition") StoreCustomerRelation record);

    /**
     * 根据店铺ID，店铺状态查询绑定的用户ID
     * @param storeCustomerRegionCondition
     * @return
     */
    List<Long> selectCustomerIds(StoreCustomerRegionCondition storeCustomerRegionCondition);

    /**
     * 解绑
     *
     * @param list
     * @return
     */
    int unBundling(List<Long> list);

    /**
     * 换绑
     *
     * @param paraMap
     * @return
     */
    int changeBuind(Map<String, Object> paraMap);

    /**
     * 根据用户ID，查询门店id
     *
     * @param customerIds
     * @return
     */
    List<Long> selectStoreIds(@Param("customerIds") List<Long> customerIds);
}