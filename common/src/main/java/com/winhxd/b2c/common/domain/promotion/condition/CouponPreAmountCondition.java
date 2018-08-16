package com.winhxd.b2c.common.domain.promotion.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @Auther wangxiaoshun
 * @Date 2018/8/9 19 49
 * @Description 计算优惠券最终优惠金额
 */
public class CouponPreAmountCondition extends ApiCondition{
    @ApiModelProperty(value = "优惠券发放id", required=true)
    private List<Long> sendIds;
    @ApiModelProperty(value = "商品信息", required=true)
    private List<CouponProductCondition> products;
    @ApiModelProperty(value = "支付方式 1扫码支付2线上支付", required=true)
    private String payType;

    public List<Long> getSendIds() {
        return sendIds;
    }

    public void setSendIds(List<Long> sendIds) {
        this.sendIds = sendIds;
    }

    public List<CouponProductCondition> getProducts() {
        return products;
    }

    public void setProducts(List<CouponProductCondition> products) {
        this.products = products;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }
}
