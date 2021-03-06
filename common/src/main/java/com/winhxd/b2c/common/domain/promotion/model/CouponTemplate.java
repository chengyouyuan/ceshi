package com.winhxd.b2c.common.domain.promotion.model;

import java.util.Date;

public class CouponTemplate {
    private Long id;

    private String title;

    private String exolian;

    private String remarks;

    private Long investorId;

    private Long gradeId;

    private Long applyRuleId;

    private String corner;

    private String couponLabel;

    private Integer couponLabelColor;

    private Short status;

    private String reason;

    private String code;

    private Short calType;

    private Short payType;

    private Long createdBy;

    private String createdByName;

    private Date created;

    private Long updatedBy;

    private String updatedByName;

    private Date updated;


    /**
     * 出资方规则名称
     */
    private String investorName;

    /**
     * 优惠券优惠方式名称
     */
    private String gradeName;

    /**
     * 优惠券类型规则名称
     */
    private String applyRuleName;


    /**
     * 优惠券类型规则类型
     */
    private Short applyRuleType;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExolian() {
        return exolian;
    }

    public void setExolian(String exolian) {
        this.exolian = exolian;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getInvestorId() {
        return investorId;
    }

    public void setInvestorId(Long investorId) {
        this.investorId = investorId;
    }

    public Long getGradeId() {
        return gradeId;
    }

    public void setGradeId(Long gradeId) {
        this.gradeId = gradeId;
    }

    public Long getApplyRuleId() {
        return applyRuleId;
    }

    public void setApplyRuleId(Long applyRuleId) {
        this.applyRuleId = applyRuleId;
    }

    public String getCorner() {
        return corner;
    }

    public void setCorner(String corner) {
        this.corner = corner;
    }

    public String getCouponLabel() {
        return couponLabel;
    }

    public void setCouponLabel(String couponLabel) {
        this.couponLabel = couponLabel;
    }

    public Integer getCouponLabelColor() {
        return couponLabelColor;
    }

    public void setCouponLabelColor(Integer couponLabelColor) {
        this.couponLabelColor = couponLabelColor;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Short getCalType() {
        return calType;
    }

    public void setCalType(Short calType) {
        this.calType = calType;
    }

    public Short getPayType() {
        return payType;
    }

    public void setPayType(Short payType) {
        this.payType = payType;
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

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }


    public String getInvestorName() {
        return investorName;
    }

    public void setInvestorName(String investorName) {
        this.investorName = investorName;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getApplyRuleName() {
        return applyRuleName;
    }

    public void setApplyRuleName(String applyRuleName) {
        this.applyRuleName = applyRuleName;
    }

    public Short getApplyRuleType() {
        return applyRuleType;
    }

    public void setApplyRuleType(Short applyRuleType) {
        this.applyRuleType = applyRuleType;
    }
}