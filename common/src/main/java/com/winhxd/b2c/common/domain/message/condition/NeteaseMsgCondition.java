package com.winhxd.b2c.common.domain.message.condition;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jujinbiao
 * @className NeteaseMsgCondition
 * @description
 */
@Data
public class NeteaseMsgCondition {
    @ApiModelProperty("单个B端用户id")
    private Long  customerId;

    @ApiModelProperty("云信消息")
    private NeteaseMsg neteaseMsg;
}
