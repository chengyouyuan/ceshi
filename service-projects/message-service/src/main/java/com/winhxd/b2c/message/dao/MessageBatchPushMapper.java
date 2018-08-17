package com.winhxd.b2c.message.dao;

import com.winhxd.b2c.common.domain.message.condition.MessageBatchPushCondition;
import com.winhxd.b2c.common.domain.message.model.MessageBatchPush;
import com.winhxd.b2c.common.domain.message.vo.MessageBatchPushVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MessageBatchPushMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MessageBatchPush record);

    int insertSelective(MessageBatchPush record);

    MessageBatchPush selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MessageBatchPush record);

    int updateByPrimaryKey(MessageBatchPush record);

    List<MessageBatchPushVO> selectMessageBatchPush(MessageBatchPushCondition condition);
}