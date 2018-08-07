package com.winhxd.b2c.common.domain.message.model;

import lombok.Data;

import java.util.Date;

/**
 * @author jujinbiao
 */
@Data
public class MessageWechatHistory {
    private Long id;
    /**
     * 接收者（用户）的 openid
     */
    private String toUser;
    /**
     * 所需下发的模板消息的id
     */
    private String templateId;
    /**
     * 消息模板标题
     */
    private String title;
    /**
     * 点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转。
     */
    private String page;
    /**
     * 表单提交场景下，为 submit 事件带上的 formId；支付场景下，为本次支付的 prepay_id
     */
    private String formId;
    /**
     * 模板内容，不填则下发空模板
     */
    private String data;
    /**
     * 发送时间
     */
    private Date sendTime;
}