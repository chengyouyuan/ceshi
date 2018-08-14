package com.winhxd.b2c.message.service;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.message.condition.NeteaseAccountCondition;
import com.winhxd.b2c.common.domain.message.condition.NeteaseMsgBoxCondition;
import com.winhxd.b2c.common.domain.message.condition.NeteaseMsgCondition;
import com.winhxd.b2c.common.domain.message.vo.NeteaseAccountVO;
import com.winhxd.b2c.common.domain.message.vo.NeteaseMsgVO;

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

    /**
     * 获取云信消息盒子
     * @param neteaseMsgBoxCondition
     * @return
     */
    PagedList<NeteaseMsgVO> getNeteaseMsgBox(NeteaseMsgBoxCondition neteaseMsgBoxCondition, Long customerId);
}
