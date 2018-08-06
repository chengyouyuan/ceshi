package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.model.CouponTemplateSend;

public interface CouponTemplateSendMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponTemplateSend record);

    int insertSelective(CouponTemplateSend record);

    CouponTemplateSend selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponTemplateSend record);

    int updateByPrimaryKey(CouponTemplateSend record);
}