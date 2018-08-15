package com.winhxd.b2c.common.domain.pay.condition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ApiModel("按费用明细结算条件")
@Data
public class VerifyDetailCondition implements Serializable {

    @ApiModelProperty("费用明细列表")
    private List<Long> ids = new ArrayList<>();

    @ApiModelProperty("核销备注")
    private String verifyRemark;

    @ApiModelProperty("核销人ID")
    private Long operatedBy;

    @ApiModelProperty("核销人姓名")
    private String operatedByName;
}
