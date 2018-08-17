package com.winhxd.b2c.common.domain.message.model;

import lombok.Data;

import java.util.Date;

/**
 * MessageBatchPush 后台管理-消息管理-手动推送消息（给所有门店推送云信消息）
 * @author jujinbiao
 */
@Data
public class MessageBatchPush {
    private Long id;
    /**
     * 消息内容
     */
    private String msgContent;
    /**
     * 定时推送
     */
    private Date timingPush;
    /**
     * 创建时间
     */
    private Date created;
    /**
     * 创建人
     */
    private Long createdBy;
    /**
     * 创建人姓名
     */
    private String createdByName;
    /**
     * 最后推送时间
     */
    private Date lastPushTime;
    /**
     * 修改时间
     */
    private Date updated;
    /**
     * 修改人
     */
    private Long updatedBy;
    /**
     * 修改人姓名
     */
    private String updatedByName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public Date getTimingPush() {
        return timingPush;
    }

    public void setTimingPush(Date timingPush) {
        this.timingPush = timingPush;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public Date getLastPushTime() {
        return lastPushTime;
    }

    public void setLastPushTime(Date lastPushTime) {
        this.lastPushTime = lastPushTime;
    }
}