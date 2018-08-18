package com.winhxd.b2c.common.domain.pay.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * PayTransfersToWxChangeVO
 *
 * @Author yindanqing
 * @Date 2018/8/17 10:27
 * @Description:
 */
@Setter
@Getter
public class PayTransfersToWxChangeVO {

    @ApiModelProperty("商户appid")
    private String mchAppid;

    @ApiModelProperty("商户号")
    private String mchid;

    @ApiModelProperty("设备号")
    private String deviceInfo;

    @ApiModelProperty("随机字符串")
    private String nonceStr;

    @ApiModelProperty("商户订单号")
    private String partnerTradeNo;

    @ApiModelProperty("微信订单号")
    private String paymentNo;

    @ApiModelProperty("处理结果")
    private boolean transfersResult;

    @ApiModelProperty("是否可以继续, 为true表示该申请可以继续使用")
    private boolean ableContinue;

    @ApiModelProperty("错误描述")
    private String errorDesc;

    @ApiModelProperty("微信支付成功时间")
    private Date paymentTime;

}
