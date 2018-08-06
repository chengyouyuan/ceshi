package com.winhxd.b2c.common.domain.promotion.model;

import java.math.BigDecimal;
import java.util.Date;

public class CouponActivity {
    private Long id;

    private String name;

    private String code;

    private String exolian;

    private Short remarks;

    private Short type;

    private Short couponType;

    private Date activityStart;

    private Date activityEnd;

    private Short couponNumType;

    private BigDecimal couponNum;

    private Short customerVoucherLimitType;

    private Long customerVoucherLimitNum;

    private BigDecimal sendNum;

    private Date sendTime;

    private Short status;

    private Long createdBy;

    private String createdByName;

    private Date created;

    private Long updateBy;

    private String updatedByName;

    private Date updated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getExolian() {
        return exolian;
    }

    public void setExolian(String exolian) {
        this.exolian = exolian;
    }

    public Short getRemarks() {
        return remarks;
    }

    public void setRemarks(Short remarks) {
        this.remarks = remarks;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public Short getCouponType() {
        return couponType;
    }

    public void setCouponType(Short couponType) {
        this.couponType = couponType;
    }

    public Date getActivityStart() {
        return activityStart;
    }

    public void setActivityStart(Date activityStart) {
        this.activityStart = activityStart;
    }

    public Date getActivityEnd() {
        return activityEnd;
    }

    public void setActivityEnd(Date activityEnd) {
        this.activityEnd = activityEnd;
    }

    public Short getCouponNumType() {
        return couponNumType;
    }

    public void setCouponNumType(Short couponNumType) {
        this.couponNumType = couponNumType;
    }

    public BigDecimal getCouponNum() {
        return couponNum;
    }

    public void setCouponNum(BigDecimal couponNum) {
        this.couponNum = couponNum;
    }

    public Short getCustomerVoucherLimitType() {
        return customerVoucherLimitType;
    }

    public void setCustomerVoucherLimitType(Short customerVoucherLimitType) {
        this.customerVoucherLimitType = customerVoucherLimitType;
    }

    public Long getCustomerVoucherLimitNum() {
        return customerVoucherLimitNum;
    }

    public void setCustomerVoucherLimitNum(Long customerVoucherLimitNum) {
        this.customerVoucherLimitNum = customerVoucherLimitNum;
    }

    public BigDecimal getSendNum() {
        return sendNum;
    }

    public void setSendNum(BigDecimal sendNum) {
        this.sendNum = sendNum;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
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

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdatedByName() {
        return updatedByName;
    }

    public void setUpdatedByName(String updatedByName) {
        this.updatedByName = updatedByName;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}