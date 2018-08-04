package com.winhxd.b2c.common.domain.promotion.model;

import java.math.BigDecimal;

/**
 *
 *@Deccription  坎级明细类
 *@User  wl
 *@Date   2018/8/4 17:30
 */
public class CouponGradeDetail {
    /**
     * 主键
     */
    private Long id;
    /**
     * 坎级ID 编码
     */
    private Long gradeId;
    /**
     * 满减金额
     */
    private BigDecimal reducedAmt;
    /**
     * 满减优惠类型 1-金额 2-折扣
     */
    private Short reducedType;
    /**
     * 优惠金额
     */
    private BigDecimal discountedAmt;
    /**
     * 满减优惠折扣
     */
    private BigDecimal discounted;
    /**
     * 优惠最大限额
     */
    private BigDecimal discountedMaxAmt;
    /**
     * 成本
     */
    private BigDecimal cost;

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

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
}