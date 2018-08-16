package com.winhxd.b2c.common.domain.message.vo;

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

    @ApiModelProperty("消息类型 0-TEXT/1-PICTURE/2-AUDIO/4-ADDRESS/100-CUSTOM")
    private Integer msgType;

    @ApiModelProperty("消息内容")
    private String message;

    @ApiModelProperty("页面类型 1-新订单 2-已完成 3-已退款 4-已取消 5-申请退款 10-提现申请 11-提现成功 12-提现失败")
    private Short pageType;

    @ApiModelProperty("消息类型简述 新订单 已完成 已退款 已取消 申请退款 提现申请 提现成功 提现失败")
    private String pageTypeSummary;

    @ApiModelProperty("消息类型描述 新 完 退 取 申 提 现 败")
    private String pageTypeDesc;

    @ApiModelProperty("点击事件 订单号等信息")
    private String treeCode;

    @ApiModelProperty("消息发送时间")
    private String alertTime;
    
    @ApiModelProperty("已读状态 0-已读 1-未读")
    private Short readStatus;

    @ApiModelProperty("消息 0-今日消息 1-历史消息")
    private Short timeType;
}
