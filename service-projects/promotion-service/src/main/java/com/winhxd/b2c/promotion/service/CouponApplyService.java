package com.winhxd.b2c.promotion.service;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponApplyCondition;
import com.winhxd.b2c.common.domain.promotion.condition.RuleRealationCountCondition;
import com.winhxd.b2c.common.domain.promotion.vo.ApplyTempleteCountVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponApplyVO;

/**
 * @Author wl
 * @Date 2018/8/9 12:14
 * @Description
 **/
public interface CouponApplyService {
    /**
     *
     *@Deccription 查看适用对象详情
     *@Params   id  type
     *@Return CouponApplyVO
     *@User  wl
     *@Date   2018/8/25 10:36
     */
    CouponApplyVO viewCouponApplyDetail(String id,Short type);
    /**
     *
     *@Deccription 适用对象设置无效
     *@Params  id, userId, userName
     *@Return
     *@User  wl
     *@Date   2018/8/25 10:43
     */
    int updateCouponApplyToValid(Long id, Long userId, String userName);
    /**
     *
     *@Deccription 适用对象分页查询
     *@Params  condition
     *@Return  PagedList<CouponApplyVO>
     *@User  wl
     *@Date   2018/8/25 10:44
     */
    PagedList<CouponApplyVO> findCouponApplyPage(CouponApplyCondition condition);

    /**
     *
     *@Deccription 添加适用对象
     *@Params
     *@Return
     *@User  wl
     *@Date   2018/8/25 10:44
     */
    int addCouponApply(CouponApplyCondition condition);

    PagedList<ApplyTempleteCountVO> findApplyTempleteCountPage(RuleRealationCountCondition condition);
}
