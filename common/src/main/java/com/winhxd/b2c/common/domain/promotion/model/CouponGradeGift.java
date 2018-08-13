package com.winhxd.b2c.common.domain.promotion.model;

import java.math.BigDecimal;

public class CouponGradeGift {
    private Integer id;

    private Long gradeDetailId;

    private String giftName;

    private String giftSku;

    private Integer count;

    private BigDecimal unitPrice;

    private String giftId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getGradeDetailId() {
        return gradeDetailId;
    }

    public void setGradeDetailId(Long gradeDetailId) {
        this.gradeDetailId = gradeDetailId;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getGiftSku() {
        return giftSku;
    }

    public void setGiftSku(String giftSku) {
        this.giftSku = giftSku;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }
}