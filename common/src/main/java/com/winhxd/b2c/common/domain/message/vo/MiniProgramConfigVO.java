package com.winhxd.b2c.common.domain.message.vo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.context.annotation.PropertySource;

/**
 * @author chengyy
 * @Description: 小程序配置信息
 * @date 2018/8/10 16:23
 */
@ApiModel("小程序配置信息VO")
@Data
public class MiniProgramConfigVO {
    @ApiModelProperty("web页面地址url")
    private String webPageUrl;
    @ApiModelProperty("用户名")
    private String userName;
    @ApiModelProperty("跳转小程序页面")
    private String path;
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("描述信息")
    private String description;
    @ApiModelProperty("交易")
    private String transaction;

}
