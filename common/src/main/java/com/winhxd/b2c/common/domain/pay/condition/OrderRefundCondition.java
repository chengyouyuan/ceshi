package com.winhxd.b2c.common.domain.pay.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liuhanning
 * @date  2018年8月13日 下午12:43:38
 * @Description 订单退款
 * @version 
 */
@Data
public class OrderRefundCondition extends ApiCondition {
	@ApiModelProperty("公众账号ID")
	private String appid;

	@ApiModelProperty("商户号")
	private String mchId;

	@ApiModelProperty("商户订单号")
	private String outTradeNo;

	@ApiModelProperty("微信订单号")
	private String transactionId;

	@ApiModelProperty("订单金额")
	private String totalFee;

	@ApiModelProperty("退款金额")
	private String refundFee;
}
