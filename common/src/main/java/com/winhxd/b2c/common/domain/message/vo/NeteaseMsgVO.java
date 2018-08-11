package com.winhxd.b2c.common.domain.message.vo;

import com.winhxd.b2c.common.domain.common.BaseCondition;
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
public class NeteaseMsgVO extends BaseCondition {

    @ApiModelProperty("消息主键")
    private Long msgid;
    @ApiModelProperty("消息内容")
    private String message;
    @ApiModelProperty("页面类型")
    private String pagetype;
    @ApiModelProperty("点击事件")
    private String treecode;
    @ApiModelProperty("消息发送时间")
    private String alerttime;
    @ApiModelProperty("语音文件路径")
    private String audioURL;
    @ApiModelProperty("是否转换为语音进行播放。1：转语音;0:不转语音。")
    private String transferaudio;
}
