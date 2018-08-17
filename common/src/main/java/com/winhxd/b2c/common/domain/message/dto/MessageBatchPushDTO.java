package com.winhxd.b2c.common.domain.message.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author jujinbiao
 * @className MessageBatchPushDTO
 * @description
 */
@ApiModel("消息管理-手动推送消息编辑传输对象")
@Data
public class MessageBatchPushDTO {
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "消息内容")
    private String msgContent;

    @ApiModelProperty(value = "定时推送")
    private Date timingPush;
}
