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
     * 页面类型/消息跳转类型：
     * 1--跳转至APP内部某个页面。通过393的treecode跳转
     * 3--跳转到订单详情页面
     * 4--后台下载，此时跳转页面为：downUrl下载地址
     * 5--后台上报数据 0--跳转至在线网页。此时跳转页面为网页链接
     */
    private Short pageType;
    /**
     * treecode
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
    /**
     * 消息是否已读
     */
    private String readStatus;
}