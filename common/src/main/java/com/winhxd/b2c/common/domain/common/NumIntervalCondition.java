package com.winhxd.b2c.common.domain.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 接收数字区间搜索条件参数
 *
 * @author songkai
 * @date 2018/8/11 09:36
 * @description
 */
@ApiModel("接收数字区间搜索条件参数")
@Data
public class NumIntervalCondition {

    @ApiModelProperty(value = "开始数字")
    private Date startNum;

    @ApiModelProperty(value = "结束数字")
    private Date endNum;
}
