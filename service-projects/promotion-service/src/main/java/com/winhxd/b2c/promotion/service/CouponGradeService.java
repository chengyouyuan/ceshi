package com.winhxd.b2c.promotion.service;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponGradeCondition;
import com.winhxd.b2c.common.domain.promotion.condition.RuleRealationCountCondition;
import com.winhxd.b2c.common.domain.promotion.vo.CouponGradeVO;
import com.winhxd.b2c.common.domain.promotion.vo.GradeTempleteCountVO;

/**
 * @Author wl
 * @Date 2018/8/8 18:30
 * @Description
 **/
public interface CouponGradeService {
    /**
     *
     *@Deccription 查看详情
     *@Params  id
     *@Return  ResponseResult<CouponGradeVO>
     *@User  wl
     *@Date   2018/8/25 10:23
     */
    CouponGradeVO viewCouponGradeDetail(long id);
    /**
     *
     *@Deccription 设置无效
     *@Params  id userId userName
     *@Return
     *@User  wl
     *@Date   2018/8/25 10:24
     */
    int updateCouponGradeValid(long id,long userId,String userName);
    /**
     *
     *@Deccription  添加坎级规则
     *@Params  couponGradeCondition
     *@Return  插入数量
     *@User  wl
     *@Date   2018/8/25 10:25
     */
    int addCouponGrade(CouponGradeCondition couponGradeCondition);
    /**
     *
     *@Deccription 分页查询坎级规则
     *@Params  condition
     *@Return  PagedList<CouponGradeVO>
     *@User  wl
     *@Date   2018/8/25 10:26
     */
    PagedList<CouponGradeVO> getCouponGradePage(CouponGradeCondition condition);
    /**
     *
     *@Deccription 分页查询坎级规则关联模板数
     *@Params  condition
     *@Return  PagedList<CouponGradeVO>
     *@User  wl
     *@Date   2018/8/25 10:26
     */
    PagedList<GradeTempleteCountVO> findGradeTempleteCountPage(RuleRealationCountCondition condition);
}
