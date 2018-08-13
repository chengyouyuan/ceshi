package com.winhxd.b2c.common.domain.promotion.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Auther wangxiaoshun
 * @Date 2018/8/8 15 40
 * @Description 用户领取优惠券
 */
public class ReceiveCouponCondition extends ApiCondition{
    @ApiModelProperty(value = "活动id")
    private Long couponActivityId;
    @ApiModelProperty(value = "优惠券id")
    private Long templateId;

    public Long getCouponActivityId() {
        return couponActivityId;
    }

    public void setCouponActivityId(Long couponActivityId) {
        this.couponActivityId = couponActivityId;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }
}
