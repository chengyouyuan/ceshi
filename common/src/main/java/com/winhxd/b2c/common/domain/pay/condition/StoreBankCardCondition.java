package com.winhxd.b2c.common.domain.pay.condition;

import java.util.Date;

import com.winhxd.b2c.common.domain.common.ApiCondition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("门店银行卡信息")
@Data
public class StoreBankCardCondition extends ApiCondition{
	@ApiModelProperty("主键")
    private Long id;
	@ApiModelProperty("门店id")
    private Long storeId;
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
	@ApiModelProperty("创建人id")
    private Long createdBy;
	@ApiModelProperty("创建人")
    private String createdByName;
	@ApiModelProperty("创建时间(使用时间)")
    private Date created;
	@ApiModelProperty("修改人id")
    private Long updatedBy;
	@ApiModelProperty("修改人")
    private String updatedByName;
	@ApiModelProperty("修改时间")
    private Date updated;
}