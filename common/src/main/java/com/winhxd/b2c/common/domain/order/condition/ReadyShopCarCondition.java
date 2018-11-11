package com.winhxd.b2c.common.domain.order.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;
import com.winhxd.b2c.common.domain.order.vo.ShopCarProdInfoVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author: wangbaokuo
 * @date: 2018/8/3 09:29
 * @description:
 */
@ApiModel("预订单传参")
@Data
public class ReadyShopCarCondition extends ApiCondition {

    @ApiModelProperty(value = "门店ID", required = true)
    private Long storeId;

    @ApiModelProperty(value = "自提地址", required = true)
    private String extractAddress;

    @ApiModelProperty(value = "自提时间", required = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date pickupDateTime;

    @ApiModelProperty(value = "支付方式(1:微信在线付款,2:微信扫码付款)", required = true)
    private Short payType;

    @ApiModelProperty(value = "优惠券ID", required = false)
    private Long[] couponIds;

    @ApiModelProperty(value = "备注", required = false)
    private String remark;

    @ApiModelProperty(value = "金额", required = false)
    private BigDecimal orderTotalMoney;

    @ApiModelProperty(value = "终端IP", required = true)
    private String spbillCreateIp;

    @ApiModelProperty(value = "设备号", required = false)
    private String deviceInfo;

    @ApiModelProperty(value = "提货类型:1:门店自提;2:送货上门", required=true)
    private Short pickupType;

    @ApiModelProperty(value = "用户收货地址ID", required=false)
    private Long customerAddressId;
    
    @ApiModelProperty(value = "购物车商品Info", required = true)
    private List<ShopCarProdInfoVO> shopCarts;

}
