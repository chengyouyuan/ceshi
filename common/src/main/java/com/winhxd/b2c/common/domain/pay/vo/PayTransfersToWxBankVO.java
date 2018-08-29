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
 * @Description: 转账至微信银行卡返参
 */
@Setter
@Getter
public class PayTransfersToWxBankVO {

    @ApiModelProperty("商户号")
    private String mchid;

    @ApiModelProperty("商户订单号")
    private String partnerTradeNo;

    @ApiModelProperty("处理结果, true为成功")
    private boolean transfersResult;

    @ApiModelProperty("错误代码")
    private String errorCode;

    @ApiModelProperty("是否可以继续, 为true表示该申请可以继续使用")
    private boolean ableContinue;

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
