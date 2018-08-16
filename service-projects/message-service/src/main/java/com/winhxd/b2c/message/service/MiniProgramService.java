package com.winhxd.b2c.message.service;

import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.message.condition.MiniFormIdCondition;
import com.winhxd.b2c.common.domain.message.condition.MiniMsgCondition;
import com.winhxd.b2c.common.domain.message.model.MiniOpenId;

import java.util.List;

/**
 * @author jujinbiao
 * @className MiniProgramService 小程序接口
 * @description
 */
public interface MiniProgramService {
    /**
     * 根据code获取小程序的openid和session_key
     * @param code
     * @return
     */
    ResponseResult<MiniOpenId> getMiniOpenId(String code);

    /**
     * 给C端用户推送小程序模板消息
     * @param miniMsgCondition
     */
    ResponseResult<Void> sendMiniMsg(MiniMsgCondition miniMsgCondition);

    /**
     * 保存formid
     * @param formIds
     * @return
     */
    void saveFormIds(MiniFormIdCondition miniFormIdCondition);
}
