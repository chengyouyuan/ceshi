package com.winhxd.b2c.common.domain.promotion.condition;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author wl
 * @Date 2018/8/6 09:56
 * @Description
 **/
public class CouponTemplateCondition {

    @ApiModelProperty(value = "优惠券标题")
    private String title;
    @ApiModelProperty(value = "优惠券说明")
    private String exolian;
    @ApiModelProperty(value = "备注")
    private String remarks;
    @ApiModelProperty(value = "出资方规则ID")
    private Long investorId;
    @ApiModelProperty(value = "使用范围规则ID")
    private Long useRangeId;
    @ApiModelProperty(value = "适用对象规则ID")
    private Long applyRuleId;
    @ApiModelProperty(value = "优惠券标签")
    private String couponLabel;
    @ApiModelProperty(value = "优惠券金额计算方式  1订单金额 2商品金额")
    private Short calType;
    @ApiModelProperty(value = "支付方式 1扫码支付 2线上支付")
    private String payType;

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

    public Long getUseRangeId() {
        return useRangeId;
    }

    public void setUseRangeId(Long useRangeId) {
        this.useRangeId = useRangeId;
    }

    public Long getApplyRuleId() {
        return applyRuleId;
    }

    public void setApplyRuleId(Long applyRuleId) {
        this.applyRuleId = applyRuleId;
    }

    public String getCouponLabel() {
        return couponLabel;
    }

    public void setCouponLabel(String couponLabel) {
        this.couponLabel = couponLabel;
    }

    public Short getCalType() {
        return calType;
    }

    public void setCalType(Short calType) {
        this.calType = calType;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }
}
