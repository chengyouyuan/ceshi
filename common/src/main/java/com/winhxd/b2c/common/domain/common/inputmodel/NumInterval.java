package com.winhxd.b2c.common.domain.common.inputmodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 接收数字/金额区间搜索条件参数
 *
 * @author songkai
 * @date 2018/8/11 09:36
 * @description
 */
@ApiModel("接收数字/金额区间搜索条件参数")
@Data
public class NumInterval {

    @ApiModelProperty(value = "开始数字")
    private BigDecimal start;

    @ApiModelProperty(value = "结束数字")
    private BigDecimal end;
}
