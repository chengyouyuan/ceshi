package com.winhxd.b2c.common.domain.pay.condition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ApiModel("按汇总结算条件")
@Data
public class VerifySummaryCondition implements Serializable {

    @Data
    public static class StoreAndDateVO {

        private Long storeId;

        private Date date;
    }

    @ApiModelProperty("门店列表")
    private List<StoreAndDateVO> list = new ArrayList<>();

    @ApiModelProperty("核销备注")
    private String verifyRemark;

    @ApiModelProperty("核销人ID")
    private Long operatedBy;

    @ApiModelProperty("核销人姓名")
    private String operatedByName;
}
