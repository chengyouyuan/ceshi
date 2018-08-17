package com.winhxd.b2c.common.domain.pay.vo;

import com.winhxd.b2c.common.domain.pay.model.AccountingDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("费用明细")
@Data
public class VerifyDetailVO extends AccountingDetail {

    @ApiModelProperty("费用类型")
    private String detailTypeName;

    @ApiModelProperty("门店名称")
    private String storeName;

    @ApiModelProperty("与支付平台结算状态")
    private String thirdPartyVerifyStatusName;

    @ApiModelProperty("结算状态")
    private String verifyStatusName;

    public String getDetailTypeName() {
        return DetailTypeEnum.getMemoOfCode(getDetailType());
    }

    public String getThirdPartyVerifyStatusName() {
        return ThirdPartyVerifyStatusEnum.getMemoOfCode(getThirdPartyVerifyStatus());
    }

    public String getVerifyStatusName() {
        return VerifyStatusEnum.getMemoOfCode(getVerifyStatus());
    }
}
