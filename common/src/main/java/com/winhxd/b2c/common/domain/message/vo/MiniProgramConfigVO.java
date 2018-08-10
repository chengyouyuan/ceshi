package com.winhxd.b2c.common.domain.message.vo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chengyy
 * @Description: 小程序配置信息
 * @date 2018/8/10 16:23
 */
@ApiModel("小程序配置信息VO")
@Data
public class MiniProgramConfigVO {

    /**
     * 第三方用户唯一凭证
     */
    @ApiModelProperty("第三方用户唯一凭证")
    private String appid;

    /**
     * 第三方用户唯一凭证密码
     */
    @ApiModelProperty("第三方用户唯一凭证密码")
    private String secret;

    /**
     * 扫描二维码跳转默认页面
     */
    @ApiModelProperty("扫描二维码跳转默认页面")
    private String pageUrl;

}
