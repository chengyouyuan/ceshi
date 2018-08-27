package com.winhxd.b2c.promotion.service;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponInvestorCondition;
import com.winhxd.b2c.common.domain.promotion.condition.RuleRealationCountCondition;
import com.winhxd.b2c.common.domain.promotion.model.CouponInvestor;
import com.winhxd.b2c.common.domain.promotion.vo.CouponInvestorVO;
import com.winhxd.b2c.common.domain.promotion.vo.InvertorTempleteCountVO;

/**
 * @Author wl
 * @Date 2018/8/7 13:08
 * @Description
 **/
public interface CouponInvestorService {

    /**
     *
     *@Deccription 新建出资方
     *@Params
     *@Return
     *@User  wl
     *@Date   2018/8/25 10:59
     */
    int saveCouponInvestor(CouponInvestorCondition condition);

    /**
     *
     *@Deccription 查看出资发详情
     *@Params  id
     *@Return
     *@User  wl
     *@Date   2018/8/25 10:58
     */
    CouponInvestorVO getCouponInvestorDetailById(String id);

    /**
     *
     *@Deccription 出资方设置无效
     *@Params   id,userId,userName
     *@Return
     *@User  wl
     *@Date   2018/8/25 10:59
     */
    int updateCouponInvestorToValid(Long id,Long userId,String userName);

   /**
    *
    *@Deccription  出资方分页查询
    *@Params
    *@Return
    *@User  wl
    *@Date   2018/8/25 10:56
    */
    PagedList<CouponInvestorVO> getCouponInvestorPage(CouponInvestorCondition condition);

    /**
     *
     *@Deccription 关联模板数量列表
     *@Params condition
     *@Return PagedList<InvertorTempleteCountVO>
     *@User  wl
     *@Date   2018/8/25 10:53
     */
    PagedList<InvertorTempleteCountVO> findInvertorTempleteCountPage(RuleRealationCountCondition condition);
}
