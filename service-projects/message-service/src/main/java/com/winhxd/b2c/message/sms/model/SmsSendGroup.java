package com.winhxd.b2c.message.sms.model;

import com.winhxd.b2c.common.domain.message.model.MessageSmsHistory;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class SmsSendGroup implements Serializable {

    private static final long serialVersionUID = -8477119964769885933L;
    private String id;
    private String title;// 标题
    private String grp;
    private int sendType;        // 发送类型   1为同步发送，0为异步发送
    private Date scheduledDate;    // 异步发送时间
    private String createdBy;
    private Date created;
    private String updatedBy;
    private Date updated;
    private Set<MessageSmsHistory> smsSendSet = new HashSet<MessageSmsHistory>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGrp() {
        return grp;
    }

    public void setGrp(String grp) {
        this.grp = grp;
    }

    public int getSendType() {
        return sendType;
    }

    public void setSendType(int sendType) {
        this.sendType = sendType;
    }

    public Date getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(Date scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Set<MessageSmsHistory> getSmsSendSet() {
        return smsSendSet;
    }

    public void setSmsSendSet(Set<MessageSmsHistory> smsSendSet) {
        this.smsSendSet = smsSendSet;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
