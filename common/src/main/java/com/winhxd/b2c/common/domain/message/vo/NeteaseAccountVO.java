package com.winhxd.b2c.common.domain.message.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author jujinbiao
 * @className NeteaseAccountVO
 * @description 云信用户
 */
@ApiModel("云信用户")
@Data
public class NeteaseAccountVO {
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("B端用户主键")
    private Long customerId;

    @ApiModelProperty("B端用户主键")
    private String accid;

    @ApiModelProperty("网易云通信昵称")
    private String name;

    @ApiModelProperty("扩展字段json格式")
    private String props;

    @ApiModelProperty("头像URL")
    private String icon;

    @ApiModelProperty("用户密码")
    private String token;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("创建时间")
    private Date created;
}
