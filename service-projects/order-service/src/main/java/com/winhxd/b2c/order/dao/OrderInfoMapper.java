package com.winhxd.b2c.order.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.winhxd.b2c.common.domain.order.condition.OrderInfoQuery4ManagementCondition;
import com.winhxd.b2c.common.domain.order.model.OrderInfo;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO;
import com.winhxd.b2c.common.domain.order.vo.StoreOrderSalesSummaryVO;

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
    List<OrderInfoDetailVO> selectOrderInfoListByCustomerId(Long customerId);

    /**
     * 获取用户的订单和订单商品内容
     *
     * @param orderNo 订单编号
     * @return
     */
    OrderInfoDetailVO selectOrderInfoByOrderNo(String orderNo);
    
    /**
     * 获取门店销售汇总信息
     * @author wangbin
     * @date  2018年8月4日 上午11:13:37
     * @param storeId
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    StoreOrderSalesSummaryVO getStoreOrderTurnover(@Param("storeId")long storeId, @Param("startDateTime")Date startDateTime, @Param("endDateTime")Date endDateTime);
    
    /**
     * @author wangbin
     * @date  2018年8月4日 上午11:53:22
     * @Description 
     * @param storeId
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    Integer getStoreOrderCustomerNum(@Param("storeId")long storeId, @Param("startDateTime")Date startDateTime, @Param("endDateTime")Date endDateTime);

    /**
     * 取消订单
     *
     * @param orderNo    订单编号
     * @param cancelReason     取消原因
     * @return 更新数量
     */
    int updateOrderStatusForCancel(@Param("orderNo")String orderNo, @Param("cancelReason")String cancelReason);

    /**
     * 查询门店下的提货码是否重复
     * @param pickUpCodes 提货码列表
     * @param storeId 门店ID
     * @return true 有重复记录，false 不重复
     */
    boolean getPickUpCodeByStoreId(@Param("pickUpCodes")List<String> pickUpCodes, @Param("storeId")long storeId);

    /**
     * 更新订单支付状态
     * @author wangbin
     * @date  2018年8月6日 上午11:12:13
     * @param statusCode
     * @param payFinishDateTime
     * @param orderId
     * @return 更新数量
     */
    int updateOrderPayStatus(@Param("payStatus")short payStatus, @Param("payFinishDateTime")Date payFinishDateTime, @Param("orderId")long orderId);
    
    /**
     * 订单状态修改接口
     * @author wangbin
     * @date  2018年8月6日 下午1:48:10
     * @Description 
     * @param expectOrderStatus 期望更改时订单状态
     * @param newOrderStatus 需要更改的订单状态
     * @param orderId 订单Id
     * @return 更新数量
     */
    int updateOrderStatus(@Param("expectOrderStatus")Short expectOrderStatus, @Param("newOrderStatus")short newOrderStatus, @Param("orderId")Long orderId);

    /**
     * 查询管理平台订单列表
     * @author wangbin
     * @date  2018年8月6日 下午3:29:26
     * @param condition
     * @return
     */
    List<OrderInfoDetailVO> listOrder4Management(@Param("condition")OrderInfoQuery4ManagementCondition condition);
}