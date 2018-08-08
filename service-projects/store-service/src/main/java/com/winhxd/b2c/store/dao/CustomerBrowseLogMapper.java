package com.winhxd.b2c.store.dao;

import com.winhxd.b2c.common.domain.store.model.CustomerBrowseLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerBrowseLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CustomerBrowseLog record);

    int insertSelective(CustomerBrowseLog record);

    CustomerBrowseLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CustomerBrowseLog record);

    int updateByPrimaryKey(CustomerBrowseLog record);

    /**
     *  用户退出时查询记录的id
     *
     * @param storeId 门店编码
     * @param customerId 用户编码
     * @return id
     */
    CustomerBrowseLog selectIdForLoginOut(@Param("storeId") Long storeId, @Param("customerId") Long customerId);
}