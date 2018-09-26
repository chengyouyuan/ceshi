package com.winhxd.b2c.message.service;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.message.condition.MessageNeteaseCondition;
import com.winhxd.b2c.common.domain.message.condition.NeteaseAccountCondition;
import com.winhxd.b2c.common.domain.message.condition.NeteaseMsgBoxCondition;
import com.winhxd.b2c.common.domain.message.condition.NeteaseMsgReadStatusCondition;
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
     * 更新云信用户信息
     * @param neteaseAccountCondition
     * @return
     */
    void updateNeteaseAccount(NeteaseAccountCondition neteaseAccountCondition);

    /**
     * 获取云信消息盒子
     * @param neteaseMsgBoxCondition
     * @param storeId
     * @return
     */
    PagedList<NeteaseMsgVO> findNeteaseMsgBox(NeteaseMsgBoxCondition neteaseMsgBoxCondition, Long storeId);

    /**
     * 修改云信消息已读状态
     * @param neteaseMsgReadStatusCondition
     * @return
     */
    Boolean modifyNeteaseMsgReadStatus(NeteaseMsgReadStatusCondition neteaseMsgReadStatusCondition, Long storeId);

    /**
     * 根据条件查询云信消息数量
     * @param messageNeteaseCondition
     * @return
     */
    Integer getNeteaseMessageCount(MessageNeteaseCondition messageNeteaseCondition);
}
