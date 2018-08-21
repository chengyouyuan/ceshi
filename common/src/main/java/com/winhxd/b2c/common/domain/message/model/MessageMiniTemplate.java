package com.winhxd.b2c.common.domain.message.model;

import lombok.Data;

import java.util.Map;

/**
 * @author jujinbiao
 * @className MessageMiniTemplate 小程序消息模板
 * @description
 */
@Data
public class MessageMiniTemplate {
    private String touser;
    private String template_id;
    private String page;
    private String form_id;
    private Map<String,Object> data;
    private String emphasis_keyword;
}
