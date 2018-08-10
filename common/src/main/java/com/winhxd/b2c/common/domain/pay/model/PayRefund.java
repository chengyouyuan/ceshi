package com.winhxd.b2c.common.domain.pay.model;

import java.math.BigDecimal;
import java.util.Date;

public class PayRefund {
    private Long id;

    private String orderNo;

    private String orderTransactionNo;

    private String refundNo;

    private BigDecimal totalFee;

    private BigDecimal refundFee;

    private String refundFeeType;

    private BigDecimal realRefundFee;

    private String refundDesc;

    private Short channel;

    private String channelId;

    private BigDecimal cmmsAmt;

    private BigDecimal rate;

    private Short status;

    private Date created;

    private Date updated;

    private Long createdBy;

    private String createdByName;

    private Long updatedBy;

    private String updatedByName;

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

    public String getOrderTransactionNo() {
        return orderTransactionNo;
    }

    public void setOrderTransactionNo(String orderTransactionNo) {
        this.orderTransactionNo = orderTransactionNo;
    }

    public String getRefundNo() {
        return refundNo;
    }

    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo;
    }

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public BigDecimal getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(BigDecimal refundFee) {
        this.refundFee = refundFee;
    }

    public String getRefundFeeType() {
        return refundFeeType;
    }

    public void setRefundFeeType(String refundFeeType) {
        this.refundFeeType = refundFeeType;
    }

    public BigDecimal getRealRefundFee() {
        return realRefundFee;
    }

    public void setRealRefundFee(BigDecimal realRefundFee) {
        this.realRefundFee = realRefundFee;
    }

    public String getRefundDesc() {
        return refundDesc;
    }

    public void setRefundDesc(String refundDesc) {
        this.refundDesc = refundDesc;
    }

    public Short getChannel() {
        return channel;
    }

    public void setChannel(Short channel) {
        this.channel = channel;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public BigDecimal getCmmsAmt() {
        return cmmsAmt;
    }

    public void setCmmsAmt(BigDecimal cmmsAmt) {
        this.cmmsAmt = cmmsAmt;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
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

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedByName() {
        return updatedByName;
    }

    public void setUpdatedByName(String updatedByName) {
        this.updatedByName = updatedByName;
    }
}