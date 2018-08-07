package com.winhxd.b2c.message.service;

import com.winhxd.b2c.common.domain.message.condition.NeteaseAccountCondition;
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
}
