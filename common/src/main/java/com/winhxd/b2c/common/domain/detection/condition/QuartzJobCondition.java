package com.winhxd.b2c.common.domain.detection.condition;

import com.winhxd.b2c.common.domain.common.PagedCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("任务查询条件")
public class QuartzJobCondition extends PagedCondition {

    /**
     * 用户ID
     */
    @ApiModelProperty("任务ID")
    private Long quartzJobId;
    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private Long userId;

    /**
     * 数据源ID
     */
    @ApiModelProperty("数据源ID")
    private Long dbId;

    /**
     * 任务名称
     */
    @ApiModelProperty("任务名称")
    private String jobName;

    /**
     * 任务状态
     */
    @ApiModelProperty("任务状态")
    private String jobStatus;

}