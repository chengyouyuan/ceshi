package com.winhxd.b2c.common.domain.pay.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel("调起后台（支付网关）支付返回对象")
@Data
public class PayPreOrderVO {
	
	@ApiModelProperty("真实订单号")
	private String outOrderNo;
	@ApiModelProperty("支付流水号")
	private String outTradeNo;
	@ApiModelProperty("小程序ID")
	private String appId;
	@ApiModelProperty("时间戳")
	private String timeStamp;
	@ApiModelProperty("随机串")
	private String nonceStr;
	@ApiModelProperty("数据包")
	private String packageData;
	@ApiModelProperty("签名方式")
	private String signType;
	@ApiModelProperty("签名")
	private String paySign;
	@ApiModelProperty("支付状态：0.支付中，1.支付完成，2.支付失败")
	private Short payStatus;

}
