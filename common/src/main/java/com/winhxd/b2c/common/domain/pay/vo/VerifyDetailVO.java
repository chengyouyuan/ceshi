package com.winhxd.b2c.common.domain.pay.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.winhxd.b2c.common.domain.pay.model.AccountingDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel("费用明细")
@Data
public class VerifyDetailVO extends AccountingDetail {

    @ApiModelProperty("前端返回唯一标识")
    private String index;

    @ApiModelProperty("费用类型")
    @Excel(name = "费用类型", width = 30)
    private String detailTypeName;

    @ApiModelProperty("门店名称")
    @Excel(name = "门店名称", width = 30)
    private String storeName;

    @ApiModelProperty("与支付平台结算状态")
    @Excel(name = "微信入账", width = 30)
    private String thirdPartyVerifyStatusName;

    @ApiModelProperty("结算状态")
    @Excel(name = "结算状态", width = 30)
    private String verifyStatusName;

    @ApiModelProperty("支付平台手续费")
    @Excel(name = "手续费", width = 30)
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
