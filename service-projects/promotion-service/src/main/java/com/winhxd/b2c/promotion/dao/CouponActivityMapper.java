package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.condition.CouponActivityCondition;
import com.winhxd.b2c.common.domain.promotion.model.CouponActivity;
import com.winhxd.b2c.common.domain.promotion.vo.CouponActivityStoreVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponActivityVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CouponActivityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponActivity record);

    int insertSelective(CouponActivity record);

    CouponActivity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponActivity record);

    int updateByPrimaryKey(CouponActivity record);

    /**
     * 根据条件查询
     * @param example
     * @return
     */
    List<CouponActivity> selectByExample(CouponActivity example);

    /**
     * 多条件查询领券列表
     * @param condition
     * @return
     */
    List<CouponActivityVO> selectCouponActivity(@Param("condition") CouponActivityCondition condition);

    /**
     * 查询优惠券列表
     * @param customerId
     * @param couponType
     * @return
     */
    List<CouponVO> selectCouponList(@Param("customerId")Long customerId,@Param("couponType")Integer couponType);

    /**
     * 查询该门店下可领取的优惠券
     * @param storeId
     * @return
     */
    List<CouponVO> selectUnclaimedCouponList(Long storeId);

    /**
     * 根据活动获取优惠券列表
     * @param condition
     * @return
     */
    List<CouponActivityStoreVO> selectCouponByActivity(@Param("condition") CouponActivityCondition condition);

    /**
     * 根据活动获取小店信息
     * @param condition
     * @return
     */
    List<CouponActivityStoreVO> selectStoreByActivity(@Param("condition") CouponActivityCondition condition);

    /**
     * 用户查询门店优惠券列表
     * @return
     */
    List<CouponVO> selectStoreCouponList(Long storeId);
}