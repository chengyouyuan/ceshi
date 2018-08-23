package com.winhxd.b2c.common.domain.pay.vo;

import com.winhxd.b2c.common.domain.order.vo.ShopCarProdInfoVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by wangbaokuo on 2018/8/23 12:18
 */
@ApiModel("预订单返回数据")
@Data
public class ReadyOrderVO {
    @ApiModelProperty("自提地址：门店地址")
    private String storeAddredd;
    @ApiModelProperty("购物车商品Info")
    private List<ShopCarProdInfoVO> shopCars;
    @ApiModelProperty("支付方式")
    private String payType;
    @ApiModelProperty("默认最优惠的优惠券")
    private CouponVO coupon;
    @ApiModelProperty("订单金额, ")
    private OrderMoneyVO orderMoney;
}
