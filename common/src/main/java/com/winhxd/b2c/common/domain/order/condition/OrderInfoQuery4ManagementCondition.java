package com.winhxd.b2c.common.domain.order.condition;

import com.winhxd.b2c.common.domain.common.PagedCondition;
import com.winhxd.b2c.common.domain.common.inputmodel.DateInterval;
import com.winhxd.b2c.common.domain.common.inputmodel.NumInterval;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 门店、用户订单列表查询
 * @author wangbin
 * @date  2018年8月6日 下午2:58:44
 * @version 
 */
@ApiModel("门店、用户订单列表查询传参")
@Data
public class OrderInfoQuery4ManagementCondition extends PagedCondition {

    @ApiModelProperty(value = "门店id")
    private Long storeId;
    
    @ApiModelProperty(value = "用户id")
    private Long customerId;
    
    @ApiModelProperty(value = "订单号数组")
    private String[] orderNos;
    
    @ApiModelProperty(value = "订单号")
    private String orderNo;
    
    @ApiModelProperty(value = "下单时间区间")
    private DateInterval dateInterval;
    
    @ApiModelProperty(value = "订单价格区间")
    private NumInterval moneyInterval;
    
    @ApiModelProperty(value = "计价类型:1:线上计价;2:线下计价;")
    private Short valuationType;
    
    @ApiModelProperty(value = "支付类型:2为微信扫码付款;1为微信在线支付;")
    private Short payType;
    
    @ApiModelProperty(value = "订单状态 1:已提交;2:待付款;3:待接单;7:已计价;9:待自提(已确认);22:已完成;99:已取消;77:已退款;33:待退款;")
    private Short orderStatus;
    
    @ApiModelProperty(value = "是否使用优惠券 1:是;2:否")
    private Short useCoupon;
    
    @ApiModelProperty(value = "地理区域编码")
    private String[] regionCode;

    @ApiModelProperty(value = "提货方式:1自提;2配送;")
    private Short pickupType;

    @ApiModelProperty(value = "收货人")
    private String orderConsignee;

    @ApiModelProperty(value = "收货电话")
    private String orderConsigneeMobile;

}
