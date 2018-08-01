package com.winhxd.b2c.common.domain.system.sys.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author zhangzhengyang
 * @description 系统用户DTO
 * @date 2018/8/1
 */

@Data
public class SysUserPasswordDTO {

    private Long id;

    private String password;

    private String newPassword;

    private String updatedBy;

    private Date updated;

}
