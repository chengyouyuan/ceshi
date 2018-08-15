package com.winhxd.b2c.common.domain.store.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 创建小店成功后返回云信账号密码
 * @author liutong
 * @date 2018-08-15 15:22:14
 */
@ApiModel("创建小店成功后返回云信账号密码")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StoreMessageAccountVO {

    @ApiModelProperty("账号")
    private String neteaseAccid;

    @ApiModelProperty("用户密码")
    private String neteaseToken;
}
