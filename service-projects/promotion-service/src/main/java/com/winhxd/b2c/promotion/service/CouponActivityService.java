package com.winhxd.b2c.promotion.service;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponActivityAddCondition;
import com.winhxd.b2c.common.domain.promotion.condition.CouponActivityCondition;
import com.winhxd.b2c.common.domain.promotion.vo.CouponActivityStoreVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponActivityVO;

/**
 *
 * @author sjx
 * @date 2018/8/6
 */
public interface CouponActivityService {

    /**
     * @Deccription 查询优惠券领券推券列表
     * @param condition
     * @return CouponActivityVO
     */
    ResponseResult<PagedList<CouponActivityVO>> queryCouponActivity(CouponActivityCondition condition);
    /**
     *
     *@Deccription 添加优惠券活动
     *@Params  condition
     *@Return  ResponseResult
     *@User  sjx
     *@Date   2018/8/6
     */
    void saveCouponActivity(CouponActivityAddCondition condition);
    /**
     *
     *@Deccription 编辑优惠券活动
     *@Params  condition
     *@Return  ResponseResult
     *@User  sjx
     *@Date   2018/8/7
     */
    void updateCouponActivity(CouponActivityAddCondition condition);

    CouponActivityVO getCouponActivityById(String id);

    /**
     *
     *@Deccription 删除优惠券活动
     *@Params  id
     *@Return  ResponseResult
     *@User  sjx
     *@Date   2018/8/8
     */
    void deleteCouponActivity(String id);

    /**
     *
     *@Deccription 撤回活动优惠券
     *@Params  id
     *@Return  ResponseResult
     *@User  sjx
     *@Date   2018/8/9
     */
    void revocationActivityCoupon(String id);

    /**
     *
     *@Deccription 停用/开启活动
     *@Params  condition
     *@Return  ResponseResult
     *@User  sjx
     *@Date   2018/8/9
     */
    void updateCouponActivityStatus(CouponActivityAddCondition condition);

    /**
     *
     *@Deccription 根据活动获取优惠券列表
     *@Params  id
     *@Return  ResponseResult
     *@User  sjx
     *@Date   2018/8/9
     */
    ResponseResult<PagedList<CouponActivityStoreVO>> queryCouponByActivity(CouponActivityCondition condition);

    /**
     *
     *@Deccription 根据活动获取小店信息
     *@Params  condition
     *@Return  ResponseResult
     *@User  sjx
     *@Date   2018/8/9
     */
    ResponseResult<PagedList<CouponActivityStoreVO>> queryStoreByActivity(CouponActivityCondition condition);
}
