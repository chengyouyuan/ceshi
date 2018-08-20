package com.winhxd.b2c.common.domain.pay.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel("出入账明细")
@Data
@ToString
public class PayFinanceAccountDetailVO {
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
	@ApiModelProperty("创建时间")
	private Date created;
	
	//财务统计页面新增字段
	@ApiModelProperty("门店名称")
	private String storeName;
	@ApiModelProperty("总进账")
	private BigDecimal allInMoney;
	@ApiModelProperty("总出账")
	private BigDecimal allOutMoney;
	@ApiModelProperty("今日进账")
	private BigDecimal todayInMoney;
	@ApiModelProperty("今日预收款")
	private BigDecimal todayPreMoney;
	@ApiModelProperty("今日实收款")
	private BigDecimal todayRealMoney;
	@ApiModelProperty("今日出账")
	private BigDecimal todayOutMoney;
	@ApiModelProperty("今日用户退款")
	private BigDecimal todayCustomerRefund;
	@ApiModelProperty("今日门店提现")
	private BigDecimal todayStoreWithdraw;
	@ApiModelProperty("优惠券抵用总金额")
	private BigDecimal useCouponAllMoney;
	@ApiModelProperty("优惠券今日抵用金额")
	private BigDecimal useTodayCouponAllMoney;
	@ApiModelProperty("当前余额")
	private BigDecimal curLeftMoney;
	@ApiModelProperty("全部手续费")
	private BigDecimal allCharge;
	@ApiModelProperty("今日手续费")
	private BigDecimal todayCharge;
	@ApiModelProperty("公司补充总入账")
	private BigDecimal companySupplementInMoney;
}
