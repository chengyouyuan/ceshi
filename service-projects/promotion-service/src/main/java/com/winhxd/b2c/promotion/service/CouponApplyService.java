package com.winhxd.b2c.promotion.service;

import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.vo.CouponApplyVO;

/**
 * @Author wl
 * @Date 2018/8/9 12:14
 * @Description
 **/
public interface CouponApplyService {
    ResponseResult<CouponApplyVO> viewCouponApplyDetail(long id);

    int updateCouponApplyToValid(long id, long userId, String userName);
}
