package com.winhxd.b2c.common.domain.pay.vo;

import com.winhxd.b2c.common.domain.order.vo.ShopCarProdInfoVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by wangbaokuo on 2018/8/23 12:18
 */
@ApiModel("预订单返回数据")
@Data
public class ReadyOrderVO {
    @ApiModelProperty("取货方式（1:到店自提2:送货上门，多个用逗号分隔）")
    private String pickupType;
    @ApiModelProperty("到店自提地址：门店地址")
    private String storeAddress;
    @ApiModelProperty("用户地址ID")
    private Long customerAddressId;
    @ApiModelProperty("收货人")
    private String orderConsignee;
    @ApiModelProperty("收货人手机号")
    private String orderConsigneeMobile;
    @ApiModelProperty("收货人详细地址")
    private String orderAddress;
    @ApiModelProperty("用户地址标签")
    private String labelName;
    @ApiModelProperty("购物车商品Info")
    private List<ShopCarProdInfoVO> shopCarts;
    @ApiModelProperty("支付方式")
    private String payType;
    @ApiModelProperty("默认最优惠的优惠券")
    private CouponVO coupon;
    @ApiModelProperty("订单金额, ")
    private OrderMoneyVO orderMoney;
}
