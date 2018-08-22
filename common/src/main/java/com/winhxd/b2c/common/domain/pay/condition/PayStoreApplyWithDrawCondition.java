package com.winhxd.b2c.common.domain.pay.condition;

import java.math.BigDecimal;

import com.winhxd.b2c.common.domain.common.ApiCondition;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @Author zhanghuan
 * @Date 2018/8/15 09:54
 * @Description  申请提现入参
 **/
@Data
@ToString
public class PayStoreApplyWithDrawCondition extends ApiCondition {
    @ApiModelProperty("提现类型 1微信2银行卡")
    private short withdrawType;
    @ApiModelProperty("提现金额")
    private BigDecimal totalFee;
    @ApiModelProperty("实际到账金额")
    private BigDecimal realFee;
    @ApiModelProperty("手续费")
    private BigDecimal cmmsAmt;
    @ApiModelProperty("流向类型 1微信 2银行卡")
    private short flowDirectionType;
    @ApiModelProperty("流向名称 微信或者各个银行卡名称")
    private String flowDirectionName;
    @ApiModelProperty("流向账户(微信账号或者银行卡卡号)")
    private String paymentAccount;
    @ApiModelProperty("银行swiftCode")
    private String swiftCode;
    @ApiModelProperty("微信账号openid")
    private String buyerId;
    @ApiModelProperty("昵称")
    private String nick;
    @ApiModelProperty("门店名称")
    private String stroeName;
    @ApiModelProperty("手机号")
    private String mobile;
}
