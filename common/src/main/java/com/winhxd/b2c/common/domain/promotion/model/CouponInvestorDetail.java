package com.winhxd.b2c.common.domain.promotion.model;

public class CouponInvestorDetail {
    private Long id;

    private Long investorId;

    private Integer investor;

    private Float percent;

    private String ids;

    private String names;

    private Short investorType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInvestorId() {
        return investorId;
    }

    public void setInvestorId(Long investorId) {
        this.investorId = investorId;
    }

    public Integer getInvestor() {
        return investor;
    }

    public void setInvestor(Integer investor) {
        this.investor = investor;
    }

    public Float getPercent() {
        return percent;
    }

    public void setPercent(Float percent) {
        this.percent = percent;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public Short getInvestorType() {
        return investorType;
    }

    public void setInvestorType(Short investorType) {
        this.investorType = investorType;
    }
}