package com.winhxd.b2c.common.domain.message.condition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author jujinbiao
 * @className NeteaseAccountCondition
 * @description 云信用户
 */
@ApiModel("云信用户")
@Data
public class NeteaseAccountCondition {
    @ApiModelProperty("B端用户主键")
    private Long customerId;

    @ApiModelProperty("网易云通信昵称")
    private String name;

    @ApiModelProperty("扩展字段json格式")
    private String props;

    @ApiModelProperty("头像URL")
    private String icon;

    @ApiModelProperty("手机号")
    private String mobile;
}
