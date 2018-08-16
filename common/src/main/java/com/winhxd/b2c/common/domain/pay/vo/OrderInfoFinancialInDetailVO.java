package com.winhxd.b2c.common.domain.pay.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhanghuan
 * @date 2018/8/15
 * 财务明细入账反参
 */
@Data
public class OrderInfoFinancialInDetailVO implements Serializable {
    private static final long serialVersionUID = -2960449858467596227L;
    @ApiModelProperty(value = "订单编号")
    private String orderNo;
    @ApiModelProperty(value = "接单门店ID")
    private Long storeId;
    @ApiModelProperty(value = "门店名称")
    private String storeName;
    @ApiModelProperty(value = "订单总金额")
    private BigDecimal orderTotalMoney;
    @ApiModelProperty(value = "实付金额")
    private BigDecimal realPaymentMoney;
    @ApiModelProperty(value = "支付类型:2:微信扫码付款;1为微信在线支付;")
    private Short payType;
    @ApiModelProperty(value = "支付状态:0为未支付;1为已支付;")
    private Short payStatus;
    @ApiModelProperty(value = "入账时间 关联对账表查询得出")
    private Date inCountTime;
    @ApiModelProperty(value = "下单门店区域编码")
    private String regionCode;
    @ApiModelProperty(value = "下单门店区域编码对应的名称")
    private String regionName;
}