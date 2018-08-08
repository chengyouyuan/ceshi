package com.winhxd.b2c.store.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.Page;
import com.winhxd.b2c.common.domain.store.condition.StoreSubmitProductCondition;
import com.winhxd.b2c.common.domain.store.model.StoreSubmitProduct;
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
    
	Page<StoreSubmitProductVO> selectVoByCondition(@Param("condition")StoreSubmitProductCondition condition);
}