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
    int deleteByPrimaryKey(Long id);

    int insert(StoreSubmitProduct record);

    int insertSelective(StoreSubmitProduct record);

    StoreSubmitProduct selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StoreSubmitProduct record);

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