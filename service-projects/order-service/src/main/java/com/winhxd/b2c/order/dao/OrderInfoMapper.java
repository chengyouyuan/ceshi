package com.winhxd.b2c.order.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.order.condition.OrderInfoQuery4ManagementCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderQuery4StoreCondition;
import com.winhxd.b2c.common.domain.order.model.OrderInfo;
import com.winhxd.b2c.common.domain.order.vo.OrderCountByStatus4StoreVO;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO;
import com.winhxd.b2c.common.domain.order.vo.StoreOrderSalesSummaryVO;
import com.winhxd.b2c.order.support.annotation.OrderInfoConvertAnnotation;

/**
 * 订单主表
 *
 * @author pangjianhua
 * @date 2018/8/2 14:50
 */
public interface OrderInfoMapper {

    /**
     * 插入数据
     *
     * @param orderInfo 订单对象
     * @return 插入
     */
    int insert(OrderInfo orderInfo);

    /**
     * 插入数据
     *
     * @param orderInfo 订单对象
     * @return 插入
     */
    int insertSelective(OrderInfo orderInfo);

    /**
     * 根据订单编号获取订单对象
     *
     * @param id 订单ID
     * @return {@link OrderInfo}
     */
    OrderInfo selectByPrimaryKey(Long id);

    /**
     * 根据订单编号获取订单对象
     *
     * @param orderNo 订单编号
     * @return {@link OrderInfo}
     */
    OrderInfo selectByOrderNo(String orderNo);

    /**
     * 更新对象 订单号、customerId、storeId字段不能更新
     *
     * @param orderInfo 订单对象
     * @return 更新行数
     */
    int updateByPrimaryKeySelective(OrderInfo orderInfo);

    /**
     * 更新对象 订单号、customerId、storeId字段不能更新
     *
     * @param orderInfo 订单对象
     * @return 插入
     */
    int updateByPrimaryKey(OrderInfo orderInfo);

    /**
     * 获取用户的订单
     *
     * @param customerId 用户ID
     * @return {@link OrderInfo}
     */
    List<OrderInfo> selectOrderByCustomerId(Long customerId);

    /**
     * 获取用户的订单和订单商品内容
     *
     * @param customerId
     * @return
     */
    List<OrderInfoDetailVO> selectOrderInfoListByCustomerId(@Param("customerId") Long customerId);

    /**
     * 获取用户的订单和订单商品内容
     *
     * @param orderNo 订单编号
     * @return
     */
    @OrderInfoConvertAnnotation(queryCustomerInfo = true, queryStoreInfo = true)
    OrderInfoDetailVO selectOrderInfoByOrderNo(String orderNo);

    /**
     * 获取门店已支付订单销售汇总信息
     *
     * @param storeId
     * @param startDateTime
     * @param endDateTime
     * @return
     * @author wangbin
     * @date 2018年8月4日 上午11:13:37
     */
    StoreOrderSalesSummaryVO getStoreOrderTurnover(@Param("storeId") long storeId, @Param("startDateTime") Date startDateTime, @Param("endDateTime") Date endDateTime);

    /**
     * 获取门店已支付订单门店信息统计
     * @param storeId
     * @param startDateTime
     * @param endDateTime
     * @return
     * @author wangbin
     * @date 2018年8月4日 上午11:53:22
     * @Description
     */
    StoreOrderSalesSummaryVO getStoreOrderCustomerNum(@Param("storeId") long storeId, @Param("startDateTime") Date startDateTime, @Param("endDateTime") Date endDateTime);

    /**
     * 取消订单
     *
     * @param orderNo      订单编号
     * @param cancelReason 取消原因
     * @return 更新数量
     */
    int updateOrderStatusForCancel(@Param("orderNo") String orderNo, @Param("cancelReason") String cancelReason);

    /**
     * 查询门店下的提货码是否重复
     *
     * @param pickUpCodes 提货码列表
     * @param storeId     门店ID
     * @return true 有重复记录，false 不重复
     */
    boolean getPickUpCodeByStoreId(@Param("pickUpCodes") Set<String> pickUpCodes, @Param("storeId") long storeId);

    /**
     * 更新订单支付状态
     *
     * @param statusCode
     * @param paymentSerialNum
     * @param payFinishDateTime
     * @param orderId
     * @return 更新数量
     * @author wangbin
     * @date 2018年8月6日 上午11:12:13
     */
    int updateOrderPayStatus(@Param("payStatus") short payStatus, @Param("paymentSerialNum") String paymentSerialNum, @Param("payFinishDateTime") Date payFinishDateTime, @Param("orderId") long
            orderId);

    /**
     * 订单状态修改接口
     *
     * @param expectOrderStatus 期望更改时订单状态
     * @param newOrderStatus    需要更改的订单状态
     * @param orderId           订单Id
     * @return 更新数量
     * @author wangbin
     * @date 2018年8月6日 下午1:48:10
     * @Description
     */
    int updateOrderStatus(@Param("expectOrderStatus") Short expectOrderStatus, @Param("newOrderStatus") short newOrderStatus, @Param("orderId") Long orderId);

    /**
     * 查询管理平台订单列表
     *
     * @param condition
     * @return
     * @author wangbin
     * @date 2018年8月6日 下午3:29:26
     */
    List<Long> listOrder4Management(@Param("condition") OrderInfoQuery4ManagementCondition condition);

