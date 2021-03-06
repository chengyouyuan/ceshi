package com.winhxd.b2c.common.domain.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 通用Api请求参数
 */
@Data
public class ApiCondition {
    @ApiModelProperty(value = "版本号", required = true)
    private String ver;
    @ApiModelProperty(value = "客户端语言", required = true)
    private String lang;
    @ApiModelProperty("App客户端信息")
    private MobileInfo mobileInfo;

    @Data
    public static class MobileInfo {
        @ApiModelProperty(value = "记录订单下单渠道,如ios,android等")
        private String platform;

        @ApiModelProperty(value = "did号")
        private String did;

        @ApiModelProperty(value = "门店下单时所用设备")
        private String imei;

        @ApiModelProperty(value = "src")
        private String src;

        @ApiModelProperty(value = "安卓模拟器ID，用于记录日志使用")
        private String emulatordid;

        @ApiModelProperty(value = "数盟ID，用于判断手机是否多终端登录")
        private String smDid;
    }
}
