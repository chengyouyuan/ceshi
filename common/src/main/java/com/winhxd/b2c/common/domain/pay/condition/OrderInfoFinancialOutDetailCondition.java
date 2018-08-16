package com.winhxd.b2c.common.domain.pay.condition;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.winhxd.b2c.common.domain.common.PagedCondition;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhanghuan
 * @date 2018/8/15
 * 财务明细出账反参
 */
@Data
public class OrderInfoFinancialOutDetailCondition extends PagedCondition implements Serializable {
    private static final long serialVersionUID = -2960449858467596227L;
    @ApiModelProperty(value = "1门店提现 2用户退款")
    private Date outType;
    @ApiModelProperty(value = "下单门店区域编码")
    private String regionCode;
    @ApiModelProperty(value = "下单门店区域编码对应的名称")
    private String regionName;
    @ApiModelProperty(value = "申请开始时间")
    private Date applyStartTime;
    @ApiModelProperty(value = "申请结束时间")
    private Date applyEndTime;
}