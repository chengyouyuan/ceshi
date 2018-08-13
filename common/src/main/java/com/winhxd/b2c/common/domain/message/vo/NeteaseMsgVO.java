package com.winhxd.b2c.common.domain.message.vo;

import com.winhxd.b2c.common.domain.common.PagedCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jujinbiao
 * @className NeteaseMsgVO
 * @description 云信用户消息
 */
@ApiModel("云信用户消息")
@Data
public class NeteaseMsgVO {

    @ApiModelProperty("消息主键")
    private Long msgId;
    @ApiModelProperty("消息内容")
    private String message;
    @ApiModelProperty("页面类型")
    private String pageType;
    @ApiModelProperty("点击事件")
    private String treeCode;
    @ApiModelProperty("消息发送时间")
    private String alertTime;
}
