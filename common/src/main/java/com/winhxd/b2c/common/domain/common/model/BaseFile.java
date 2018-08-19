package com.winhxd.b2c.common.domain.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: cdn文件基类
 * @author: zhanglingke
 * @create: 2018-08-06 11:26
 **/
@ApiModel("cdn文件")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BaseFile {
    @ApiModelProperty(value = "文件名称", required = true)
    private String Name;
    @ApiModelProperty(value = "文件路径", required = true)
    private String Url;
}
