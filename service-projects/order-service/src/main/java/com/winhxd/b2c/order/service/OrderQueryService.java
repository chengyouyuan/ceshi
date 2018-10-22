package com.winhxd.b2c.order.service;

import java.util.Date;
import java.util.List;

import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.context.StoreUser;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.order.condition.AllOrderQueryByCustomerCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderInfoQuery4ManagementCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderQuery4StoreCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderQueryByCustomerCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderQueryByStoreCondition;
import com.winhxd.b2c.common.domain.order.vo.OrderCountByStatus4StoreVO;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO4Management;
import com.winhxd.b2c.common.domain.order.vo.StoreOrderSalesSummaryVO;
import com.winhxd.b2c.common.domain.pay.vo.OrderPayVO;

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
    PagedList<OrderInfoDetailVO> findOrderListByCustomerId(CustomerUser customer,AllOrderQueryByCustomerCondition condition);

    /**
     * 根据用户ID查询所有订单
     *
     * @author pangjianhua
     * @param condition 入参
     * @return
     */
    OrderInfoDetailVO findOrderByCustomerId(CustomerUser customer,OrderQueryByCustomerCondition condition);

    /**
     * B端用户查订单详情
     * @date  2018年8月23日 下午1:17:38
     * @param orderQueryByCustomerCondition
     * @return
     */
    OrderInfoDetailVO findOrderForStore(StoreUser store, OrderQueryByStoreCondition orderQueryByCustomerCondition);

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

    /**
     * 根据门店Id获取各状态订单数量
     * @author wangbin
     * @date  2018年8月9日 上午11:27:52
     * @param storeCustomerId
     * @return
     */
    OrderCountByStatus4StoreVO getOrderCountByStatus(Long storeCustomerId);

    /**
     * 无分页订单信息查询
     * @author wangbin
     * @date  2018年8月13日 上午10:27:11
     * @param infoQuery4ManagementCondition
     * @return
     */
    List<OrderInfoDetailVO> listOrder4ManagementWithNoPage(
            OrderInfoQuery4ManagementCondition infoQuery4ManagementCondition);


    /**
     * 获取用户订单支付信息
     * @author wangbin
     * @date  2018年8月16日 上午11:07:37
     * @param orderNo
     * @param spbillCreateIp 终端IP
     * @param deviceInfo 设备号
     * @param customerId
     * @param openid
     * @return
     */
    OrderPayVO getOrderPayInfo(String orderNo, String spbillCreateIp, String deviceInfo, Long customerId,
            String openid);

    /**
     * 获取门店当天订单销售数据
     * @author wangbin
     * @date  2018年8月23日 下午1:18:08
     * @Description 
     * @param storeId
     * @return
     */
    StoreOrderSalesSummaryVO getStoreIntradayOrderSalesSummary(long storeId);

    /**
     * 获取门店30天销量数据
     * @author wangbin
     * @date  2018年8月23日 下午1:32:48
     * @param storeId
     * @return
     */
    StoreOrderSalesSummaryVO getStoreMonthOrderSalesSummary(long storeId);

    /**
     * 查询指定时间段数据
     * @author wangbin
     * @date  2018年8月27日 下午4:14:35
     * @Description 
     * @param storeId
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    StoreOrderSalesSummaryVO calculateStoreOrderSalesSummary(long storeId, Date startDateTime, Date endDateTime);
}
