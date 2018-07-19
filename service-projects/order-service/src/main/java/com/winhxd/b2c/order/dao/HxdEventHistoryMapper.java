package com.winhxd.b2c.order.dao;

import com.github.pagehelper.Page;
import com.winhxd.b2c.common.domain.order.model.HxdEventHistory;
import org.apache.ibatis.annotations.Param;

public interface HxdEventHistoryMapper {
    int deleteByPrimaryKey(@Param("eventName") String eventName, @Param("eventKey") String eventKey);

    int insert(HxdEventHistory record);

    int insertSelective(HxdEventHistory record);

    HxdEventHistory selectByPrimaryKey(@Param("eventName") String eventName, @Param("eventKey") String eventKey);

    int updateByPrimaryKeySelective(HxdEventHistory record);

    int updateByPrimaryKey(HxdEventHistory record);

    Page<HxdEventHistory> findAll();
}