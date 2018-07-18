package com.winhxd.b2c.order.dao;

import com.winhxd.b2c.order.model.HxdEventConsumed;
import org.apache.ibatis.annotations.Param;

public interface HxdEventConsumedMapper {
    int deleteByPrimaryKey(@Param("eventName") String eventName, @Param("eventKey") String eventKey, @Param("eventConsumerGroupId") String eventConsumerGroupId);

    int insert(HxdEventConsumed record);

    int insertSelective(HxdEventConsumed record);

    HxdEventConsumed selectByPrimaryKey(@Param("eventName") String eventName, @Param("eventKey") String eventKey, @Param("eventConsumerGroupId") String eventConsumerGroupId);

    int updateByPrimaryKeySelective(HxdEventConsumed record);

    int updateByPrimaryKey(HxdEventConsumed record);
}