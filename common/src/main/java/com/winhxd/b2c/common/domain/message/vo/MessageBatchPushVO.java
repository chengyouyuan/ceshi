package com.winhxd.b2c.common.domain.message.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author jujinbiao
 * @Description: 系统后台手动推送消息列表页vo
 * @date 2018/8/17 10:50
 */
@Data
@ApiModel(value = "后台手动推送消息VO", description = "后台手动推送消息VO")
public class MessageBatchPushVO {
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("消息内容")
    private String msgContent;

    @ApiModelProperty("定时推送")
    private Date timingPush;

    @ApiModelProperty("创建时间")
    private Date created;

    @ApiModelProperty("创建人")
    private Long createdBy;

    @ApiModelProperty("创建人姓名")
    private String createdByName;

    @ApiModelProperty("最后推送时间")
    private Date lastPushTime;

    private static final long serialVersionUID = 1L;

}
