package com.winhxd.b2c.common.domain.pay.vo;

import com.winhxd.b2c.common.domain.pay.model.AccountingDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@ApiModel("费用明细")
@Data
public class VerifyDetailVO extends AccountingDetail {

    @ApiModelProperty("前端返回唯一标识")
    private String index;

    @ApiModelProperty("费用类型")
    private String detailTypeName;

    @ApiModelProperty("门店名称")
    private String storeName;

    @ApiModelProperty("与支付平台结算状态")
    private String thirdPartyVerifyStatusName;

    @ApiModelProperty("结算状态")
    private String verifyStatusName;

    @ApiModelProperty("支付平台手续费")
    private BigDecimal thirdPartyFeeMoney;

    public String getDetailTypeName() {
        return DetailTypeEnum.getMemoOfCode(getDetailType());
    }

    public String getThirdPartyVerifyStatusName() {
        return ThirdPartyVerifyStatusEnum.getMemoOfCode(getThirdPartyVerifyStatus());
    }

    public String getVerifyStatusName() {
        return VerifyStatusEnum.getMemoOfCode(getVerifyStatus());
    }

    public String getIndex() {
        return String.valueOf(getId());
    }
}
