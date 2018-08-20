package com.winhxd.b2c.common.domain.pay.condition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@ApiModel("门店提现申请审核条件")
@Data
public class ApproveStoreWithdrawalsCondition {

    @ApiModelProperty("提现申请ID列表")
    private List<Long> ids = new ArrayList<>();

    @ApiModelProperty("提现申请门店ID列表")
    private List<Long> storeIds = new ArrayList<>();

    @ApiModelProperty("审核状态")
    private Short auditStatus;

    @ApiModelProperty("审核意见")
    private String auditDesc;

    @ApiModelProperty("操作人")
    private Long updatedBy;

    @ApiModelProperty("操作人姓名")
    private String updatedByName;
}
