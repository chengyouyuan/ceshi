package com.winhxd.b2c.common.domain.message.model;

import lombok.Data;

import java.util.Date;

/**
 * @author jujinbiao
 */
@Data
public class MessageNeteaseHistory {
    private Long id;
    /**
     * 消息发送者的云信账号
     */
    private String fromAccid;
    /**
     * 消息接受者的云信账号
     */
    private String toAccid;
    /**
     * 消息类型 0-TEXT/1-PICTURE/2-AUDIO/4-ADDRESS/100-CUSTOM
     */
    private Short msgType;
    /**
     * 消息内容
     */
    private String msgBody;
    /**
     * 消息附加内容
     */
    private String msgAttach;
    /**
     * 消息扩展
     */
    private String extJson;
    /**
     * 页面类型
     */
    private Short pageType;
    /**
     * 消息跳转类型
     */
    private String treeCode;
    /**
     * 消息发送时间
     */
    private Date msgTimeStamp;
    /**
     * 云信端消息ID
     */
    private String msgIdServer;
}