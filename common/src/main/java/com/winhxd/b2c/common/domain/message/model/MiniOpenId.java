package com.winhxd.b2c.common.domain.message.model;

import lombok.Data;

/**
 * @author jujinbiao
 * @className MiniOpenId 小程序登录返回的信息
 * @description
 */
@Data
public class MiniOpenId {
    /**
     * 用户唯一标识openid
     */
    private String openid;
    /**
     * 会话密钥session_key
     */
    private String sessionKey;
    /**
     * 用户在开放平台的唯一标识符
     */
    private String unionid;
}
