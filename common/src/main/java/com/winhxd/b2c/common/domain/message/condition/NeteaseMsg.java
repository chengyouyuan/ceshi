package com.winhxd.b2c.common.domain.message.condition;

import lombok.Data;

import java.util.List;

/**
 * @author jujinbiao
 * @className NeteaseMsg
 * @description
 */
@Data
public class NeteaseMsg {
    /**
     * 消息内容
     */
    private String msgContent;
    /**
     * 页面类型
     */
    private int pageType;
    /**
     * 消息跳转类型：需和手机端商定
     */
    private String treeCode;
    /**
     * expiration秒后过期
     */
    private int expiration;
    /**
     * 根据订单状态，播放手机端语音文件，0：不播放，1：用户下单，2：用户发起退款
     */
    private int audioType;
    /**
     * 0：在消息盒子中;1:不在消息盒子中;
     */
    private int msgType;
    /**
     * 创建人
     */
    private String createdBy;
}