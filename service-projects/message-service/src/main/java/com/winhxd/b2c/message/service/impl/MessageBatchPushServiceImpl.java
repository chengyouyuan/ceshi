package com.winhxd.b2c.message.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.message.condition.MessageBatchPushCondition;
import com.winhxd.b2c.common.domain.message.model.MessageBatchPush;
import com.winhxd.b2c.common.domain.message.model.MessageNeteaseAccount;
import com.winhxd.b2c.common.domain.message.model.MessageNeteaseHistory;
import com.winhxd.b2c.common.domain.message.vo.MessageBatchPushVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.message.dao.MessageBatchPushMapper;
import com.winhxd.b2c.message.dao.MessageNeteaseAccountMapper;
import com.winhxd.b2c.message.dao.MessageNeteaseHistoryMapper;
import com.winhxd.b2c.message.service.MessageBatchPushService;
import com.winhxd.b2c.message.utils.NeteaseUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author jujinbiao
 * @className MessageBatchPushServiceImpl
 * @description
 */
@Service
public class MessageBatchPushServiceImpl implements MessageBatchPushService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageBatchPushServiceImpl.class);
    private static final String SUCCESS_CODE = "200";
    private static final String PARAM_CODE = "code";
    private static final String PARAM_DESC = "desc";

    @Autowired
    MessageBatchPushMapper messageBatchPushMapper;

    @Autowired
    MessageNeteaseAccountMapper messageNeteaseAccountMapper;

    @Autowired
    MessageNeteaseHistoryMapper messageNeteaseHistoryMapper;

    @Autowired
    NeteaseUtils neteaseUtils;

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
        MessageBatchPush messageBatchPush = messageBatchPushMapper.selectByPrimaryKey(id);
        if (messageBatchPush == null){
            LOGGER.error("MessageBatchPushServiceImpl ->batchPushMessage，手动给用户推送消息出错，messageBatchPush推送配置不存在。");
            throw new BusinessException(BusinessCode.CODE_1001);
        }
        List<MessageNeteaseAccount> messageNeteaseAccounts = messageNeteaseAccountMapper.selectAll();
        if (CollectionUtils.isEmpty(messageNeteaseAccounts)){
            LOGGER.error("MessageBatchPushServiceImpl ->batchPushMessage，手动给用户推送消息出错，没有云信用户记录。");
            throw new BusinessException(BusinessCode.CODE_1001);
        }
        List<String> accids = new ArrayList<>();
        for (MessageNeteaseAccount account: messageNeteaseAccounts) {
            accids.add(account.getAccid());
        }
        String[] accidsArr = accids.toArray(new String[accids.size()]);
        Map<String, Object> msgMap = neteaseUtils.sendTxtMessage2Batch(accidsArr,messageBatchPush.getMsgContent());
        if (SUCCESS_CODE.equals(String.valueOf(msgMap.get(PARAM_CODE)))) {
            //云信消息发送成功
            saveNeteaseMsgHistory(accids, messageBatchPush.getMsgContent());
        } else {
            LOGGER.error("MessageBatchPushServiceImpl ->batchPushMessage,给B端用户手动推送云信消息出错，错误码={}", String.valueOf(msgMap.get(PARAM_CODE)));
        }
    }

    private void saveNeteaseMsgHistory(List<String> accids, String msgContent) {
        List<MessageNeteaseHistory> list = new ArrayList<>();
        for (String accid: accids) {
            MessageNeteaseHistory history = new MessageNeteaseHistory();
            history.setFromAccid("admin");
            history.setToAccid(accid);
            history.setMsgType(Short.valueOf("0"));
            history.setMsgBody(msgContent);
            history.setMsgTimeStamp(new Date());
            list.add(history);
        }
        messageNeteaseHistoryMapper.insertHistories(list);
    }

}
