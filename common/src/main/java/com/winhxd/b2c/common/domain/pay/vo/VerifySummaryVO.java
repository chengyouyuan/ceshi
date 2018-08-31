package com.winhxd.b2c.common.domain.pay.vo;

import com.winhxd.b2c.common.domain.pay.model.AccountingDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.Date;

@ApiModel("结算汇总")
@Data
public class VerifySummaryVO {

    @ApiModelProperty("前端返回唯一标识")
    private String index;

    @ApiModelProperty("门店ID")
    private Long storeId;

    @ApiModelProperty("门店名称")
    private String storeName;

    @ApiModelProperty("结算状态")
    private Integer verifyStatus;

    @ApiModelProperty("结算状态")
    private String verifyStatusName;

    @ApiModelProperty("订单截止日")
    private Date lastRecordedTime;

    private String verifyCode;

    @ApiModelProperty("操作时间")
    private Date operatedTime;

    @ApiModelProperty("操作人")
    private String operatedByName;

    @ApiModelProperty("实付款")
    private BigDecimal realPay;

    @ApiModelProperty("手续费")
    private BigDecimal thirdPartyFee;

    @ApiModelProperty("促销费")
    private BigDecimal discount;

    @ApiModelProperty("实结款")
    private BigDecimal realVerify;

    public String getVerifyStatusName() {
        return AccountingDetail.VerifyStatusEnum.getMemoOfCode(getVerifyStatus());
    }

    public String getIndex() {
        String notNullVerifyCode = String.valueOf(getVerifyCode());
        StringBuilder sb = new StringBuilder();
        sb.append(getVerifyStatus());
        sb.append(notNullVerifyCode);
        sb.append(String.valueOf(getStoreId()));
        return Base64.getEncoder().encodeToString(sb.toString().getBytes());
    }
}
