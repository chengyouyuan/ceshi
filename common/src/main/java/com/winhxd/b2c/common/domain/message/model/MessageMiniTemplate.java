package com.winhxd.b2c.common.domain.message.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @author jujinbiao
 * @className MessageMiniTemplate 小程序消息模板
 * @description
 */
@Data
public class MessageMiniTemplate {
    @ApiModelProperty("接收者（用户）的 openid")
    private String touser;
    @ApiModelProperty("所需下发的模板消息的id")
    private String template_id;
    @ApiModelProperty("点击模板卡片后的跳转页面，不填则模板无跳转。")
    private String page;
    private String form_id;
    @ApiModelProperty("模板内容，不填则下发空模板。 数组元素的顺序，要求和小程序的模板库中的对应模板的参数顺序一致。")
    private Map<String,Object> data;
    @ApiModelProperty("模板需要放大的关键词，不填则默认无放大")
    private String emphasis_keyword;
}
