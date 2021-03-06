package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.condition.CouponActivityAddCondition;
import com.winhxd.b2c.common.domain.promotion.condition.CouponActivityCondition;
import com.winhxd.b2c.common.domain.promotion.model.CouponActivity;
import com.winhxd.b2c.common.domain.promotion.vo.CouponActivityStoreVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponActivityVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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
    List<CouponVO> selectCouponList(@Param("customerId")Long customerId,@Param("couponType")Integer couponType,@Param("status")String status);

    /**
     * 查询优惠券列表
     * @param customerId
     * @param couponType
     * @return
     */
    List<CouponVO> selectNewUserCouponList(@Param("customerId")Long customerId,@Param("couponType")Short couponType);

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

    /**
     * 判断活动时间是否冲突。
     * @param condition
     * @return
     */
    Integer getActivityDateClash(@Param("condition") CouponActivityAddCondition condition);

    List<CouponVO> selectCouponListGroup(@Param("customerId")Long customerId,@Param("couponType")Integer couponType,@Param("status")String status);
    
    List<Map<String, Object>> selectNums(@Param("status")String status,@Param("activityIds")List<Long> activityIds);
}