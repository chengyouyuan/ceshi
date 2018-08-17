package com.winhxd.b2c.message.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.message.condition.MessageBatchPushCondition;
import com.winhxd.b2c.common.domain.message.model.MessageBatchPush;
import com.winhxd.b2c.common.domain.message.vo.MessageBatchPushVO;
import com.winhxd.b2c.message.dao.MessageBatchPushMapper;
import com.winhxd.b2c.message.service.MessageBatchPushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jujinbiao
 * @className MessageBatchPushServiceImpl
 * @description
 */
@Service
public class MessageBatchPushServiceImpl implements MessageBatchPushService {

    @Autowired
    MessageBatchPushMapper messageBatchPushMapper;

    @Override
    public PagedList<MessageBatchPushVO> findMessageBatchPushPageInfo(MessageBatchPushCondition condition) {
        PagedList<MessageBatchPushVO> pagedList = new PagedList<>();
        PageHelper.startPage(condition.getPageNo(), condition.getPageSize());
        List<MessageBatchPushVO> messageBatchPushVOS = messageBatchPushMapper.selectMessageBatchPush(condition);
        PageInfo<MessageBatchPushVO> pageInfo = new PageInfo<>(messageBatchPushVOS);
        pagedList.setData(pageInfo.getList());
        pagedList.setPageNo(pageInfo.getPageNum());
        pagedList.setPageSize(pageInfo.getPageSize());
        pagedList.setTotalRows(pageInfo.getTotal());
        return pagedList;
    }

    @Override
    public int addBatchPush(MessageBatchPush messageBatchPush) {
       return messageBatchPushMapper.insertSelective(messageBatchPush);
    }

    @Override
    public int modifyBatchPush(MessageBatchPush messageBatchPush) {
        return messageBatchPushMapper.updateByPrimaryKeySelective(messageBatchPush);
    }

    @Override
    public MessageBatchPush getBatchPush(Long id) {
        return messageBatchPushMapper.selectByPrimaryKey(id);
    }

    @Override
    public int removeBatchPush(Long id) {
        return messageBatchPushMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void batchPushMessage(Long id) {

    }

}
