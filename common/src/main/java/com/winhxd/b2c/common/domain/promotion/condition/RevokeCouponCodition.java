package com.winhxd.b2c.common.domain.promotion.condition;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @Auther wangxiaoshun
 * @Date 2018/8/9 15 11
 * @Description
 */
public class RevokeCouponCodition {
    @ApiModelProperty(value = "优惠券发放id", required=true)
    private List<Long> sendIds;

    public List<Long> getSendIds() {
        return sendIds;
    }

    public void setSendIds(List<Long> sendIds) {
        this.sendIds = sendIds;
    }
}
