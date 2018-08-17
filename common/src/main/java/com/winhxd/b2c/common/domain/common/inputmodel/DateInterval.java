package com.winhxd.b2c.common.domain.common.inputmodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 接收日期区间搜索条件参数
 *
 * @author songkai
 * @date 2018/8/11 09:36
 * @description
 */
@ApiModel("接收日期区间搜索条件参数")
@Data
public class DateInterval {

    @ApiModelProperty(value = "开始时间")
    private Date start;

    @ApiModelProperty(value = "结束时间")
    private Date end;
}
