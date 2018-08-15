package com.winhxd.b2c.common.domain.pay.condition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ApiModel("费用明细操作条件")
@Data
public class VerifyDetailCondition implements Serializable {

    @ApiModelProperty("费用明细列表")
    private List<Long> ids = new ArrayList<>();

    @ApiModelProperty("操作备注")
    private String verifyRemark;

    @ApiModelProperty("操作人ID")
    private Long operatedBy;

    @ApiModelProperty("操作人姓名")
    private String operatedByName;
}
