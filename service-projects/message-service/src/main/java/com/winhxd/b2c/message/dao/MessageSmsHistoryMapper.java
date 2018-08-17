package com.winhxd.b2c.message.dao;


import com.winhxd.b2c.common.domain.message.model.MessageSmsHistory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MessageSmsHistoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MessageSmsHistory record);

    int insertSelective(MessageSmsHistory record);

    int insertBatch(List<MessageSmsHistory> record);

    MessageSmsHistory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MessageSmsHistory record);

    int updateByPrimaryKeyWithBLOBs(MessageSmsHistory record);

    int updateByPrimaryKey(MessageSmsHistory record);
}