package com.winhxd.b2c.message.service;

import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.message.condition.NeteaseAccountCondition;
import com.winhxd.b2c.common.domain.message.condition.NeteaseMsgCondition;
import com.winhxd.b2c.common.domain.message.vo.NeteaseAccountVO;

public interface NeteaseService {
    /**
     * 获取云信用户信息
     * @param neteaseAccountCondition
     * @return
     */
    NeteaseAccountVO getNeteaseAccountInfo(NeteaseAccountCondition neteaseAccountCondition);
    /**
     * 创建云信用户
     * @param neteaseAccountCondition
     * @return
     */
    NeteaseAccountVO createNeteaseAccount(NeteaseAccountCondition neteaseAccountCondition);

    /**
     * 给B端用户发送云信消息
     * @param neteaseMsgCondition
     */
    ResponseResult<Void> sendNeteaseMsg(NeteaseMsgCondition neteaseMsgCondition);
}
