package com.winhxd.b2c.common.domain.promotion.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Auther wangxiaoshun
 * @Date 2018/8/18 17 08
 * @Description
 */
public class OrderAvailableCouponCondition extends ApiCondition{
    @ApiModelProperty(value = "支付方式 1为微信在线支付;2为微信扫码付款", required=true)
    private String payType;
    @ApiModelProperty(value = "门店id", required=true)
    private Long storeId;

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }
}
