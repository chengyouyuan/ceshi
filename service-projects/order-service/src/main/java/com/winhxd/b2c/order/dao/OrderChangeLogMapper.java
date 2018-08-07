package com.winhxd.b2c.order.dao;

import java.util.List;

import com.winhxd.b2c.common.domain.order.vo.OrderChangeVO;
import com.winhxd.b2c.order.model.OrderChangeLog;

public interface OrderChangeLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderChangeLog record);

    int insertSelective(OrderChangeLog record);

    OrderChangeLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderChangeLog record);

    int updateByPrimaryKeyWithBLOBs(OrderChangeLog record);

    int updateByPrimaryKey(OrderChangeLog record);

    List<OrderChangeVO> listOrderChanges(String orderNo);
}