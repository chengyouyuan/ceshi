package com.winhxd.b2c.system.model;

import java.util.Date;

public class DataOperateEvent {

    private Long id;
    private String eventType;
    private String eventDesc;
    private String operBy;
    private Date operTime;
    private String relTable;
    private String relId;
    private String originalValue;
    private String originalValueDesc;
    private String oldValue;
    private String oldValueDesc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public String getOperBy() {
        return operBy;
    }

    public void setOperBy(String operBy) {
        this.operBy = operBy;
    }

    public Date getOperTime() {
        return operTime;
    }

    public void setOperTime(Date operTime) {
        this.operTime = operTime;
    }

    public String getRelTable() {
        return relTable;
    }

    public void setRelTable(String relTable) {
        this.relTable = relTable;
    }

    public String getRelId() {
        return relId;
    }

    public void setRelId(String relId) {
        this.relId = relId;
    }

    public String getOriginalValue() {
        return originalValue;
    }

    public void setOriginalValue(String originalValue) {
        this.originalValue = originalValue;
    }

    public String getOriginalValueDesc() {
        return originalValueDesc;
    }

    public void setOriginalValueDesc(String originalValueDesc) {
        this.originalValueDesc = originalValueDesc;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getOldValueDesc() {
        return oldValueDesc;
    }

    public void setOldValueDesc(String oldValueDesc) {
        this.oldValueDesc = oldValueDesc;
    }
}
