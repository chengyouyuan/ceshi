package com.winhxd.b2c.store.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.Page;
import com.winhxd.b2c.common.domain.store.condition.StoreProductManageCondition;
import com.winhxd.b2c.common.domain.store.model.StoreProductManage;
import com.winhxd.b2c.common.domain.store.vo.StoreProdSimpleVO;

/**
 * @description:
 * @author: lvsen
 * @date: 2018/8/6 14:30
 */
@Repository
public interface StoreProductManageMapper {
    int deleteByPrimaryKey(Long id);

    int insert(StoreProductManage record);

    int insertSelective(StoreProductManage record);

    StoreProductManage selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StoreProductManage record);

    int updateByPrimaryKey(StoreProductManage record);

    /**
     * 目前支持价格跟创建时间排序，是否推荐，门店id,状态
     *
     * @param condition
     * @return List<String>
     * @Title: selectPutawaySkusByConditon
     * @Description: 通过门店商品管理condition获取sku集合(带排序)
     * @author wuyuanbao
     * @date 2018年8月4日下午3:34:29
     */
    List<String> selectSkusByConditon(@Param("condition") StoreProductManageCondition condition);
    
    /**
     * 查询门店对应的sku信息
    * @Title: selectPutawayProdBySkuCodes 
    * @Description: TODO 
    * @param storeId
    * @param skuCodes
    * @return List<StoreProductManage>
    * @author wuyuanbao
    * @date 2018年8月6日下午8:05:24
     */
    List<StoreProductManage> selectPutawayProdBySkuCodes(@Param("storeId")Long storeId, @Param("skuCodes")String...skuCodes);
    
    /**
     * 统计sku数量通过condition
    * @Title: countSkusByConditon 
    * @Description: TODO 
    * @param condition
    * @return int
    * @author wuyuanbao
    * @date 2018年8月6日下午8:16:11
     */
    int countSkusByConditon(@Param("condition") StoreProductManageCondition condition);
    /**
     * 通过storeId跟skucode查询StoreProductManage
    * @Title: selectBySkuCodeAndStoreId 
    * @Description: TODO 
    * @param storeId
    * @param skuCode
    * @return StoreProductManage
    * @author wuyuanbao
    * @date 2018年8月8日下午3:43:07
     */
    StoreProductManage selectBySkuCodeAndStoreId(@Param("storeId")Long storeId,@Param("skuCode")String skuCode);
    /**
     * 分页获取
    * @Title: selectVoByCondition 
    * @Description: TODO 
    * @param condition
    * @return Page<StoreProdSimpleVO>
    * @author wuyuanbao
    * @date 2018年8月8日下午8:42:09
     */
    Page<StoreProdSimpleVO> selectVoByCondition(@Param("condition")StoreProductManageCondition condition);
}