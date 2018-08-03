package com.winhxd.b2c.common.domain.shopcar.condition;

import java.math.BigDecimal;
import java.util.Date;

import com.winhxd.b2c.common.domain.base.condition.BaseCondition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author: wangbaokuo
 * @date: 2018/8/3 09:29
 * @description:
 */
@ApiModel("用户加购传参")
@Data
public class ShopCarCondition extends BaseCondition{

    @ApiModelProperty(value = "门店ID")
    private Long storeId;

    @ApiModelProperty(value = "自提地址")
    private String extractAddress;

    @ApiModelProperty(value = "自提时间")
    private Date extractTime;

    @ApiModelProperty(value = "商品sku")
    private String skuCode;

    @ApiModelProperty(value = "商品数量")
    private Integer prodNum;

    @ApiModelProperty(value = "支付方式(1:微信扫码付款,2微信在线付款)")
    private Short payType;

    @ApiModelProperty(value = "优惠券ID")
    private Long couponId;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "金额")
    private BigDecimal orderTotalMoney;

}
