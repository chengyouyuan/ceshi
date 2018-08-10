package com.winhxd.b2c.promotion.service;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponGradeCondition;
import com.winhxd.b2c.common.domain.promotion.vo.CouponGradeVO;
import com.winhxd.b2c.common.domain.promotion.vo.GradeTempleteCountVO;

/**
 * @Author liangliang
 * @Date 2018/8/8 18:30
 * @Description
 **/
public interface CouponGradeService {
    ResponseResult<CouponGradeVO> viewCouponGradeDetail(long id);
    int updateCouponGradeValid(long id,long userId,String userName);
    int addCouponGrade(CouponGradeCondition couponGradeCondition);
    ResponseResult<PagedList<CouponGradeVO>> getCouponGradePage(CouponGradeCondition condition);
    ResponseResult<PagedList<GradeTempleteCountVO>> findGradeTempleteCountPage(String gradeId, Integer pageNo, Integer pageSize);
}
