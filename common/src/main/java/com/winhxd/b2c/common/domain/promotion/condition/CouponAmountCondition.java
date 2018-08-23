package com.winhxd.b2c.common.domain.promotion.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @Auther wangxiaoshun
 * @Date 2018/8/22 18 35
 * @Description C端计算优惠券优惠金额
 */
public class CouponAmountCondition extends ApiCondition {
    @ApiModelProperty(value = "优惠券发放id", required=true)
    private List<Long> sendIds;

    @ApiModelProperty(value = "门店id", required=true)
    private Long storeId;


    public List<Long> getSendIds() {
        return sendIds;
    }

    public void setSendIds(List<Long> sendIds) {
        this.sendIds = sendIds;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }
}
