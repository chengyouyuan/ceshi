package com.winhxd.b2c.order.dao;

import com.winhxd.b2c.order.model.HxdEvent;
import org.apache.ibatis.annotations.Param;

public interface HxdEventMapper {
    int deleteByPrimaryKey(@Param("eventName") String eventName, @Param("eventKey") String eventKey);

    int insert(HxdEvent record);

    int insertSelective(HxdEvent record);

    HxdEvent selectByPrimaryKey(@Param("eventName") String eventName, @Param("eventKey") String eventKey);

    int updateByPrimaryKeySelective(HxdEvent record);

    int updateByPrimaryKey(HxdEvent record);
}