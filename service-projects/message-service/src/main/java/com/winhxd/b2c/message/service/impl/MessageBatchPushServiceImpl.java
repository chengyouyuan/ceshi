package com.winhxd.b2c.message.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.message.condition.MessageBatchPushCondition;
import com.winhxd.b2c.common.domain.message.condition.NeteaseMsgDelayCondition;
import com.winhxd.b2c.common.domain.message.model.MessageBatchPush;
import com.winhxd.b2c.common.domain.message.model.MessageNeteaseAccount;
import com.winhxd.b2c.common.domain.message.model.MessageNeteaseHistory;
import com.winhxd.b2c.common.domain.message.vo.MessageBatchPushVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.mq.MQHandler;
import com.winhxd.b2c.common.mq.StringMessageListener;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.common.util.MessageSendUtils;
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

    @Autowired
    MessageBatchPushMapper messageBatchPushMapper;

    @Autowired
    MessageNeteaseAccountMapper messageNeteaseAccountMapper;

    @Autowired
    MessageNeteaseHistoryMapper messageNeteaseHistoryMapper;

    @Autowired
    NeteaseUtils neteaseUtils;

    @Autowired
    MessageSendUtils messageSendUtils;

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
        LOGGER.info("MessageBatchPushServiceImpl ->batchPushMessage，手动给所有门店推送云信消息，开始...消息配置id={}",id);
        //获取推送配置信息
        MessageBatchPush messageBatchPush = messageBatchPushMapper.selectByPrimaryKey(id);
        if (messageBatchPush == null){
            LOGGER.error("MessageBatchPushServiceImpl ->batchPushMessage，手动给门店推送消息出错，messageBatchPush推送配置不存在。");
            throw new BusinessException(BusinessCode.CODE_703501);
        }
        //获取所有门店云信账号
        List<MessageNeteaseAccount> messageNeteaseAccounts = messageNeteaseAccountMapper.selectAll();
        if (CollectionUtils.isEmpty(messageNeteaseAccounts)){
            LOGGER.error("MessageBatchPushServiceImpl ->batchPushMessage，手动给门店推送消息出错，没有云信门店记录。");
            throw new BusinessException(BusinessCode.CODE_703502);
        }
        List<String> accids = new ArrayList<>();
        for (MessageNeteaseAccount account: messageNeteaseAccounts) {
            accids.add(account.getAccid());
        }
        //发送延迟消息
        Date timingPush = messageBatchPush.getTimingPush();
        int delayMilli = 0;
        if (timingPush.compareTo(new Date()) > 0){
            delayMilli = (int)(timingPush.getTime() - System.currentTimeMillis());
        }
        NeteaseMsgDelayCondition neteaseMsgDelayCondition = new NeteaseMsgDelayCondition();
        neteaseMsgDelayCondition.setAccids(accids);
        neteaseMsgDelayCondition.setMsgContent(messageBatchPush.getMsgContent());
        messageSendUtils.sendNeteaseMsgDelay(neteaseMsgDelayCondition,delayMilli);
        messageBatchPush.setLastPushTime(new Date());
        messageBatchPushMapper.updateByPrimaryKeySelective(messageBatchPush);
        LOGGER.info("MessageBatchPushServiceImpl ->batchPushMessage，手动给门店推送云信消息，结束...消息配置id={}",id);
    }

    @StringMessageListener(MQHandler.NETEASE_MESSAGE_DELAY_HANDLER)
    public void batchSendNeteaseMsg(String neteaseMsgDelayConditionJson){
        LOGGER.info("消息服务->批量发送云信消息，MessageBatchPushServiceImpl.batchSendNeteaseMsg(),neteaseMsgDelayConditionJson={}",neteaseMsgDelayConditionJson);
        NeteaseMsgDelayCondition neteaseMsgDelayCondition = JsonUtil.parseJSONObject(neteaseMsgDelayConditionJson,NeteaseMsgDelayCondition.class);
        List<String> accids = neteaseMsgDelayCondition.getAccids();
        String msgContent = neteaseMsgDelayCondition.getMsgContent();
        String[] accidsArr = accids.toArray(new String[accids.size()]);
        //给所有门店批量推送云信消息
        Map<String, Object> msgMap = neteaseUtils.sendTxtMessage2Batch(accidsArr,msgContent);
        if (SUCCESS_CODE.equals(String.valueOf(msgMap.get(PARAM_CODE)))) {
            //云信消息发送成功
            saveNeteaseMsgHistory(accids, msgContent);
        } else {
            LOGGER.error("MessageBatchPushServiceImpl ->batchPushMessage,给B端门店手动推送云信消息出错，错误码={}", String.valueOf(msgMap.get(PARAM_CODE)));
            throw new BusinessException(BusinessCode.CODE_703503);
        }
    }

    /**
     * 保存云信消息推送记录
     * @param accids
     * @param msgContent
     */
    private void saveNeteaseMsgHistory(List<String> accids, String msgContent) {
        List<MessageNeteaseHistory> list = new ArrayList<>();
        for (String accid: accids) {
            MessageNeteaseHistory history = new MessageNeteaseHistory();
            history.setFromAccid("admin");
            history.setToAccid(accid);
            //消息类型0：text
            history.setMsgType(Short.valueOf("0"));
            history.setMsgBody(msgContent);
            history.setMsgTimeStamp(new Date());
            //页面跳转类型1：根据treecode跳转
            history.setPageType(Short.valueOf("1"));
            history.setTreeCode("treeCode");
            //1：未读
            history.setReadStatus("1");
            list.add(history);
        }
        messageNeteaseHistoryMapper.insertHistories(list);
    }
}
