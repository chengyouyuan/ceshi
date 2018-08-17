package com.winhxd.b2c.common.domain.pay.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * PayTransfersToWxBankVO
 *
 * @Author yindanqing
 * @Date 2018/8/17 10:27
 * @Description:
 */
@Setter
@Getter
public class PayTransfersToWxBankVO {

    @ApiModelProperty("商户号")
    private String mchid;

    @ApiModelProperty("商户订单号")
    private String partnerTradeNo;

    @ApiModelProperty("处理结果")
    private boolean transfersResult;

    @ApiModelProperty("错误描述")
    private String errorDesc;

    @ApiModelProperty("代付金额")
    private BigDecimal amount;

    @ApiModelProperty("手续费金额")
    private BigDecimal cmmsAmt;

    @ApiModelProperty("随机字符串")
    private String nonceStr;

    @ApiModelProperty("微信企业付款单号")
    private String paymentNo;

}
