package com.winhxd.b2c.common.domain.system.sys.condition;

import com.winhxd.b2c.common.domain.condition.BaseCondition;
import lombok.Data;

import java.util.Date;

/**
 * @author zhangzhengyang
 * @description 系统用户condition
 * @date 2018/8/1
 */

@Data
public class SysUserCondition extends BaseCondition {


    private Long userId;

    private String userCode;

    private String userName;

    private String mobile;

    private String ruleName;

}
