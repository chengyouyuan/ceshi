package com.winhxd.b2c.order.service;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.order.condition.OrderListCondition;
import com.winhxd.b2c.common.domain.order.model.OrderInfo;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO;

import java.util.List;

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
}
