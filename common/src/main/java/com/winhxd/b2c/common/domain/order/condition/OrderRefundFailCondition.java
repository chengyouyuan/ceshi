package com.winhxd.b2c.common.domain.order.condition;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单退款失败入参
 *
 * @author pangjianhua
 * @date 2018/9/11 14:44
 */
@Data
public class OrderRefundFailCondition {
    @ApiModelProperty(value = "是否是用户的原因 true为用户原因 false为系统原因", required = true)
    private Boolean customerFail;
    @ApiModelProperty(value = "退款失败错误码", required = true)
    private String refundErrorCode;
    @ApiModelProperty(value = "退款失败原因", required = true)
    private String refundErrorDesc;
    @ApiModelProperty(value = "订单号", required = true)
    private String orderNo;
}
