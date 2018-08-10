package com.winhxd.b2c.message.dao;

import com.winhxd.b2c.common.domain.message.model.MessageNeteaseHistory;
import org.springframework.stereotype.Service;

@Service
public interface MessageNeteaseHistoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MessageNeteaseHistory record);

    int insertSelective(MessageNeteaseHistory record);

    MessageNeteaseHistory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MessageNeteaseHistory record);

    int updateByPrimaryKey(MessageNeteaseHistory record);
}