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

    @ApiModelProperty("页面类型/消息跳转类型：参考MsgPageTypeEnum中的pageType;" +
            "--跳转到订单详情页面,treeCode传订单号;" +
            "--消息通知，无跳转，treeCode不用传,")
    private short pageType;

    @ApiModelProperty("treeCode")
    private String treeCode;

    @ApiModelProperty("消息类别，参考MsgCategoryEnum中的typeCode")
    private short msgCategory;

    @ApiModelProperty("expiration秒后过期")
    private int expiration;

    @ApiModelProperty("根据订单状态，播放手机端语音文件，0：不播放，1：用户下单，2：用户发起退款")
    private int audioType;

    @ApiModelProperty("是否需要语音。1：转语音;0:不转语音。;")
    private int transferAudio;

    @ApiModelProperty("0：在消息盒子中;1:不在消息盒子中;")
    private int msgType;

    @ApiModelProperty("创建人")
    private String createdBy;
}
