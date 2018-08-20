package com.winhxd.b2c.common.domain.pay.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author zhanghaun
 * @date  2018年8月16日 
 * @Description 获取需要提现的用户基本信息
 * @version
 */
@Data
@ToString
public class PayStoreUserInfoVO implements Serializable {
	private static final long serialVersionUID = 1L;
	@ApiModelProperty("门店名称")
    private String storeName;
	@ApiModelProperty("用户账号")
    private String storeMobile;
	@ApiModelProperty("门店地址")
    private String storeAddress;
	@ApiModelProperty("区域编码")
    private String storeRegionCode;
	@ApiModelProperty("联系方式")
    private String contactMobile;
	@ApiModelProperty("微信openid")
    private String openid;
	@ApiModelProperty("微信昵称")
    private String nick;
	@ApiModelProperty("用户实名")
    private String name;
	@ApiModelProperty("惠小店状态（0、未开店，1、有效，2、无效）")
    private Short storeStatus;
	
	@ApiModelProperty("总金额")
    private BigDecimal totalMoney;
	@ApiModelProperty("可提现金额")
    private BigDecimal totalFee;
    @ApiModelProperty("流向类型 1微信 2银行卡")
    private short flowDirectionType;
    @ApiModelProperty("流向名称 微信或者各个银行卡名称")
    private String flowDirectionName;
    @ApiModelProperty("银行卡卡号")
    private String cardNumber;
    @ApiModelProperty("银行支行或者分行名称")
	private String bandBranchName;
    @ApiModelProperty("银行名称")
    private String bankName;
    @ApiModelProperty("开户人姓名")
    private String bankUserName;
}