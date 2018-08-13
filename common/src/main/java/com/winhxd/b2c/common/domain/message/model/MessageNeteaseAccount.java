package com.winhxd.b2c.common.domain.message.model;

import lombok.Data;

import java.util.Date;

/**
 * @author jujinbiao
 */
@Data
public class MessageNeteaseAccount {
    private Long id;
    /**
     * B端用户主键
     */
    private Long customerId;
    /**
     * 网易云通信ID
     */
    private String accid;
    /**
     * 网易云通信昵称
     */
    private String name;
    /**
     * 扩展字段json格式
     */
    private String props;
    /**
     * 头像URL
     */
    private String icon;
    /**
     * 用户密码
     */
    private String token;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 创建时间
     */
    private Date created;
}