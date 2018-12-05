package com.winhxd.b2c.common.domain.pay.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
    @Excel(name = "门店ID", width = 30)
    private Long storeId;

    @ApiModelProperty("门店名称")
    @Excel(name = "门店名称", width = 30)
    private String storeName;

    @ApiModelProperty("结算状态")
    private Integer verifyStatus;

    @ApiModelProperty("结算状态")
    @Excel(name = "结算状态", width = 30)
    private String verifyStatusName;

    @ApiModelProperty("订单截止日")
    @Excel(name = "订单截止日", width = 30, exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date lastRecordedTime;

    private String verifyCode;

    @ApiModelProperty("操作时间")
    @Excel(name = "核销时间", width = 30, exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date operatedTime;

    @ApiModelProperty("操作人")
    @Excel(name = "操作人", width = 30)
    private String operatedByName;

    @ApiModelProperty("实付款")
    @Excel(name = "实收总额", width = 30)
    private BigDecimal realPay;

    @ApiModelProperty("手续费")
    @Excel(name = "手续费总额", width = 30)
    private BigDecimal thirdPartyFee;

    @ApiModelProperty("促销费")
    @Excel(name = "优惠券总额", width = 30)
    private BigDecimal discount;

    @ApiModelProperty("实结款")
    @Excel(name = "实结总额", width = 30)
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
