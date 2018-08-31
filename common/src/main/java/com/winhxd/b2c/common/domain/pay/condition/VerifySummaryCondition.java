package com.winhxd.b2c.common.domain.pay.condition;

import com.fasterxml.jackson.annotation.JsonFormat;
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

        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date lastRecordedTime;

        private Integer verifyStatus;
    }

    @ApiModelProperty("门店列表")
    private List<StoreAndDateVO> list = new ArrayList<>();

    @ApiModelProperty("操作备注")
    private String verifyRemark;

    @ApiModelProperty("操作人ID")
    private Long operatedBy;

    @ApiModelProperty("操作人姓名")
    private String operatedByName;
}
