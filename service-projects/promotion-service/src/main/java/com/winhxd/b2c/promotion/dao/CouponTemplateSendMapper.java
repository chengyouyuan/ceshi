package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.condition.CouponCheckStatusCondition;
import com.winhxd.b2c.common.domain.promotion.model.CouponTemplateSend;
import org.apache.ibatis.annotations.Param;

public interface CouponTemplateSendMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponTemplateSend record);

    int insertSelective(CouponTemplateSend record);

    CouponTemplateSend selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponTemplateSend record);

    int updateByPrimaryKey(CouponTemplateSend record);

    int getCouponNumsByCustomerForStore(@Param("customerId") Long customerId);

    int checkCouponStatus(@Param("condition") CouponCheckStatusCondition condition);
}