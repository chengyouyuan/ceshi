package com.winhxd.b2c.common.domain.promotion.model;

import java.math.BigDecimal;

public class CouponGradeDetail {
    private Long id;

    private Long gradeId;

    private BigDecimal reducedAmt;

    private Short reducedType;

    private BigDecimal discountedAmt;

    private BigDecimal discounted;

    private BigDecimal discountedMaxAmt;

    private BigDecimal fullGiveAmt;

    private BigDecimal increaseAmt;

    private BigDecimal cost;

    private Integer count;

    private Integer times;

    private String remarks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGradeId() {
        return gradeId;
    }

    public void setGradeId(Long gradeId) {
        this.gradeId = gradeId;
    }

    public BigDecimal getReducedAmt() {
        return reducedAmt;
    }

    public void setReducedAmt(BigDecimal reducedAmt) {
        this.reducedAmt = reducedAmt;
    }

    public Short getReducedType() {
        return reducedType;
    }

    public void setReducedType(Short reducedType) {
        this.reducedType = reducedType;
    }

    public BigDecimal getDiscountedAmt() {
        return discountedAmt;
    }

    public void setDiscountedAmt(BigDecimal discountedAmt) {
        this.discountedAmt = discountedAmt;
    }

    public BigDecimal getDiscounted() {
        return discounted;
    }

    public void setDiscounted(BigDecimal discounted) {
        this.discounted = discounted;
    }

    public BigDecimal getDiscountedMaxAmt() {
        return discountedMaxAmt;
    }

    public void setDiscountedMaxAmt(BigDecimal discountedMaxAmt) {
        this.discountedMaxAmt = discountedMaxAmt;
    }

    public BigDecimal getFullGiveAmt() {
        return fullGiveAmt;
    }

    public void setFullGiveAmt(BigDecimal fullGiveAmt) {
        this.fullGiveAmt = fullGiveAmt;
    }

    public BigDecimal getIncreaseAmt() {
        return increaseAmt;
    }

    public void setIncreaseAmt(BigDecimal increaseAmt) {
        this.increaseAmt = increaseAmt;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}