    /**
     * 查询订单列表
     *
     * @param orderIds
     * @return
     * @author wangbin
     * @date 2018年8月6日 下午3:29:26
     */
    List<OrderInfoDetailVO> listOrderInOrderIds(@Param("orderIds") List<Long> orderIds);

    /**
     * 查询门店订单列表
     *
     * @param orderIds
     * @return
     * @author wangbin
     * @date 2018年8月6日 下午3:29:26
     */
    List<OrderInfoDetailVO> listOrder4StoreInOrderIds(@Param("orderIds") List<Long> orderIds);

    /**
     * 更新订单提货码
     *
     * @param pickUpCode
     * @param id
     * @author wangbin
     * @date 2018年8月7日 下午1:21:55
     */
    int updateOrderPickupCode(@Param("pickUpCode") String pickUpCode, @Param("orderId") Long orderId);

    /**
     * 根据条件查询
     *
     * @param condition
     * @param storeId
     * @return
     * @author wangbin
     * @date 2018年8月7日 下午2:08:07
     */
    List<Long> listOrder4Store(@Param("condition") OrderQuery4StoreCondition condition, @Param("storeId") Long storeId);

    /**
     * C端申请退款更新状态
     *
     * @param orderNo
     * @param customerId
     */
    int updateOrderStatusForApplyRefund(@Param("orderNo") String orderNo, @Param("customerId") Long customerId, @Param("reason") String reason, @Param("orderStatus") short orderStatus);

    /**
     * 订单提货
     *
     * @param pickupCode
     * @param orderId
     * @param expectOrderStatus
     * @param newOrderStatus
     * @return
     * @author wangbin
     * @date 2018年8月8日 下午6:25:55
     * @Description
     */
    int orderPickup(@Param("pickupCode") String pickupCode, @Param("orderId") Long orderId, @Param("expectOrderStatus") Short expectOrderStatus, @Param("newOrderStatus") short newOrderStatus);

    /**
     * 订单各状态数量统计
     *
     * @param storeCustomerId
     * @return
     * @author wangbin
     * @date 2018年8月9日 下午2:02:13
     */
    OrderCountByStatus4StoreVO getOrderCountByStatus(Long storeCustomerId);

    /**
     * 更新订单总金额
     *
     * @param orderTotalMoney
     * @param realPayMoney
     * @param orderId
     * @return
     * @author wangbin
     * @date 2018年8月9日 下午4:00:33
     */
    int updateOrderMoney(@Param("orderTotalMoney") BigDecimal orderTotalMoney, @Param("realPayMoney") BigDecimal realPayMoney, @Param("orderId") Long orderId);

    /**
     * 更新订单状态为已退款状态
     *
     * @param orderNo
     * @return
     */
    int updateOrderStatusForRefundCallback(String orderNo);

    /**
     * 更新订单 接单时间
     *
     * @param date
     * @author wangbin
     * @date 2018年8月13日 下午7:59:27
     */
    void updateOrderConfirmDate(@Param("date") Date date, @Param("orderId") Long orderId);

    /**
     * 获取C端用户的订单和订单商品内容
     *
     * @param orderNo    订单编号
     * @param customerId 用户id
     * @return {@link OrderInfoDetailVO}
     */
    OrderInfoDetailVO selectOrderInfoByOrderNoAndCustomer(@Param("orderNo") String orderNo, @Param("customerId") Long customerId);

    /**
     * 获取B端门店的订单和订单商品内容
     *
     * @param orderNo 订单编号
     * @param storeId 门店id
     * @return {@link OrderInfoDetailVO}
     */
    OrderInfoDetailVO selectOrderInfoByOrderNoAndStore(@Param("orderNo") String orderNo, @Param("storeId") Long storeId);

    /**
     * 回退订单状态为待自提（门店不同意退款时使用）
     *
     * @param orderNo
     * @param storeId
     * @param reason
     * @return
     */
    int updateOrderStatusForReturnWaitSelfLifting(@Param("orderNo") String orderNo, @Param("storeId") Long storeId, @Param("reason") String reason);

    /**
     * 获取指定时间内 门店下单完成的用户id
     * @author wangbin
     * @date  2018年8月23日 下午3:33:22
     * @param storeId
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    List<Long> getStoreOrderDistinctCustomerIds(@Param("storeId")long storeId, @Param("startDateTime")Date startDateTime, @Param("endDateTime")Date endDateTime);

    /**
     * 获取门店完成订单销售信息
     * @author wangbin
     * @date  2018年8月24日 上午11:03:36
     * @param storeId
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    StoreOrderSalesSummaryVO getStoreCompletedOrderTurnover(@Param("storeId") long storeId, @Param("startDateTime") Date startDateTime, @Param("endDateTime") Date endDateTime);

    /**
     * 获取门店完成订单用户信息
     * @author wangbin
     * @date  2018年8月24日 上午11:03:50
     * @param storeId
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    StoreOrderSalesSummaryVO getStoreCompletedOrderCustomerNum(@Param("storeId") long storeId, @Param("startDateTime") Date startDateTime, @Param("endDateTime") Date endDateTime);
}