package com.winhxd.b2c.order.model;

import java.util.Date;

/**
 * 订单流转记录实体
 * @author wangbin
 * @date  2018年8月2日 下午1:43:58
 * @version 
 */
public class OrderChangeLog {
    
    public OrderChangeLog() {
        super();
    }

    public OrderChangeLog(String orderNo, Short originalOrderStatus, Short newOrderStatus, String changeMsg,
            Short mainPoint, Date created, String createdBy, String originalJson, String newJson) {
        super();
        this.orderNo = orderNo;
        this.originalOrderStatus = originalOrderStatus;
        this.newOrderStatus = newOrderStatus;
        this.changeMsg = changeMsg;
        this.mainPoint = mainPoint;
        this.created = created;
        this.createdBy = createdBy;
        this.originalJson = originalJson;
        this.newJson = newJson;
    }

    private Long id;

    private String orderNo;

    private Short originalOrderStatus;

    private Short newOrderStatus;

    private String changeMsg;

    private Short mainPoint;

    private Date created;

    private String createdBy;

    private String originalJson;

    private String newJson;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Short getOriginalOrderStatus() {
        return originalOrderStatus;
    }

    public void setOriginalOrderStatus(Short originalOrderStatus) {
        this.originalOrderStatus = originalOrderStatus;
    }

    public Short getNewOrderStatus() {
        return newOrderStatus;
    }

    public void setNewOrderStatus(Short newOrderStatus) {
        this.newOrderStatus = newOrderStatus;
    }

    public String getChangeMsg() {
        return changeMsg;
    }

    public void setChangeMsg(String changeMsg) {
        this.changeMsg = changeMsg;
    }

    public Short getMainPoint() {
        return mainPoint;
    }

    public void setMainPoint(Short mainPoint) {
        this.mainPoint = mainPoint;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getOriginalJson() {
        return originalJson;
    }

    public void setOriginalJson(String originalJson) {
        this.originalJson = originalJson;
    }

    public String getNewJson() {
        return newJson;
    }

    public void setNewJson(String newJson) {
        this.newJson = newJson;
    }

    @Override
    public String toString() {
        return "OrderChangeLog [id=" + id + ", orderNo=" + orderNo + ", originalOrderStatus=" + originalOrderStatus
                + ", newOrderStatus=" + newOrderStatus + ", changeMsg=" + changeMsg + ", mainPoint=" + mainPoint
                + ", created=" + created + ", createdBy=" + createdBy + ", originalJson=" + originalJson + ", newJson="
                + newJson + "]";
    }
}