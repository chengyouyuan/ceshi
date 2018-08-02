package com.winhxd.b2c.order.dao;

import com.github.pagehelper.Page;
import com.winhxd.b2c.common.domain.order.model.AsyncEvent;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

public interface AsyncEventMapper {
    int deleteByPrimaryKey(@Param("eventName") String eventName, @Param("eventKey") String eventKey);

    int insert(AsyncEvent record);

    int insertSelective(AsyncEvent record);

    AsyncEvent selectByPrimaryKey(@Param("eventName") String eventName, @Param("eventKey") String eventKey);

    int updateByPrimaryKeySelective(AsyncEvent record);

    int updateByPrimaryKeyWithBLOBs(AsyncEvent record);

    int updateByPrimaryKey(AsyncEvent record);

    @Select("select * from async_event")
    @ResultMap("ResultMapWithBLOBs")
    Page<AsyncEvent> findAll();
}