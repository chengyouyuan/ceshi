package com.winhxd.b2c.message.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.message.condition.MessageBatchPushCondition;
import com.winhxd.b2c.common.domain.message.condition.NeteaseMsgDelayCondition;
import com.winhxd.b2c.common.domain.message.model.MessageBatchPush;
import com.winhxd.b2c.common.domain.message.model.MessageNeteaseAccount;
import com.winhxd.b2c.common.domain.message.vo.MessageBatchPushVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.util.MessageSendUtils;
import com.winhxd.b2c.message.dao.MessageBatchPushMapper;
import com.winhxd.b2c.message.dao.MessageNeteaseAccountMapper;
import com.winhxd.b2c.message.dao.MessageNeteaseHistoryMapper;
import com.winhxd.b2c.message.service.MessageBatchPushService;
import com.winhxd.b2c.message.utils.NeteaseUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author jujinbiao
 * @className MessageBatchPushServiceImpl
 * @description
 */
@Service
public class MessageBatchPushServiceImpl implements MessageBatchPushService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageBatchPushServiceImpl.class);

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
    @Transactional(rollbackFor = Exception.class)
    public Long addBatchPush(MessageBatchPush messageBatchPush) {
        LOGGER.info("MessageBatchPushServiceImpl ->addBatchPush，手动给所有门店推送云信消息，开始...消息配置{}");
        //获取所有门店云信账号
        List<MessageNeteaseAccount> messageNeteaseAccounts = messageNeteaseAccountMapper.selectAll();
        if (CollectionUtils.isEmpty(messageNeteaseAccounts)){
            LOGGER.error("MessageBatchPushServiceImpl ->addBatchPush，手动给门店推送消息出错，没有云信门店记录。");
            throw new BusinessException(BusinessCode.CODE_703502);
        }
        List<String> accids = new ArrayList<>();
        for (MessageNeteaseAccount account: messageNeteaseAccounts) {
            accids.add(account.getAccid());
        }
        //发送消息参数
        NeteaseMsgDelayCondition neteaseMsgDelayCondition = new NeteaseMsgDelayCondition();
        neteaseMsgDelayCondition.setAccids(accids);
        neteaseMsgDelayCondition.setMsgContent(messageBatchPush.getMsgContent());
        //发送延迟消息
        Date timingPush = messageBatchPush.getTimingPush();
        if(timingPush == null){
            //立即发送
            messageSendUtils.sendNeteaseMsgBatch(neteaseMsgDelayCondition);
            messageBatchPush.setLastPushTime(new Date());
        }else{
            //延迟发送
            int delayMilli = 0;
            if (timingPush.compareTo(new Date()) > 0){
                delayMilli = (int)(timingPush.getTime() - System.currentTimeMillis());
            }
            messageSendUtils.sendNeteaseMsgDelay(neteaseMsgDelayCondition,delayMilli);
        }
        messageBatchPushMapper.insert(messageBatchPush);
        LOGGER.info("MessageBatchPushServiceImpl ->addBatchPush，手动给门店推送云信消息，结束...消息配置id={}",messageBatchPush.getId());
        return messageBatchPush.getId();
    }

    @Override
    public int modifyBatchPush(MessageBatchPush messageBatchPush) {
        //编辑方法一期暂时移除
        return 0;
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
