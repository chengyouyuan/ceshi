package com.winhxd.b2c.common.domain.pay.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("门店银行卡信息")
@Data
public class StoreBankCardCondition extends ApiCondition{
	@ApiModelProperty("银行卡号")
    private String cardNumber;
	@ApiModelProperty("身份证号")
    private String personId;
	@ApiModelProperty("开户人姓名")
    private String bankUserName;
	@ApiModelProperty("银行名称")
    private String bankName;
	@ApiModelProperty("银行支行或者分行名称")
    private String bandBranchName;
	@ApiModelProperty("银行swiftCode")
    private String swiftCode;
	@ApiModelProperty("手机号")
    private String mobile;
	@ApiModelProperty("验证码")
    private String verificationCode;
	@ApiModelProperty("是否有效 0无效1有效")
    private Short status;
}