package com.winhxd.b2c.common.domain.system.sys.dto;

import lombok.Data;

/**
 * @author zhangzhengyang
 * @description 系统用户DTO
 * @date 2018/8/1
 */

@Data
public class SysUserDTO {

    private Long id;

    private String userCode;

    private String userName;

    private String mobile;

    private Short status;

    private String password;

    private Long ruleId;

}
