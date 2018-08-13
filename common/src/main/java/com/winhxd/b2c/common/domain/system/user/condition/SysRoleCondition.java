<<<<<<< HEAD
package com.winhxd.b2c.common.domain.system.user.condition;

import com.winhxd.b2c.common.domain.base.condition.BaseCondition;
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

=======
package com.winhxd.b2c.common.domain.system.user.condition;

import com.winhxd.b2c.common.domain.common.PagedCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("系统管理权组查询参数")
@Data
public class SysRoleCondition extends PagedCondition {

    @ApiModelProperty("权组编号")
    private Long id;

    @ApiModelProperty("权组名称")
    private String ruleName;

>>>>>>> branch 'master' of git@192.168.1.101:retail2c/retail2c-backend.git
}