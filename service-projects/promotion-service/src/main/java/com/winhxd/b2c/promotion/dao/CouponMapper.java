package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.vo.CouponVO;
import org.apache.ibatis.annotations.Param;

/**
 * @Auther wangxiaoshun
 * @Date 2018/8/8 09 53
 * @Description
 */
public interface CouponMapper {
    /**
     * 获取某个优惠券领取的数量
     * @param activityId
     * @param templateId
     * @return
     */
    int getCouponNumByTemplateId(@Param("activityId") Long activityId,@Param("templateId") Long templateId);

    /**
     * 获取某个优惠券用户领取的数量
     * @param activityId
     * @param templateId
     * @param storeId
     * @param customerId
     * @return
     */
    int getCouponNumByCustomerId(@Param("activityId") Long activityId,@Param("templateId") Long templateId,@Param("storeId") Long storeId,@Param("customerId") Long customerId);

    /**
     * 获取某个优惠券门店领取的数量
     * @param activityId
     * @param templateId
     * @param storeId
     * @return
     */
    int getCouponNumByStoreId(@Param("activityId")Long activityId, @Param("templateId")Long templateId,@Param("storeId") Long storeId);
}