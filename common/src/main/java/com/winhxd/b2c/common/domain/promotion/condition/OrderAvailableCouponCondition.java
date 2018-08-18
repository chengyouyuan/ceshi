package com.winhxd.b2c.common.domain.promotion.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Auther wangxiaoshun
 * @Date 2018/8/18 17 08
 * @Description
 */
public class OrderAvailableCouponCondition extends ApiCondition{
    @ApiModelProperty(value = "支付方式 1扫码支付2线上支付", required=true)
    private String payType;
    @ApiModelProperty(value = "门店id", required=true)
    private String storeId;

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}
