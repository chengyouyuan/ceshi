package com.winhxd.b2c.common.domain.pay.condition;

import com.winhxd.b2c.common.domain.common.PagedCondition;
import com.winhxd.b2c.common.domain.pay.vo.DoubleDate;
import com.winhxd.b2c.common.domain.pay.vo.DoubleDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel("门店提现申请列表查询条件")
@Data
public class PayWithdrawalsListCondition extends PagedCondition {

    @ApiModelProperty("门店ID")
    private Long storeId;

    @ApiModelProperty("联系电话")
    private String mobile;

    @ApiModelProperty("审核状态")
    private Short auditStatus;

    @ApiModelProperty("提现状态")
    private Short callbackStatus;

    @ApiModelProperty("提现时间")
    private DoubleDate withdrawalsDate;

    @ApiModelProperty("提现时间开始")
    private Date withdrawalsDateStart;

    @ApiModelProperty("提现时间结束")
    private Date withdrawalsDateEnd;

    @ApiModelProperty("提现金额开始")
    private DoubleDecimal totalFee;

    @ApiModelProperty("提现金额开始")
    private BigDecimal totalFeeStart;

    @ApiModelProperty("提现金额结束")
    private BigDecimal totalFeeEnd;

    @ApiModelProperty("是否查询全部数据")
    private Boolean isQueryAll = false;
}
