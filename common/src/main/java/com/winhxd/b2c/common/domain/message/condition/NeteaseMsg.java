package com.winhxd.b2c.common.domain.message.condition;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jujinbiao
 * @className NeteaseMsg
 * @description
 */
@Data
public class NeteaseMsg {
    @ApiModelProperty("消息内容")
    private String msgContent;

    @ApiModelProperty("页面类型/消息跳转类型：" +
            "1--跳转至APP内部某个页面。通过393的treecode跳转;" +
            "3--跳转到订单详情页面;" +
            "4--后台下载，此时跳转页面为：downUrl下载地址;" +
            "5--后台上报数据 0--跳转至在线网页。此时跳转页面为网页链接")
    private short pageType;

    @ApiModelProperty("treeCode")
    private String treeCode;

    @ApiModelProperty("expiration秒后过期")
    private int expiration;

    @ApiModelProperty("根据订单状态，播放手机端语音文件，0：不播放，1：用户下单，2：用户发起退款")
    private int audioType;

    @ApiModelProperty("0：在消息盒子中;1:不在消息盒子中;")
    private int msgType;

    @ApiModelProperty("创建人")
    private String createdBy;
}
