package com.winhxd.b2c.common.domain.pay.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("出入账明细")
@Data
public class OutInAccountDetail implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModelProperty("主键")
    private Long id;
	@ApiModelProperty("订单编码")
    private String orderNo;
	@ApiModelProperty("交易流水号")
    private String tradeNo;
	@ApiModelProperty("金额")
    private BigDecimal money;
	@ApiModelProperty("门店Id")
    private Long storeId;
	@ApiModelProperty("地理区域编码")
    private String regionCode;
	@ApiModelProperty("地理区域名称")
    private String regionName;
	@ApiModelProperty("类型")// 1出账 2入账
    private Short type;
	@ApiModelProperty("1门店提现 2用户退款")//1门店提现 2用户退款
    private Short outType;
	@ApiModelProperty("微信账单状态")//1未入账 2已入账
    private Short wechatAccountStatus;
	@ApiModelProperty("状态")//0无效 1有效
    private Short status;
	@ApiModelProperty("入账状态")//1预入账 2实入账
    private Short dataStatus;
	@ApiModelProperty("流向类型")//1微信 2银行卡
    private Short flowDirectionType;
	@ApiModelProperty("流向名称")//微信或者各个银行卡名称
    private String flowDirectionName;
	@ApiModelProperty("退款账户")
    private String paymentAccount;
	@ApiModelProperty("手续费")
    private BigDecimal cmmsAmt;
	@ApiModelProperty("费率")
    private BigDecimal rate;
	@ApiModelProperty("出入账时间")
    private Date outInDate;
	@ApiModelProperty("创建人id")
    private Long createdBy;
	@ApiModelProperty("创建人")
    private String createdByName;
	@ApiModelProperty("创建时间")
    private Date created;
	@ApiModelProperty("修改人id")
    private Long updatedBy;
	@ApiModelProperty("修改人")
    private String updatedByName;
	@ApiModelProperty("修改时间")
    private Date updated;
}