package com.winhxd.b2c.store.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.Page;
import com.winhxd.b2c.common.domain.store.condition.BackStageStoreProdCondition;
import com.winhxd.b2c.common.domain.store.condition.StoreProductManageCondition;
import com.winhxd.b2c.common.domain.store.model.StoreProductManage;
import com.winhxd.b2c.common.domain.store.vo.BackStageStoreProdVO;
import com.winhxd.b2c.common.domain.store.vo.StoreProdSimpleVO;

/**
 * @description:
 * @author: lvsen
 * @date: 2018/8/6 14:30
 */
@Repository
public interface StoreProductManageMapper {
    /**
     * 物理删除
    * @Title: deleteByPrimaryKey 
    * @Description: TODO 
    * @param id
    * @return int
    * @author wuyuanbao
    * @date 2018年8月20日上午10:05:22
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入一条数据
    * @Title: insert 
    * @Description: TODO 
    * @param record
    * @return int
    * @author wuyuanbao
    * @date 2018年8月20日上午10:05:31
     */
    int insert(StoreProductManage record);

    /**
     * 插入一条数据
    * @Title: insertSelective 
    * @Description: TODO 
    * @param record
    * @return int
    * @author wuyuanbao
    * @date 2018年8月20日上午10:05:49
     */
    int insertSelective(StoreProductManage record);

    /**
     * 根据id查询StoreProductManage
    * @Title: selectByPrimaryKey 
    * @Description: TODO 
    * @param id
    * @return StoreProductManage
    * @author wuyuanbao
    * @date 2018年8月20日上午10:06:01
     */
    StoreProductManage selectByPrimaryKey(Long id);

    /**
     * 更新数据
    * @Title: updateByPrimaryKeySelective 
    * @Description: TODO 
    * @param record
    * @return int
    * @author wuyuanbao
    * @date 2018年8月20日上午10:06:15
     */
    int updateByPrimaryKeySelective(StoreProductManage record);

    /**
     * 更新数据
    * @Title: updateByPrimaryKey 
    * @Description: TODO 
    * @param record
    * @return int
    * @author wuyuanbao
    * @date 2018年8月20日上午10:06:23
     */
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
    List<String> countSkusByConditon(@Param("condition") StoreProductManageCondition condition);
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


    /**
     * 查询商品集合
     * @param storeProductManageCondition
     * @return
     */
    List<StoreProductManage> selectProductBySelective(@Param("condition") StoreProductManageCondition storeProductManageCondition);
    
    /**
     * 查询返回后台VO
    * @Title: selectBackStageVoByCondition 
    * @Description: TODO 
    * @param condition
    * @return Page<BackStageStoreProdVO>
    * @author wuyuanbao
    * @date 2018年8月13日下午1:51:42
     */
    Page<BackStageStoreProdVO> selectBackStageVoByCondition(@Param("condition")BackStageStoreProdCondition condition);
}