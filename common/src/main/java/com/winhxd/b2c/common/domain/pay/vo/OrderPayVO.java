package com.winhxd.b2c.common.domain.pay.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("订单支付返回对象")
@Data
public class OrderPayVO {

	@ApiModelProperty("时间戳")
	private String timeStamp;
	@ApiModelProperty("随机串")
	private String nonceStr;
	@ApiModelProperty("数据包")
	@JsonProperty("package")
	private String packageData;
	@ApiModelProperty("签名方式")
	private String signType;
	@ApiModelProperty("签名")
	private String paySign;
	@ApiModelProperty("appid")
	private String appid;
}
