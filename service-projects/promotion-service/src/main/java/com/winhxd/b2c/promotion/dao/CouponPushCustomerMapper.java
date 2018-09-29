package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.model.CouponPushCustomer;
import com.winhxd.b2c.common.domain.promotion.vo.CouponPushVO;

import java.util.List;

public interface CouponPushCustomerMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponPushCustomer record);

    int insertSelective(CouponPushCustomer record);

    CouponPushCustomer selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponPushCustomer record);

    int updateByPrimaryKey(CouponPushCustomer record);

    /**
     * 查询优惠券发放给多少用户
     * @param customerIds
     * @return
     */
    int selectCouponStoreUser(List<Long> customerIds);

    /**
     * 查询优惠券推送给用户列表
     * @param customerId
     * @return
     */
    List<CouponPushVO> selectCouponPushCustomer(Long customerId);

    /**
     * 查询优惠券推送给门店
     * @param storeId
     * @return
     */
    List<CouponPushVO> selectCouponPushStore(Long storeId);


    /**
     * 通过活动ID和模板ID查询优惠券已使用数量
     * @param couponPushVO
     * @return
     */
    Long countUsedCouponNum(CouponPushVO couponPushVO);


    List<CouponPushCustomer> getCouponPushCustomerByActiveId(Long activeId);

}