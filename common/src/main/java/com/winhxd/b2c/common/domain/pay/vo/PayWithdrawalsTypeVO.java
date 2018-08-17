package com.winhxd.b2c.common.domain.pay.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("提现类型")
@Data
public class PayWithdrawalsTypeVO implements Serializable {
	@ApiModelProperty("类型1 到微信  2到银行卡")
    private Short type;
	@ApiModelProperty("说明")
    private String remarks;
	@ApiModelProperty("是否有效 0无效1有效")
    private Short status;

    private static final long serialVersionUID = 1L;
}