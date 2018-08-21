package com.winhxd.b2c.common.domain.pay.condition;

import com.winhxd.b2c.common.domain.common.PagedCondition;
import com.winhxd.b2c.common.domain.pay.vo.DoubleDate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@ApiModel("费用明细查询条件")
@Data
public class VerifyDetailListCondition extends PagedCondition implements Serializable {

    @ApiModelProperty("结算状态")
    private Integer verifyStatus;

    @ApiModelProperty("第三方平台与惠下单结算状态")
    private Integer thirdPartyVerifyStatus;

    @ApiModelProperty("门店ID")
    private Long storeId;

    @ApiModelProperty("费用类型")
    private Integer detailType;

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

    @ApiModelProperty("是否查询全部数据")
    private Boolean isQueryAll = false;
}
