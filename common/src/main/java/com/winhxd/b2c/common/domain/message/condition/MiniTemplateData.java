package com.winhxd.b2c.common.domain.message.condition;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jujinbiao
 * @className MiniTemplateData
 * @description
 */
@Data
public class MiniTemplateData {
    @ApiModelProperty("组织小程序模板消息，data中的key名称，例如：keyword1、keyword2")
    private String keyName;
    @ApiModelProperty("组织小程序模板消息，data中的key对应的value")
    private String value;
}
