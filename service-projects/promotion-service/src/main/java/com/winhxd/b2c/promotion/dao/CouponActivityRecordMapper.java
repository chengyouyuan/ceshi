package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.model.CouponActivityRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CouponActivityRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponActivityRecord record);

    int insertSelective(CouponActivityRecord record);

    CouponActivityRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponActivityRecord record);

    int updateByPrimaryKey(CouponActivityRecord record);

    /**
     * 根据条件查询
     * @param example
     * @return
     */
    List<CouponActivityRecord> selectByExample(CouponActivityRecord example);

    /**
     * 根据活动查看领取记录
     * @param activityId
     * @return
     */
    List<CouponActivityRecord> selectRecordByActivityId(Long activityId);

    /**
     * 验证门店下的用户是否推券
     * @param customerId
     * @param storeId
     * @param activityId
     * @return
     */
    CouponActivityRecord checkStoreUserIsPushCoupon(@Param("customerId") Long customerId, @Param("storeId") Long storeId,@Param("activityId") Long activityId);

    /**
     * 检查用户是否参加过活动
     * @param record
     * @return
     */
    Integer checkCustomerJoinActive(CouponActivityRecord record);
}