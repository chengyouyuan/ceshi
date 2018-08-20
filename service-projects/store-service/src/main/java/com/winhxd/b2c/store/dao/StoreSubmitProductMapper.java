package com.winhxd.b2c.store.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.Page;
import com.winhxd.b2c.common.domain.store.condition.BackStageStoreSubmitProdCondition;
import com.winhxd.b2c.common.domain.store.condition.StoreSubmitProductCondition;
import com.winhxd.b2c.common.domain.store.model.StoreSubmitProduct;
import com.winhxd.b2c.common.domain.store.vo.BackStageStoreSubmitProdVO;
import com.winhxd.b2c.common.domain.store.vo.StoreSubmitProductVO;



/**
 * @description:
 * @author: lvsen
 * @date: 2018/8/6 14:37
 */
@Repository
public interface StoreSubmitProductMapper {
    /**
     * 物理删除
    * @Title: deleteByPrimaryKey 
    * @Description: TODO 
    * @param id
    * @return int
    * @author wuyuanbao
    * @date 2018年8月20日上午10:08:38
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入
    * @Title: insert 
    * @Description: TODO 
    * @param record
    * @return int
    * @author wuyuanbao
    * @date 2018年8月20日上午10:08:47
     */
    int insert(StoreSubmitProduct record);

    /**
     * 插入
    * @Title: insertSelective 
    * @Description: TODO 
    * @param record
    * @return int
    * @author wuyuanbao
    * @date 2018年8月20日上午10:09:03
     */
    int insertSelective(StoreSubmitProduct record);

    /**
     * 根据id值获取数据
    * @Title: selectByPrimaryKey 
    * @Description: TODO 
    * @param id
    * @return StoreSubmitProduct
    * @author wuyuanbao
    * @date 2018年8月20日上午10:09:10
     */
    StoreSubmitProduct selectByPrimaryKey(Long id);

    /**
     * 更新数据
    * @Title: updateByPrimaryKeySelective 
    * @Description: TODO 
    * @param record
    * @return int
    * @author wuyuanbao
    * @date 2018年8月20日上午10:09:19
     */
    int updateByPrimaryKeySelective(StoreSubmitProduct record);

    /**
     * 更新数据
    * @Title: updateByPrimaryKey 
    * @Description: TODO 
    * @param record
    * @return int
    * @author wuyuanbao
    * @date 2018年8月20日上午10:09:28
     */
    int updateByPrimaryKey(StoreSubmitProduct record);
    /**
     * 分页查询
    * @Title: selectVoByCondition 
    * @Description: TODO 
    * @param condition
    * @return Page<StoreSubmitProductVO>
    * @author wuyuanbao
    * @date 2018年8月8日下午8:59:06
     */
	Page<StoreSubmitProductVO> selectVoByCondition(@Param("condition")StoreSubmitProductCondition condition);
	
	/**
	 * 后台分页查询
	* @Title: selectBackStageVOByCondition 
	* @Description: TODO 
	* @param condition
	* @return Page<BackStageStoreSubmitProdVO>
	* @author wuyuanbao
	* @date 2018年8月14日下午4:17:19
	 */
	Page<BackStageStoreSubmitProdVO> selectBackStageVOByCondition(@Param("condition")BackStageStoreSubmitProdCondition condition);
}