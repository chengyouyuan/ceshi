package com.winhxd.b2c.common.domain.pay.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BanksVO {
	@ApiModelProperty("银行卡ID")
	private String bankCode;
	
	@ApiModelProperty("银行名称")
	private String bankName;
}
