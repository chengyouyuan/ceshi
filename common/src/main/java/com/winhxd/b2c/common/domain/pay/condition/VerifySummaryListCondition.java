package com.winhxd.b2c.common.domain.pay.condition;

import com.winhxd.b2c.common.domain.common.PagedCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@ApiModel("结算汇总查询条件")
@Data
public class VerifySummaryListCondition extends PagedCondition implements Serializable {

    @ApiModelProperty("结算状态")
    private Integer verifyStatus;

    @ApiModelProperty("门店ID")
    private Long storeId;

    @ApiModelProperty("费用入账时间开始")
    private Date recordedDateStart;

    @ApiModelProperty("费用入账时间结束")
    private Date recordedDateEnd;

    @ApiModelProperty("结算时间开始")
    private Date verifyDateStart;

    @ApiModelProperty("结算时间结束")
    private Date verifyDateEnd;

    @ApiModelProperty("实付款开始")
    private Date realPayMoneyStart;

    @ApiModelProperty("实付款结束")
    private Date realPayMoneyEnd;

    @ApiModelProperty("实结款开始")
    private Date realVerifyMoneyStart;

    @ApiModelProperty("实结款结束")
    private Date realVerifyMoneyEnd;
}
