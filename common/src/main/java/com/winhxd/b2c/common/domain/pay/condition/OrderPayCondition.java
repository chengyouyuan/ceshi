package com.winhxd.b2c.common.domain.pay.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("订单支付条件对象")
@Data
public class OrderPayCondition extends ApiCondition {
  
	@ApiModelProperty("订单号")
	private String orderNo;	
    
    /**
     * 买家用户标识
     * trade_type=JSAPI时（即公众号支付），此参数必传，此参数为微信用户在商户对应appid下的唯一标识（openid）
     */
	@ApiModelProperty("此参数必传，此参数为微信用户在商户对应appid下的唯一标识（openid）")
    private String openid;
    
    /**
     * 终端IP
     */
    @ApiModelProperty("终端IP")
    private String spbillCreateIp;
	
	/**
     * （非必填）设备号
     */
    @ApiModelProperty("（非必填）设备号")
    private String deviceInfo;
    
    /**
     * （非必填）商品ID
     */
    @ApiModelProperty("（非必填）商品ID")
    private String productId;

    /**
     * （非必填）附加数据，可作为自定义参数使用
     */
    @ApiModelProperty("（非必填）附加数据，可作为自定义参数使用")
    private String attach;
    
    /**
     * （非必填）
     * 限制支付类型（微信参数），上传此参数no_credit--可限制用户不能使用信用卡支付
     */
    @ApiModelProperty("（非必填）限制支付类型（微信参数），上传此参数no_credit--可限制用户不能使用信用卡支付")
    private String limitPay;
	
}
