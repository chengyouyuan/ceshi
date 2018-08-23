package com.winhxd.b2c.common.domain.order.vo;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 门店每日订单销售数据汇总
 * @author wangbin
 * @date  2018年8月4日 上午10:07:46
 * @version 
 */
@ApiModel("门店每日订单销售数据汇总")
@Data
public class StoreOrderSalesSummaryVO {
    
    @ApiModelProperty(value = "营业额：统计指定时间内小店付款金额（=当面付款金额+微信在线付款金额-退款金额）")
    private BigDecimal turnover = BigDecimal.ZERO;
    
    @ApiModelProperty(value = "下单人数：统计指定时间内已付款单数（=微信在线付款单数+微信扫码付款单数-已退款单数）的UV")
    private Integer customerNum = 0;
    
    @ApiModelProperty(value = "付款单数：统计指定时间内已付款单数（=微信在线付款单数+微信扫码付款单数-已退款单数）")
    private Integer orderNum = 0;
    
    @ApiModelProperty(value = "门店ID")
    private Long storeId;
    
    @ApiModelProperty(value = "订单商品总数量")
    private Integer skuQuantity = 0;
    
    @ApiModelProperty(value = "订单商品种类数量")
    private Integer skuCategoryQuantity = 0;
    
}
