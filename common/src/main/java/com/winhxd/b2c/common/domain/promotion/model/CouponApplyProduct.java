package com.winhxd.b2c.common.domain.promotion.model;

import java.math.BigDecimal;

public class CouponApplyProduct {
    private Integer id;

    private Integer applyId;

    private BigDecimal appointMinTotal;

    private Integer appointMinNumber;

    private Integer mustMinNumber;

    private BigDecimal mustMinTotal;

    private Integer spareMinNumber;

    private BigDecimal spareMinTotal;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getApplyId() {
        return applyId;
    }

    public void setApplyId(Integer applyId) {
        this.applyId = applyId;
    }

    public BigDecimal getAppointMinTotal() {
        return appointMinTotal;
    }

    public void setAppointMinTotal(BigDecimal appointMinTotal) {
        this.appointMinTotal = appointMinTotal;
    }

    public Integer getAppointMinNumber() {
        return appointMinNumber;
    }

    public void setAppointMinNumber(Integer appointMinNumber) {
        this.appointMinNumber = appointMinNumber;
    }

    public Integer getMustMinNumber() {
        return mustMinNumber;
    }

    public void setMustMinNumber(Integer mustMinNumber) {
        this.mustMinNumber = mustMinNumber;
    }

    public BigDecimal getMustMinTotal() {
        return mustMinTotal;
    }

    public void setMustMinTotal(BigDecimal mustMinTotal) {
        this.mustMinTotal = mustMinTotal;
    }

    public Integer getSpareMinNumber() {
        return spareMinNumber;
    }

    public void setSpareMinNumber(Integer spareMinNumber) {
        this.spareMinNumber = spareMinNumber;
    }

    public BigDecimal getSpareMinTotal() {
        return spareMinTotal;
    }

    public void setSpareMinTotal(BigDecimal spareMinTotal) {
        this.spareMinTotal = spareMinTotal;
    }
}