package com.winhxd.b2c.message.service;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.message.condition.MessageBatchPushCondition;
import com.winhxd.b2c.common.domain.message.model.MessageBatchPush;
import com.winhxd.b2c.common.domain.message.vo.MessageBatchPushVO;

public interface MessageBatchPushService {
    /**
     * 后台消息管理，查询手动推送消息列表信息
     * @param condition
     * @return
     */
    PagedList<MessageBatchPushVO> findMessageBatchPushPageInfo(MessageBatchPushCondition condition);

    /**
     * 后台消息管理，新增手动推送消息列表
     * @param messageBatchPush
     * @return
     */
    Long addBatchPush(MessageBatchPush messageBatchPush);

    /**
     * 后台消息管理，修改手动推送消息列表
     * @param messageBatchPush
     * @return
     */
    int modifyBatchPush(MessageBatchPush messageBatchPush);

    /**
     * 后台消息管理，获取手动推送信息
     * @param id
     * @return
     */
    MessageBatchPush getBatchPush(Long id);

    /**
     * 后台消息管理，删除手动推送信息
     * @param id
     * @return
     */
    int removeBatchPush(Long id);

    /**
     * 后台消息管理，手动推送信息
     * @param id
     * @return
     */
    void batchPushMessage(Long id);
}
