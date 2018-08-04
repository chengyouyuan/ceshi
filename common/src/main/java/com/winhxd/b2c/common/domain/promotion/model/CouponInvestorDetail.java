package com.winhxd.b2c.common.domain.promotion.model;
/**
 *
 *@Deccription  出资方明细类
 *@User  wl
 *@Date   2018/8/4 17:18
 */
public class CouponInvestorDetail {
    /**
     * 主键
     */
    private Integer id;
    /**
     * 出资方ID
     */
    private Long investorId;
    /**
     * 出资方
     */
    private Integer investor;
    /**
     * 占比
     */
    private Float percent;
    /**
     * 品牌/供应商 ID
     */
    private String ids;
    /**
     * 品牌/供应商名称
     */
    private String names;
    /**
     * 出资方  1-惠下单  2-品牌商
     */
    private Short investorType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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