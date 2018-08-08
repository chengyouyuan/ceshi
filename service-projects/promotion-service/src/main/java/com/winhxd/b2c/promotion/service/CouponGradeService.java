package com.winhxd.b2c.promotion.service;

import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.vo.CouponGradeVO;

/**
 * @Author liangliang
 * @Date 2018/8/8 18:30
 * @Description
 **/
public interface CouponGradeService {
    ResponseResult<CouponGradeVO> viewCouponGradeDetail(long id);
    int updateCouponGradeValid(long id,long userId,String userName);

}
