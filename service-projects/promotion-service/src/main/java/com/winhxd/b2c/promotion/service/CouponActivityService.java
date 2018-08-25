package com.winhxd.b2c.promotion.service;

import com.winhxd.b2c.common.domain.promotion.condition.CouponActivityAddCondition;
import com.winhxd.b2c.common.domain.promotion.condition.CouponActivityCondition;
import com.winhxd.b2c.common.domain.promotion.vo.CouponActivityStoreVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponActivityVO;

import java.util.List;

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
    List<CouponActivityVO> findCouponActivity(CouponActivityCondition condition);
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
    void deleteCouponActivity(CouponActivityCondition condition);

    /**
     *
     *@Deccription 撤回活动优惠券
     *@Params  condition
     *@Return  ResponseResult
     *@User  sjx
     *@Date   2018/8/9
     */
    void revocationActivityCoupon(CouponActivityCondition condition);

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
    List<CouponActivityStoreVO> findCouponByActivity(CouponActivityCondition condition);

    /**
     *
     *@Deccription 根据活动获取小店信息
     *@Params  condition
     *@Return  ResponseResult
     *@User  sjx
     *@Date   2018/8/9
     */
    List<CouponActivityStoreVO> findStoreByActivity(CouponActivityCondition condition);

    /**
     *
     *@Deccription 判断活动时间是否冲突
     *@Params  condition
     *@Return  ResponseResult
     *@User  sjx
     *@Date   2018/8/21
     */
    Boolean getActivityDateClash(CouponActivityAddCondition condition);
}
