package com.winhxd.b2c.common.domain.pay.condition;

import java.util.Date;

import com.winhxd.b2c.common.domain.common.PagedCondition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@ApiModel("出入账明细查询条件")
@Data
@ToString
public class PayFinanceAccountDetailCondition extends PagedCondition{
	@ApiModelProperty("订单编码")
    private String orderNo;
	@ApiModelProperty("门店id")
    private Long storeId;
	@ApiModelProperty("门店名称")
    private String storeName;
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
	@ApiModelProperty("入账状态")//1预入账 2实入账
    private Short dataStatus;
	@ApiModelProperty("出入账开始时间")
	private Date outInStartDate;
	@ApiModelProperty("出入账结束时间")
	private Date outInEndDate;
	@ApiModelProperty("创建开始时间")
	private Date createdStart;
	@ApiModelProperty("创建结束时间")
	private Date createdEnd;
}
