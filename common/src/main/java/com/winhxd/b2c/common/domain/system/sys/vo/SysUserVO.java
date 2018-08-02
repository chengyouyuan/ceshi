package com.winhxd.b2c.common.domain.system.sys.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author zhangzhengyang
 * @description 系统用户VO
 * @date 2018/8/1
 */
@ApiModel("系统用户视图对象")
@Data
public class SysUserVO {

    @ApiModelProperty(value = "编号")
    private Long id;

    @ApiModelProperty(value = "账号")
    private String userCode;

    @ApiModelProperty(value = "姓名")
    private String userName;

    @ApiModelProperty(value = "手机")
    private String mobile;

    @ApiModelProperty(value = "状态（0：未启用，1：启用）")
    private Short status;

    @ApiModelProperty(value = "创建人")
    private String createdBy;

    @ApiModelProperty(value = "创建时间")
    private Date created;

    @ApiModelProperty(value = "修改人")
    private String updatedBy;

    @ApiModelProperty(value = "修改时间")
    private Date updated;

    @ApiModelProperty(value = "角色编号")
    private Long ruleId;

    @ApiModelProperty(value = "角色名称")
    private String ruleName;

    @ApiModelProperty(value = "权限列表")
    private List<String> permissions;

}
