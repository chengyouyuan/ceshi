package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.promotion.condition.CouponActivityCondition;
import com.winhxd.b2c.common.domain.promotion.model.CouponActivity;
import com.winhxd.b2c.common.domain.promotion.vo.CouponActivityVO;

import java.util.List;

public interface CouponActivityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponActivity record);

    int insertSelective(CouponActivity record);

    CouponActivity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponActivity record);

    int updateByPrimaryKey(CouponActivity record);

    /**
     * 根据条件查询
     * @param example
     * @return
     */
    List<CouponActivity> selectByExample(CouponActivity example);

    /**
     * 多条件查询领券列表
     * @param condition
     * @return
     */
    PagedList<CouponActivityVO> queryCouponActivity(CouponActivityCondition condition);
}