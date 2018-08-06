package com.winhxd.b2c.message.dao;

import com.winhxd.b2c.common.domain.message.model.MessageWechatHistory;

public interface MessageWechatHistoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MessageWechatHistory record);

    int insertSelective(MessageWechatHistory record);

    MessageWechatHistory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MessageWechatHistory record);

    int updateByPrimaryKey(MessageWechatHistory record);
}