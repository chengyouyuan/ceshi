package com.winhxd.b2c.common.domain.pay.condition;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.winhxd.b2c.common.domain.common.PagedCondition;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhanghuan
 * @date 2018/8/15
 */
@Data
public class OrderInfoFinancialInDetailCondition extends PagedCondition implements Serializable {
    private static final long serialVersionUID = -2960449858467596227L;
    @ApiModelProperty(value = "订单编号")
    private String orderNo;
    @ApiModelProperty(value = "接单门店ID")
    private Long storeId;
    @ApiModelProperty(value = "门店名称")
    private String storeName;
    @ApiModelProperty(value = "入账状态 0为预入账；1为实入账")
    private short InAcountStatus;
    @ApiModelProperty(value = "微信对账状态 0为未入账 1为已入账")
    private Short wechatAccountStatus;
    @ApiModelProperty(value = "入账时间开始时间")
    private Date inCountStartTime;
    @ApiModelProperty(value = "入账时间结束时间")
    private Date inCountEndTime;
    @ApiModelProperty(value = "下单门店区域编码")
    private String regionCode;
    @ApiModelProperty(value = "下单门店区域编码对应的名称")
    private String regionName;
}