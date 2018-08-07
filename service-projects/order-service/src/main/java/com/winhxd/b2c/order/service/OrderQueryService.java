package com.winhxd.b2c.order.service;

import java.util.Date;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.order.condition.AllOrderQueryByCustomerCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderInfoQuery4ManagementCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderQuery4StoreCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderQueryByCustomerCondition;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO4Management;
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
     * @author pangjianhua
     * @param condition 入参
     * @return
     */
    PagedList<OrderInfoDetailVO> findOrderListByCustomerId(AllOrderQueryByCustomerCondition condition);

    /**
     * 根据用户ID查询所有订单
     *
     * @author pangjianhua
     * @param condition 入参
     * @return
     */
    OrderInfoDetailVO findOrderByCustomerId(OrderQueryByCustomerCondition condition);

    /**
     * 获取门店销售数据
     * @author wangbin
     * @date  2018年8月4日 上午10:51:59
     * @param storeId
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    StoreOrderSalesSummaryVO getStoreOrderSalesSummary(long storeId, Date startDateTime, Date endDateTime);

    /**
     * 计算并缓存门店销售数据 
     * @author wangbin
     * @date  2018年8月4日 下午3:38:46
     * @param storeId
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    StoreOrderSalesSummaryVO calculateStoreOrderSalesSummaryAndSetCache(long storeId, Date startDateTime,
            Date endDateTime);
    /**
     * 根据门店ID获取门店提货码
     * @author pangjianhua
     * @param storeId 门店ID
     * @return
     */
    String getPickUpCode(long storeId);
    
    /**
     * 根据条件 查询订单列表，后台管理系统用
     *
     * @author wangbin
     * @param condition 入参
     * @return
     */
    PagedList<OrderInfoDetailVO> listOrder4Management(OrderInfoQuery4ManagementCondition condition);

    /**
     * 根据订单编号 查询订单详情
     * @author wangbin
     * @date  2018年8月6日 下午7:20:35
     * @param orderNo
     * @return
     */
    OrderInfoDetailVO4Management getOrderDetail4Management(String orderNo);

    /**
     * 门店订单列表查询
     * @author wangbin
     * @date  2018年8月7日 下午1:49:03
     * @param condition
     * @param storeId
     * @return
     */
    PagedList<OrderInfoDetailVO> listOrder4Store(OrderQuery4StoreCondition condition, Long storeId);
}
