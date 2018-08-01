package com.winhxd.b2c.common.domain.system.sys.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author zhangzhengyang
 * @description 系统用户VO
 * @date 2018/8/1
 */

@Data
public class SysUserVO {

    private Long id;

    private String userCode;

    private String userName;

    private String mobile;

    private Short status;

    private String password;

    private String createdBy;

    private Date created;

    private String updatedBy;

    private Date updated;

    private Long ruleId;

    private String ruleName;

    private List<String> permissions;

}
