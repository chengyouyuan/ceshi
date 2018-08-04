package com.winhxd.b2c.order.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
     * 获取门店销售汇总信息
     * @author wangbin
     * @date  2018年8月4日 上午11:13:37
     * @param storeId
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    StoreOrderSalesSummaryVO getStoreOrderTurnover(long storeId, Date startDateTime, Date endDateTime);
    
    /**
     * @author wangbin
     * @date  2018年8月4日 上午11:53:22
     * @Description 
     * @param storeId
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    Integer getStoreOrderCustomerNum(long storeId, Date startDateTime, Date endDateTime);
}