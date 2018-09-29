package com.winhxd.b2c.common.domain.store.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chengyy
 * @Description: 分享门店小程序码数据
 * @date 2018/8/15 19:02
 */
@ApiModel("分享门店小程序码数据")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class QRCodeInfoVO {

    @ApiModelProperty("小程序码地址")
    private String miniProgramCodeUrl;

    @ApiModelProperty("门店名称")
    private String storeName;

    @ApiModelProperty("门店简称")
    private String storeShortName;

}
