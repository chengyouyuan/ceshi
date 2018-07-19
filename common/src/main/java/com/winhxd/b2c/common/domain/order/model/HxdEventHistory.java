package com.winhxd.b2c.common.domain.order.model;

import java.util.Date;

public class HxdEventHistory {
    private String eventName;

    private String eventKey;

    private Long id;

    private Date eventTime;

    private String eventData;

    private Date eventConsumeTime;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventData() {
        return eventData;
    }

    public void setEventData(String eventData) {
        this.eventData = eventData;
    }

    public Date getEventConsumeTime() {
        return eventConsumeTime;
    }

    public void setEventConsumeTime(Date eventConsumeTime) {
        this.eventConsumeTime = eventConsumeTime;
    }
}