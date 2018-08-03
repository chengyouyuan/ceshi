package com.winhxd.b2c.order.dao;

import java.util.List;

import com.winhxd.b2c.common.domain.order.model.OrderItem;

public interface OrderItemMapper {
    int insert(OrderItem record);
    
    int insertItems(List<OrderItem> items);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);
}