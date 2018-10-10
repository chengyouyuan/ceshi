package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.model.CouponTemplateUse;

import java.util.List;

public interface CouponTemplateUseMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponTemplateUse record);

    int insertSelective(CouponTemplateUse record);

    CouponTemplateUse selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponTemplateUse record);

    int updateByPrimaryKey(CouponTemplateUse record);

    /**
     * 根据订单号查询使用的优惠券
     * @param orderNo
     * @return
     */
    List<CouponTemplateUse> selectByOrderNo(String orderNo);

    /**
     * 根据订单号查询使用的优惠券
     * @param orderNos
     * @return
     */
    List<CouponTemplateUse> selectByOrderNos(List<String> orderNos);

    /**
     * 根据订单查询活动状态
     * @param record
     * @return
     */
    Integer  selectActiveStatusByOrderNo(CouponTemplateUse record);
}