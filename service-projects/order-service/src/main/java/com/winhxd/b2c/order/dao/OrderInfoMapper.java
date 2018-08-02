package com.winhxd.b2c.order.dao;

import com.winhxd.b2c.common.domain.order.model.OrderInfo;

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
}