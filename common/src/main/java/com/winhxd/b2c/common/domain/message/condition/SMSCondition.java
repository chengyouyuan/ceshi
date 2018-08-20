package com.winhxd.b2c.common.domain.message.condition;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jujinbiao
 * @className SMSCondition
 * @description 发送短信condition
 */
@Data
public class SMSCondition {
    @ApiModelProperty("发送短信手机号")
    private String mobile;

    @ApiModelProperty("发送短信内容")
    private String content;
}
