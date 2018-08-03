package com.winhxd.b2c.common.exception;

import io.swagger.annotations.ApiModelProperty;

public class OrderCreateExceptonCodes {

    private OrderCreateExceptonCodes() {
    }
    
    @ApiModelProperty("服务器内部错误")
    public static final int CODE_10000 = 10000;
    
    @ApiModelProperty("订单创建客户id为空")
    public static final int CODE_401001 = 401001;
    
    @ApiModelProperty("订单创建门店id为空")
    public static final int CODE_401002 = 401002;
    
    @ApiModelProperty("订单创建支付类型为空或错误")
    public static final int CODE_401003 = 401003;
    
    @ApiModelProperty("订单创建自提时间为空")
    public static final int CODE_401004 = 401004;
    
    @ApiModelProperty("订单创建商品信息为空")
    public static final int CODE_401005 = 401005;
    
    @ApiModelProperty("订单创建商品数量信息错误")
    public static final int CODE_401006 = 401006;
    
    @ApiModelProperty("订单创建商品sku为空")
    public static final int CODE_401007 = 401007;
    
    @ApiModelProperty("订单创建不支持订单类型")
    public static final int CODE_401008 = 401008;
}
