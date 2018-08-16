package com.winhxd.b2c.common.domain.pay.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class PayRefundVO {

    @ApiModelProperty("退款接收状态")
    private boolean status;

    @ApiModelProperty("公众号ID/小程序ID")
    private String appid;

    @ApiModelProperty("微信订单号")
    private String transactionId;

    @ApiModelProperty("退款流水号")
    private String outRefundNo;

    @ApiModelProperty("微信退款ID")
    private String refundId;

    @ApiModelProperty("退款金额：元")
    private BigDecimal refundAmount;


}
