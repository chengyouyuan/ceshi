package com.winhxd.b2c.common.domain.pay.condition;

import com.winhxd.b2c.common.domain.common.PagedCondition;
import com.winhxd.b2c.common.domain.pay.vo.DoubleDate;
import com.winhxd.b2c.common.domain.pay.vo.DoubleDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@ApiModel("结算汇总查询条件")
@Data
public class VerifySummaryListCondition extends PagedCondition implements Serializable {

    @ApiModelProperty("结算状态")
    private Integer verifyStatus;

    @ApiModelProperty("门店ID")
    private Long storeId;

    @ApiModelProperty("费用入账时间")
    private DoubleDate recordedDate;

    @ApiModelProperty("费用入账时间开始")
    private Date recordedDateStart;

    @ApiModelProperty("费用入账时间结束")
    private Date recordedDateEnd;

    @ApiModelProperty("结算时间")
    private DoubleDate verifyDate;

    @ApiModelProperty("结算时间开始")
    private Date verifyDateStart;

    @ApiModelProperty("结算时间结束")
    private Date verifyDateEnd;

    @ApiModelProperty("实付款")
    private DoubleDecimal realPayMoney;

    @ApiModelProperty("实付款开始")
    private BigDecimal realPayMoneyStart;

    @ApiModelProperty("实付款结束")
    private BigDecimal realPayMoneyEnd;

    @ApiModelProperty("实结款")
    private DoubleDecimal realVerifyMoney;

    @ApiModelProperty("实结款开始")
    private BigDecimal realVerifyMoneyStart;

    @ApiModelProperty("实结款结束")
    private BigDecimal realVerifyMoneyEnd;

    @ApiModelProperty("是否查询全部数据")
    private Boolean isQueryAll = false;
}
