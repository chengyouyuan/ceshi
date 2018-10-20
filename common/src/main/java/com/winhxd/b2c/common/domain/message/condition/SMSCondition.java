package com.winhxd.b2c.common.domain.message.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jujinbiao
 * @className SMSCondition
 * @description 发送短信condition
 */
@Data
public class SMSCondition extends ApiCondition {
    @ApiModelProperty("发送短信手机号")
    private String mobile;

    @ApiModelProperty("发送短信内容")
    private String content;

    @ApiModelProperty("发送短信手机号（惠下单短信服务）")
    private String telePhoneNo;

    @ApiModelProperty("发送短信类型，验证码和通知类型全部用“1”")
    private String type = "1";

}
