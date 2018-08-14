package com.winhxd.b2c.common.domain.message.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;
import com.winhxd.b2c.common.domain.common.PagedCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jujinbiao
 * @className NeteaseHistoryCondition
 * @description 云信用户消息历史记录
 */
@ApiModel("云信用户消息历史记录")
@Data
public class NeteaseMsgBoxCondition extends PagedCondition {
    @ApiModelProperty("查询消息是当天还是历史，0：当天，1：历史")
    private Integer timeType;
}
