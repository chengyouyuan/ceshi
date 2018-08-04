package com.winhxd.b2c.order.service;

import java.util.Date;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.order.condition.OrderListCondition;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO;
import com.winhxd.b2c.common.domain.order.vo.StoreOrderSalesSummaryVO;

/**
 * 订单查询接口,提供订单查询方法
 *
 * @author wangbin
 * @date 2018年8月2日 下午5:50:07
 */
public interface OrderQueryService {
    /**
     * 根据用户ID查询所有订单
     *
     * @param condition
     * @return
     */
    PagedList<OrderInfoDetailVO> findOrderByCustomerId(OrderListCondition condition);
    
    /**
     * @author wangbin
     * @date  2018年8月4日 上午10:51:59
     * @Description 
     * @param storeId
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    StoreOrderSalesSummaryVO getStoreOrderSalesSummary(long storeId, Date startDateTime, Date endDateTime);
}
