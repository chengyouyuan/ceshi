package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.model.CouponActivityTemplate;

import java.util.List;

public interface CouponActivityTemplateMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponActivityTemplate record);

    int insertSelective(CouponActivityTemplate record);

    CouponActivityTemplate selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponActivityTemplate record);

    int updateByPrimaryKey(CouponActivityTemplate record);

    /**
     * 多条件查询
     * @param couponActivityTemplate
     * @return
     */
    List<CouponActivityTemplate> selectByExample(CouponActivityTemplate couponActivityTemplate);

    List<CouponActivityTemplate> selectByActivityId(Long id);

    /**
     * 更新status状态
     * @param couponActivityTemplate
     * @return
     */
    int updateByCouponActivityId(CouponActivityTemplate couponActivityTemplate);

}