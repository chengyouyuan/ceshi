package com.winhxd.b2c.common.domain.promotion.vo;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * @Auther wangxiaoshun
 * @Date 2018/8/13 15 30
 * @Description
 */
public class CouponInvestorAmountVO {
    @ApiModelProperty(value = "优惠券id", required=true)
    private Long templateId;
    @ApiModelProperty(value = "优惠券名称", required=true)
    private String couponName;
    @ApiModelProperty(value = "平台承担金额", required=true)
    private BigDecimal platformAmount;
    @ApiModelProperty(value = "品牌商承担金额", required=true)
    private BigDecimal brandAmount;

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public BigDecimal getPlatformAmount() {
        return platformAmount;
    }

    public void setPlatformAmount(BigDecimal platformAmount) {
        this.platformAmount = platformAmount;
    }

    public BigDecimal getBrandAmount() {
        return brandAmount;
    }

    public void setBrandAmount(BigDecimal brandAmount) {
        this.brandAmount = brandAmount;
    }
}
