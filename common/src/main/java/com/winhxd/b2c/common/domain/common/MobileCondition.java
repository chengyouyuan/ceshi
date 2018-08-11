package com.winhxd.b2c.common.domain.common;

import com.winhxd.b2c.common.domain.common.ApiCondition;
import io.swagger.annotations.ApiModelProperty;

/**
 * 接收手机端参数
 * 手机端接口继承该类
 *
 * @author yuluyuan
 */
public class MobileCondition extends ApiCondition {

    @ApiModelProperty(value = "记录订单下单渠道,如ios,android等")
    private String platform;

    @ApiModelProperty(value = "did号")
    private String did;

    @ApiModelProperty(value = "门店下单时所用设备")
    private String imei;

    @ApiModelProperty(value = "src")
    private String src;

    @ApiModelProperty(value = "lang")
    private String lang;

    @ApiModelProperty(value = "安卓模拟器ID，用于记录日志使用")
    private String emulatordId;

    @ApiModelProperty(value = "数盟ID，用于判断手机是否多终端登录")
    private String smDid;

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getEmulatordId() {
        return emulatordId;
    }

    public void setEmulatordId(String emulatordId) {
        this.emulatordId = emulatordId;
    }

    public String getSmDid() {
        return smDid;
    }

    public void setSmDid(String smDid) {
        this.smDid = smDid;
    }
}