package com.winhxd.b2c.common.domain.message.condition;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author jujinbiao
 * @className NeteaseMsgDelayCondition
 * @description 后台消息管理，定时发送云信消息
 */
@Data
public class NeteaseMsgDelayCondition {
    @ApiModelProperty("云信用户账号id集合")
    private List<String> accids;
    @ApiModelProperty("消息内容")
    private String msgContent;
}
