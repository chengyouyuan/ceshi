package com.winhxd.b2c.common.domain.pay.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhanghuan
 * @date 2018/8/15
 * 财务明细出账反参
 */
@Data
public class OrderInfoFinancialOutDetailVO implements Serializable {
    private static final long serialVersionUID = -2960449858467596227L;
    @ApiModelProperty(value = "订单总金额")
    private BigDecimal TotalMoney;
    @ApiModelProperty(value = "订单总数")
    private Integer totalCount;
    @ApiModelProperty(value = "实付金额")
    private BigDecimal realPaymentMoney;
    @ApiModelProperty("流向类型")//1微信 2银行卡
    private Short flowDirectionType;
	@ApiModelProperty("流向名称")//微信或者各个银行卡名称
    private String flowDirectionName;
	@ApiModelProperty("付款账户")
    private String paymentAccount;
	@ApiModelProperty("银行手续费")
    private BigDecimal cmmsAmt;
	@ApiModelProperty("微信费率")
    private BigDecimal rate;
    @ApiModelProperty(value = "1门店提现 2用户退款")
    private Date outType;
    @ApiModelProperty(value = "下单门店区域编码")
    private String regionCode;
    @ApiModelProperty(value = "下单门店区域编码对应的名称")
    private String regionName;
    @ApiModelProperty(value = "出账时间")
    private Date outAccountTime;
    @ApiModelProperty(value = "申请时间")
    private Date applyTime;
}