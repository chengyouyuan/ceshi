package com.winhxd.b2c.common.domain.order.vo;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 门店每日订单销售数据汇总
 * @author wangbin
 * @date  2018年8月4日 上午10:07:46
 * @version 
 */
@ApiModel("门店每日订单销售数据汇总")
public class StoreOrderSalesSummaryVO {
    
    @ApiModelProperty(value = "今日营业额：统计当日24h内小店付款金额（=当面付款金额+微信在线付款金额-退款金额）")
    private BigDecimal dailyTurnover;
    
    @ApiModelProperty(value = "今日下单人数：统计当日24h内下单成功的UV")
    private BigDecimal dailyCustomerNum;
    
    @ApiModelProperty(value = "今日付款单数：统计当日24h内已付款单数（=微信在线付款单数+微信扫码付款单数-已退款单数）")
    private BigDecimal dailyOrderNum;
    
    @ApiModelProperty(value = "门店ID")
    private Long storeId;
    
}
