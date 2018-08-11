package com.winhxd.b2c.common.domain.system.user.condition;

import com.winhxd.b2c.common.domain.common.BaseCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("系统管理权组查询参数")
@Data
public class SysRoleCondition extends BaseCondition {

    @ApiModelProperty("权组编号")
    private Long id;

    @ApiModelProperty("权组名称")
    private String ruleName;

}